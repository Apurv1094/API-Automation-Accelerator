pipeline {
 agent any
 tools { jdk 'JDK17'; maven 'Maven3' }
 parameters {
  choice(name: 'ENV', choices: ['local', 'qa'], description: 'Config environment')
  choice(name: 'TEST_GROUP', choices: ['all', 'smoke', 'regression', 'negative', 'auth'], description: 'TestNG group')
 }
 stages {
  stage('Checkout') { steps { checkout scm } }
  stage('API Tests') {
   steps {
    withCredentials([usernamePassword(credentialsId: 'api-test-credentials', usernameVariable: 'API_USERNAME', passwordVariable: 'API_PASSWORD')]) {
     script {
      def groupArg = params.TEST_GROUP == 'all' ? '' : "-Dgroups=${params.TEST_GROUP}"
      if (isUnix()) { sh "mvn -B clean test -Denv=${params.ENV} ${groupArg}" }
      else { bat "mvn -B clean test -Denv=${params.ENV} ${groupArg}" }
     }
    }
   }
  }
 }
 post { always { junit allowEmptyResults: true, testResults: 'target/surefire-reports/TEST-*.xml'; archiveArtifacts allowEmptyArchive: true, artifacts: 'target/surefire-reports/**' } }
}
