pipeline {
  agent any

  options {
    skipDefaultCheckout() // we will checkout explicitly after secret validation
  }

  environment {
    MAVEN_SERVER_ID = 'nexus-releases'
  }

  stages {

    stage('Fetch secrets from Delinea') {
      steps {
        script {
          withSecretServer(secrets: [[
            id: '11270',
            mappings: [
              [field: 'username', environmentVariable: 'username'],
              [field: 'password', environmentVariable: 'password']
            ]
          ]]) {
            // promote secrets to global vars for later stages
            env.MAVEN_USERNAME = env.username
            env.MAVEN_PASSWORD = env.password
            echo "Secret fetched successfully from Delinea"
            echo "Username (masked): ${env.MAVEN_USERNAME}"
          }
        }
      }
    }

    stage('Checkout source') {
      steps {
        checkout scm
      }
    }

    stage('Build') {
      steps {
        script {
          if (isUnix()) {
            sh 'mvn -B -DskipTests clean package'
          } else {
            bat 'mvn -B -DskipTests clean package'
          }
        }
      }
    }

    stage('Prepare Maven settings.xml') {
      steps {
        script {
          def settings = """
          <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                        https://maven.apache.org/xsd/settings-1.0.0.xsd">
            <servers>
              <server>
                <id>${env.MAVEN_SERVER_ID}</id>
                <username>${env.MAVEN_USERNAME}</username>
                <password>${env.MAVEN_PASSWORD}</password>
              </server>
            </servers>
          </settings>
          """
          writeFile file: 'ci-settings.xml', text: settings
          echo "Maven settings.xml created at workspace/ci-settings.xml"
        }
      }
    }

    stage('Publish artifact') {
      steps {
        script {
          if (isUnix()) {
            sh 'mvn -B -s ci-settings.xml deploy -PsonatypeDeploy deploy'
          } else {
            bat 'mvn -B -s ci-settings.xml deploy -PsonatypeDeploy deploy'
          }
        }
      }
    }
  }

  post {
    always {
      script {
        if (isUnix()) {
          sh 'rm -f ci-settings.xml || true'
        } else {
          bat 'del ci-settings.xml 2>nul || exit 0'
        }
      }
    }
  }
}
