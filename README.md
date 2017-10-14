# guava_programming
initial


背景： 
最近公司因为用的云服务器，需要保证kafka的安全性。可喜的是kafka0.9开始，已经支持权限控制了。网上中文资料又少，特此基于kafka0.9，记录kafaka的权限控制 ( flume需要1.7及其以上才支持kafka的SSL认证)。

下面各位看官跟着小二一起开始kafak权限认证之旅吧！嘎嘎嘎！

介绍： 
kafka权限控制整体可以分为三种类型： 
1.基于SSL(CDH 5.8不支持) 
2.基于Kerberos(此认证一般基于CDH，本文不与讨论) 
3.基于acl的 (CDH5.8中的kafka2.X不支持 )

本文主要基于apace版本的，实现1和3，也是用的最多的展开讨论。

统一说明： 
在本文中&符号表示注释

一，准备工作 
组件分布 
kafka centos11，centos12，centos13 
zoopeeker centos11，centos12，centos13

二、在kafka集群任选一台机子 ( 先介绍基于SSL的 )

密码统一为123456

&Step 1  Generate SSL key and certificate for each Kafka broker
keytool -keystore server.keystore.jks -alias centos11 -validity 365 -genkey

%Step 2  Creating your own CA
openssl req -new -x509 -keyout ca-key -out ca-cert -days 365
keytool -keystore server.truststore.jks -alias CARoot -import -file ca-cert
keytool -keystore client.truststore.jks -alias CARoot -import -file ca-cert

&Step 3  Signing the certificate
keytool -keystore server.keystore.jks -alias centos11 -certreq -file cert-file
openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file -out cert-signed -days 365 -CAcreateserial -passin pass:123456
keytool -keystore server.keystore.jks -alias CARoot -import -file ca-cert
keytool -keystore server.keystore.jks -alias centos11 -import -file cert-signed
1
2
3
4
5
6
7
8
9
10
11
12
13
三、其他的kafka集群

 &机器centos13  centos12
 keytool -keystore kafka.client.keystore.jks -alias centos13 -validity 365 -genkey 
  keytool -keystore kafka.client.keystore.jks -alias centos13 -certreq -file cert-file
  cp cert-file cert-file-centos13

 &centos11
 scp ./ca* ce* server* root@centos13:/opt/kafka_2.10/

  openssl x509 -req -CA ca-cert -CAkey ca-key -in cert-file-centos13  -out cert-signed-centos13  -days 365 -CAcreateserial -passin pass:123456 
 keytool -keystore kafkacentos13.client.keystore.jks -alias CARoot -import -file ca-cert 
 keytool -keystore kafkacentos13.client.keystore.jks -alias centos13 -import -file cert-signed-centos13 



rm -rf producer.properties
echo "bootstrap.servers=centos13:9093" >> producer.properties  
echo "security.protocol=SSL" >> producer.properties  
echo "ssl.truststore.location=/opt/kafka_2.10/kafkacentos12.client.keystore.jks">> producer.properties  
echo "ssl.truststore.password=123456">> producer.properties  
echo "ssl.keystore.location=/opt/kafka_2.10/server.keystore.jks">> producer.properties  
echo "ssl.keystore.password=123456">> producer.properties  
echo "ssl.key.password=123456">> producer.properties   
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
4.验证：

openssl s_client -debug -connect localhost:9093 -tls1

output:
    -----BEGIN CERTIFICATE-----
        {variable sized random bytes}
        -----END CERTIFICATE-----
        subject=/C=US/ST=CA/L=Santa Clara/O=org/OU=org/CN=Sriharsha Chintalapani
        issuer=/C=US/ST=CA/L=Santa Clara/O=org/OU=org/CN=kafka/emailAddress=test@test.com
1
2
3
4
5
6
7
8
5.使用：

bin/kafka-console-consumer.sh --bootstrap-server kafka2:9093 --topic test --new-consumer --consumer.config  config/producer.properties

bin/kafka-console-producer.sh --broker-list centos11:9093 --topic test --producer.config  ./config/producer.properties  

bin/kafka-console-consumer.sh --bootstrap-server centos11:9093 --topic test --new-consumer --consumer.config ./config/producer.properties 

bin/kafka-console-consumer.sh --bootstrap-server centos13:9093 --topic test --new-consumer --consumer.config ./config/producer.properties  --from-beginning
1
2
3
4
5
6
7
6.基于ACL

server.properties中加配置

allow.everyone.if.no.acl.found=true
super.users=User:root
authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer  
principal.builder.class=org.apache.kafka.common.security.auth.DefaultPrincipalBuilder 
1
2
3
4
7.ACL的简单使用：

