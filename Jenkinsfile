pipeline {
  agent none
  stages {
    stage('Build') {
      environment {
        REGISTRY_CREDS = credentials('admin_kt')
        REGISTRY = 'harbor.k8s.io:30082'
      }
      agent any
      steps {
        sh 'ls -alF'
        checkout scm
        sh 'ls -alF'
        sh 'chmod 755 mvnw'
        sh './mvnw -Dmaven.test.skip clean package'
//        sh 'sleep 3600'
        sh 'ls -alF target'
        sh "docker login -u ${REGISTRY_CREDS_USR} -p ${REGISTRY_CREDS_PSW} ${REGISTRY}"
        script {
          def BUILD_VERSIONS = ["${env.BUILD_NUMBER}", "latest"]
          BUILD_VERSIONS.each { BUILD_VERSION ->
            sh "docker build -t nlu_project/kt-api-server:${BUILD_VERSION} ."
            sh "docker tag nlu_project/kt-api-server:${BUILD_VERSION} ${REGISTRY}/nlu_project/kt-api-server:${BUILD_VERSION}"
            sh "docker push ${REGISTRY}/nlu_project/kt-api-server:${BUILD_VERSION}"
          }
        }
      }
    }
    stage('K8S Pod') {
      agent {
        kubernetes {
          yamlFile 'jenkins-pod.yaml'
        }
      }
      steps {
        sh 'hostname'
      }
    }
//    stage('K8S Deployment') {
//      agent {
//        kubernetes {
//          yamlFile 'jenkins-deployment.yaml'
//        }
//      }
//      steps {
//        sh 'hostname'
//      }
//    }
//    stage('K8S Service') {
//      agent {
//        kubernetes {
//          yamlFile 'jenkins-service.yaml'
//        }
//      }
//      steps {
//        sh 'hostname'
//      }
//    }
//    stage('K8S Ingress') {
//      agent {
//        kubernetes {
//          yamlFile 'jenkins-ingress.yaml'
//        }
//      }
//      steps {
//        sh 'hostname'
//      }
//    }
  }
}
