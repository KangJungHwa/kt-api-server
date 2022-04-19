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
    stage('K8S Deployment & service') {
      agent any
      steps {
        kubernetesDeploy configs: "jenkins-*.yaml", kubeconfigId: 'kubeconfig'
      }
    }
  }
}