bin/kafka-acls.sh --authorizer-properties zookeeper.connect=centos11:2181 --add --allow-principal User:Bob --allow-principal User:Alice --allow-host 198.51.100.0 --allow-host 198.51.100.1 --operation Read --operation Write --topic test

bin/kafka-acls.sh --authorizer-properties zookeeper.connect=centos11:2181 --remove --allow-principal User:Bob --allow-principal User:Alice --allow-host 198.51.100.0 --allow-host 198.51.100.1 --operation Read --operation Write --topic test
1
2
3
8.Java Demo 
需要将server.keystore.jks、client.truststore.jks从任一台机器上拷贝下来即可。

SSL is supported only for the new Kafka Producer and Consumer, the older API is not supported

ConsumerDemo

package xmhd.examples;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SslConfigs;
import java.util.Arrays;
import java.util.Properties;
/**
  * Created by shengjk1.
  * blog address :http://blog.csdn.net/jsjsjs1789
 *
 *  生产者可以保证权限认证
 *  SSL is supported only for the new Kafka Producer and Consumer, the older API is not supported
 */
public class ConsumerZbdba {
    public static void main(String[] args) {
//      new ConsumerZbdba("test").start();// 使用kafka集群中创建好的主题 test
        Properties props = new Properties();
/* 定义kakfa 服务的地址，不需要将所有broker指定上 */
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos11:9093;centos13:9093;centos12:9093");
/* 制定consumer group */
        props.put("group.id", "test");
        props.put("auto.offset.reset","earliest");
/* 是否自动确认offset */
//      props.put("enable.auto.commit", "true");
//      props.put(ProducerConfig.CLIENT_ID_CONFIG, "myApiKey");
        props.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "F:\\server.keystore.jks");
        props.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "123456");
        props.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "F:\\client.truststore.jks");
        props.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "123456");
        props.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
//


///* 自动确认offset的时间间隔 */
//      props.put("auto.commit.interval.ms", "1000");
//      props.put("session.timeout.ms", "30000");
/* key的序列化类 */
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
/* value的序列化类 */
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
 /* 定义consumer */
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);
/* 消费者订阅的topic, 可同时订阅多个 */
        consumer.subscribe(Arrays.asList("test"));

 /* 读取数据，读取超时时间为100ms */
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s", record.offset(), record.key(), record.value()+"\n");
        }
    } 
}
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
52
53
54
55
56
57
58
ProducerDemo

package xmhd.examples;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.config.SslConfigs;
import java.util.Properties;
/**
 * Created by shengjk1.
 * blog address :http://blog.csdn.net/jsjsjs1789
 *  生产者可以保证权限认证
 */
public class ProducerZbdba {
    public static void main(String[] args) {
         Properties producerProps = new Properties();
         producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "centos12:9093");
//         producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "myApiKey");
        producerProps.put(SslConfigs.SSL_KEYSTORE_LOCATION_CONFIG, "F:\\server.keystore.jks");
        producerProps.put(SslConfigs.SSL_KEYSTORE_PASSWORD_CONFIG, "123456");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_LOCATION_CONFIG, "F:\\client.truststore.jks");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_PASSWORD_CONFIG, "123456");
        producerProps.put(SslConfigs.SSL_TRUSTSTORE_TYPE_CONFIG, "JKS");
        producerProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, "SSL");
         producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");  
         producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");  
         KafkaProducer producer = new KafkaProducer(producerProps);  
         for(int i = 0; i < 100; i++)  
             producer.send(new ProducerRecord<String, String>("test", Integer.toString(i), Integer.toString(i)));
             System.out.println("test");  
         producer.close();  
    }  
}  
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
8.1 flume1.7 的配置 (基于kafka SSL认证)

tier1.sources  = source1
tier1.channels = channel1
tier1.sinks    = sink1


tier1.sources.source1.type     = exec
tier1.sources.source1.command = tail -F -n+1 /opt/scan.log
tier1.sources.source1.channels = channel1

tier1.channels.channel1.type   = memory
tier1.sinks.sink1.type         = org.apache.flume.sink.kafka.KafkaSink
tier1.sinks.sink1.topic = test
tier1.sinks.sink1.kafka.bootstrap.servers = centos11:9093,centos12:9093,centos13:9093
tier1.channels.channel1.kafka.bootstrap.servers = centos11:9093,centos12:9093,centos13:9093
tier1.sinks.sink1.requiredAcks = 1
tier1.sinks.sink1.batchSize = 100


