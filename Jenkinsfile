pipeline {
  agent any
  stages {
    stage('checkout') {
      steps {
        git(url: 'git@github.com:wangwenjun/guava_programming.git', branch: 'master', changelog: true, poll: true)
      }
    }
    stage('compile') {
      steps {
        withMaven(jdk: '/opt/java')
      }
    }
    stage('deploy') {
      steps {
        echo 'hello'
      }
    }
  }
}