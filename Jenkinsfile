pipeline {
    agent any
    tools {
        maven 'Maven'
        jdk 'jdk-17'
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
                dir('app-backend/report/target') {
                    sh 'ls -l'
                }
            }
        }
    }

    post{
        success {
            script {
                jacoco (
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/*.classes',
                    sourcePttern: '**/src/main/java',
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
