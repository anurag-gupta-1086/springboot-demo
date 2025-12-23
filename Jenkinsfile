pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'Java-8'
    }

    environment {
        APP_NAME = "springboot-demo"
        JAR_NAME = "springboot-demo-0.0.1-SNAPSHOT.jar"
        WORKSPACE_JAR = "target/springboot-demo-0.0.1-SNAPSHOT.jar"

        DEPLOY_DIR = "/Users/anuraggupta/deploy/springboot-demo"
        CURRENT_JAR = "/Users/anuraggupta/deploy/springboot-demo/current.jar"
        BACKUP_JAR = "/Users/anuraggupta/deploy/springboot-demo/backup.jar"
        LOG_FILE = "/Users/anuraggupta/deploy/springboot-demo/app.log"
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

        stage('Deploy Application') {
            steps {
                sh '''
                set -e

                echo "Preparing deploy directory..."
                mkdir -p $DEPLOY_DIR

                echo "Stopping existing application (if any)..."
                pkill -f springboot-demo || true

                if [ -f "$CURRENT_JAR" ]; then
                    echo "Backing up current version..."
                    mv $CURRENT_JAR $BACKUP_JAR
                fi

                echo "Deploying new version..."
                cp $WORKSPACE_JAR $CURRENT_JAR

                echo "Starting application..."
                export JENKINS_NODE_COOKIE=dontKillMe
                nohup java -jar $CURRENT_JAR > $LOG_FILE 2>&1 &

                sleep 5

                if ps -ef | grep current.jar | grep -v grep > /dev/null; then
                    echo "Application started successfully"
                else
                    echo "Application failed to start"
                    exit 1
                fi
                '''
            }
        }
    }

    post {
        failure {
            echo '❌ Deployment failed. Starting rollback...'

            sh '''
            set +e
            pkill -f springboot-demo || true

            if [ -f "$BACKUP_JAR" ]; then
                echo "Rolling back to previous version..."
                export JENKINS_NODE_COOKIE=dontKillMe
                nohup java -jar $BACKUP_JAR > $DEPLOY_DIR/rollback.log 2>&1 &
                echo "Rollback completed."
            else
                echo "No backup available. Rollback skipped."
            fi
            '''
        }

        success {
            echo '✅ Build & Deployment Successful'
        }
    }
}
