pipeline {
    agent any
    tools {
        maven 'Maven'
    }

    environment {
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk-amd64'
    }

    stages {
        stage('Checkout'){
            steps {
                checkout scm
            }
        }

        stage('Build Backend'){
            steps {
                dir('app-backend') {
                    sh 'mvn clean package verify'
                }
            }
        }

        stage('Verify Jacoco Exec') {
            steps {
                dir('app-backend') {
                    sh 'ls -l target'
                }
            }
        }
    }

    post{
        success {
            script {
                jacoco (
                execPattern: 'app-backend/target/jacoco.exec',
                classPattern: 'app-backend/target/classes',
                sourcePattern: 'app-backend/src/main/java'
                    exclusionPattern: '**/target/test-classes',
                    changeBuildStatus: true,
                    minimumLineCoverage: '85'
                )            
            }

            echo 'Backend build completed successfully'
        }

        failure{
            echo 'Backend build failed'
        }
    }
}
