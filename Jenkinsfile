pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'Java-8'
    }

    environment {
        APP_NAME = "springboot-demo"
        JAR_PATH = "target/springboot-demo-0.0.1-SNAPSHOT.jar"
    }

    stages {

        stage('Checkout Code') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/anurag-gupta-1086/springboot-demo.git'
            }
        }

        stage('Clean & Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Run Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Archive Artifact') {
            steps {
                archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            }
        }

        stage('Run Application') {
            steps {
                sh 'java -jar target/springboot-demo-0.0.1-SNAPSHOT.jar'
            }
        }
    }

    post {
        success {
            echo '✅ Build & Deployment Successful'
        }
        failure {
            echo '❌ Build Failed'
        }
    }
}
