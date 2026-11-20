
pipeline {
    agent any

    tools {
        jdk 'JDK21'
        maven 'Maven 3.10.1'
    }

    parameters {
        choice(
            name: 'ENV',
            choices: ['local', 'qa', 'ci'],
            description: 'Select API environment'
        )

        choice(
            name: 'TEST_GROUP',
            choices: ['all', 'smoke', 'regression', 'negative', 'auth'],
            description: 'Select TestNG group'
        )
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Prepare Configuration') {
            steps {
                script {
                    if (params.ENV == 'local') {
                        if (isUnix()) {
                            sh 'cp src/test/resources/config/local.properties.example src/test/resources/config/local.properties'
                        } else {
                            bat 'copy /Y src\\test\\resources\\config\\local.properties.example src\\test\\resources\\config\\local.properties'
                        }
                    }
                }
            }
        }

        stage('API Tests') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'api-test-credentials',
                        usernameVariable: 'API_USERNAME',
                        passwordVariable: 'API_PASSWORD'
                    )
                ]) {
                    script {
                        def groupArg = params.TEST_GROUP == 'all'
                            ? ''
                            : "-Dgroups=${params.TEST_GROUP}"

                        if (isUnix()) {
                            sh "mvn -B clean test -Denv=${params.ENV} ${groupArg}"
                        } else {
                            bat "mvn -B clean test -Denv=${params.ENV} ${groupArg}"
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            junit(
                allowEmptyResults: true,
                testResults: 'target/surefire-reports/TEST-*.xml'
            )

            archiveArtifacts(
                allowEmptyArchive: true,
                artifacts: 'target/surefire-reports/**'
            )
        }
    }
}
