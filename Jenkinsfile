pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'jdk17'
    }

    environment {
        PATH = "${tool 'Maven3'}/bin:${env.PATH}"
    }

    stages {

        stage('Checkout') {
            steps {
                echo '--- Checking out source code ---'
                checkout scm
            }
        }

        stage('Build') {
            steps {
                echo '--- Building the project ---'
                bat 'mvn clean install'
            }
        }

        stage('Test') {
            steps {
                echo '--- Running tests ---'
                bat 'mvn test'
            }
        }

        stage('Code Analysis') {
            steps {
                echo '--- Running static analysis ---'
                bat 'mvn checkstyle:check'
            }
        }

        stage('Deploy') {
            steps {
                echo '--- Deploying artifact ---'
                // Example deployment command
                bat 'echo Deploy step placeholder'
            }
        }
    }

    post {
        always {
            echo 'Cleaning workspace...'
            cleanWs() // workspace cleanup
        }
        failure {
            echo 'Build failed. Please check logs.'
        }
        success {
            echo 'Build succeeded!'
        }
    }
}