tier1.sinks.sink1.kafka.producer.security.protocol = SSL
tier1.sinks.sink1.kafka.producer.ssl.truststore.type=JKS
tier1.sinks.sink1.kafka.producer.ssl.truststore.location = /opt/kafka_2.10/server.truststore.jks
tier1.sinks.sink1.kafka.producer.ssl.truststore.password =123456
tier1.sinks.sink1.kafka.producer.security.protocol = SSL
tier1.sinks.sink1.kafka.producer.ssl.keystore.location = /opt/kafka_2.10/server.keystore.jks
tier1.sinks.sink1.kafka.producer.ssl.keystore.password =123456
&tier1.sinks.sink1.kafka.producer.ssl.endpoint.identification.algorithm = HTTPS


tier1.sinks.sink1.channel      = channel1
tier1.channels.channel1.capacity = 100


&tier1.channels.channel1.kafka.producer.security.protocol = SSL
&tier1.channels.channel1.kafka.producer.ssl.truststore.type=JKS
&tier1.channels.channel1.kafka.producer.ssl.truststore.location = /opt/kafka_2.10/server.truststore.jks
&tier1.channels.channel1.kafka.producer.ssl.truststore.password =123456
&tier1.channels.channel1.kafka.producer.security.protocol = SSL
&tier1.channels.channel1.kafka.producer.ssl.keystore.location = /opt/kafka_2.10/server.keystore.jks
&tier1.channels.channel1.kafka.producer.ssl.keystore.password =123456
&tier1.channels.channel1.kafka.producer.ssl.endpoint.identification.algorithm = HTTPS


&tier1.channels.channel1.kafka.consumer.security.protocol = SSL
&tier1.channels.channel1.kafka.consumer.ssl.truststore.location = /opt/kafka_2.10/server.truststore.jks
&tier1.channels.channel1.kafka.consumer.ssl.truststore.password =123456
&tier1.channels.channel1.kafka.consumer.security.protocol = SSL
&tier1.channels.channel1.kafka.consumer.ssl.keystore.location = /opt/kafka_2.10/server.truststore.jks
&tier1.channels.channel1.kafka.consumer.ssl.keystore.password =123456
&tier1.channels.channel1.kafka.consumer.ssl.endpoint.identification.algorithm = HTTPS
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
9.kafak Server.properties最终版的

三台机子需保证一样，centos11为机器名，根据需要自行修改

broker.id=0
############################# Socket Server Settings #############################
&这一点可能需要特别的注意,PLAINTEXT注释掉之后，一些基本的kafka脚本都不在能用了
&listeners=PLAINTEXT://centos11:9092,SSL://centos11:9093
listeners=SSL://centos11:9093
advertised.listeners=SSL://centos11:9093  
ssl.keystore.location=/opt/kafka_2.10/server.keystore.jks  
ssl.keystore.password=123456
ssl.key.password=123456
ssl.truststore.location=/opt/kafka_2.10/server.truststore.jks  
ssl.truststore.password=123456  
ssl.client.auth=required  
ssl.enabled.protocols=TLSv1.2,TLSv1.1,TLSv1  
ssl.keystore.type=JKS  
ssl.truststore.type=JKS  
security.inter.broker.protocol=SSL  

&acl
allow.everyone.if.no.acl.found=true
super.users=User:root
authorizer.class.name=kafka.security.auth.SimpleAclAuthorizer  
principal.builder.class=org.apache.kafka.common.security.auth.DefaultPrincipalBuilder 

host.name=centos11
advertised.host.name=centos11

num.network.threads=3
num.io.threads=8
socket.send.buffer.bytes=102400
socket.receive.buffer.bytes=102400
socket.request.max.bytes=104857600
############################# Log Basics #############################
log.dirs=/opt/kafka-logs
num.partitions=1
num.recovery.threads.per.data.dir=1
log.retention.hours=168
log.segment.bytes=1073741824
log.retention.check.interval.ms=300000
############################# Zookeeper #############################
zookeeper.connect=centos11:2181,centos12:2181,centos13:2181
zookeeper.connection.timeout.ms=6000
1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
kafka producer.properties 
centos11为机器名，根据需求自行修改

bootstrap.servers=centos11:9093
security.protocol=SSL
ssl.truststore.location=/opt/kafka_2.10/client.truststore.jks  
ssl.truststore.password=123456  
ssl.keystore.location=/opt/kafka_2.10/server.keystore.jks  
ssl.keystore.password=123456
ssl.key.password=123456
