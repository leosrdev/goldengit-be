pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '2'))
  }
  environment {
    DOCKERHUB_CREDENTIALS = credentials('docker-hub-token')
  }
    stages {
        stage('Git: checkout project') {
            steps {
                git(
                    url: 'https://github.com/leosrdev/goldengit-be.git',
                    branch: 'main',
                    credentialsId: 'c477bdec-e3da-4b84-935b-6b29973fc8a5'
                )
            }
        }
        stage('Maven: create JAR package') {
            steps {
                sh 'mvn package -Dmaven.test.skip=true'
            }
        }
        stage('Docker: build image') {
            steps {
                sh 'docker build -t leosrdev/goldengit:latest .'
            }
        }
        stage('Docker: login') {
          steps {
            sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
          }
        }
        stage('Docker: push to registry') {
            steps {
                sh "docker push leosrdev/goldengit:latest"
            }
        }
        stage('Docker: deploy image') {
            steps {
                sh "docker service create --env-file /opt/goldengit/prod.env --replicas 2 -p 8080:8080 --name goldengit-service leosrdev/goldengit:latest"
            }
        }
    }
  post {
    always {
      sh 'docker logout'
    }
  }
}