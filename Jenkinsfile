pipeline {
    agent any   // Use any Jenkins node

    tools {
        // Make sure these tools are configured in Jenkins under "Global Tool Configuration"
        maven 'Maven3'     // or whatever name you used for Maven
        jdk 'JDK17'        // or your configured JDK version
    }

    environment {
        // Define environment variables if needed
        BUILD_ENV = 'development'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '--- Checking out source code ---'
                checkout scm
                sh 'ls -l'
            }
        }

        stage('Build') {
            steps {
                echo '--- Building project ---'
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                echo '--- Running unit tests ---'
                sh 'mvn test'
            }
        }

        stage('Code Analysis') {
            steps {
                echo '--- Running static analysis (optional) ---'
                // Example: Uncomment if you use SonarQube
                // sh 'mvn sonar:sonar'
            }
        }

        stage('Deploy') {
            when {
                branch 'main'   // Only deploy from main branch
            }
            steps {
                echo '--- Deploying project (main branch only) ---'
                // Add your deployment logic here
                // Example: copy artifacts, call script, or deploy to a server
                sh 'echo "Deploying artifact..."'
            }
        }
    }

    post {
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed. Please check logs.'
        }
        always {
            echo 'Cleaning workspace...'
            cleanWs()
        }
    }
}
