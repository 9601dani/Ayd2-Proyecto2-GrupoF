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

        stage('Build Frontend') {
            steps {
                dir('app-frontend') {
                    sh 'npm install'

                    sh 'ls -l'

                    sh 'mkdir -p src/environments'

                    sh """
                        echo \"export const environment = {\n  production: true,\n  API_URL: '\${API_URL}', \n BUCKET_URL: '\${BUCKET_URL}'\n};\" > src/environments/environment.ts
                    """
                }
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

        stage('Deploy') {
            steps {
                sh 'ls -l'
                sh "./deploy.sh main"
                sshagent(credentials: ['jenkins-ssh']) {
                    sh 'ssh -o StrictHostKeyChecking=no $VM_USERNAME@$PROD_IP uptime'
                    sh 'ssh -v $VM_USERNAME@$PROD_IP'
                    sh 'scp -r deploy $VM_USERNAME@$PROD_IP:/home/$VM_USERNAME/'
                    sh 'ssh -o StrictHostKeyChecking=no $VM_USERNAME@$PROD_IP "bash /home/$VM_USERNAME/deploy/serve.sh"'
                }
            }
        }
    }

    post{
        success {
            script {
                jacoco (
                    execPattern: '**/target/*.exec',
                    classPattern: '**/target/classes',
                    sourcePattern: '**/src/main/java',
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
