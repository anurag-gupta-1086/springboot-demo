pipeline {
    agent any

    tools {
        maven 'Maven-3.9'
        jdk 'Java-8'
    }

    environment {
        APP_NAME = "springboot-demo"
        JAR_NAME = "springboot-demo-0.0.1-SNAPSHOT.jar"
        TARGET_DIR = "target"
        CURRENT_JAR = "springboot-demo-current.jar"
        BACKUP_JAR = "springboot-demo-backup.jar"
        LOG_FILE = "app.log"
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

                echo "Stopping existing application (if any)..."
                pkill -f springboot-demo || true

                cd $TARGET_DIR

                if [ -f "$CURRENT_JAR" ]; then
                    echo "Backing up current version..."
                    mv $CURRENT_JAR $BACKUP_JAR
                fi

                echo "Deploying new version..."
                cp $JAR_NAME $CURRENT_JAR

                echo "Starting application..."
                export JENKINS_NODE_COOKIE=dontKillMe
                nohup java -jar $CURRENT_JAR > $LOG_FILE 2>&1 &

                sleep 5
                echo "Application started. Showing last logs:"
                tail -n 20 $LOG_FILE || true
                '''
            }
        }
    }

    post {
        failure {
            echo '❌ Deployment failed. Starting rollback...'

            sh '''
            set +e

            echo "Stopping failed application..."
            pkill -f springboot-demo || true

            cd $TARGET_DIR

            if [ -f "$BACKUP_JAR" ]; then
                echo "Rolling back to previous version..."
                export JENKINS_NODE_COOKIE=dontKillMe
                nohup java -jar $BACKUP_JAR > rollback.log 2>&1 &
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
