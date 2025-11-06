pipeline {
  agent any

  options {
    // Keep build logs short, etc.
    skipDefaultCheckout() // we will checkout explicitly after secret validation
  }

  environment {
    // optional: default server id used in pom.xml's distributionManagement
    MAVEN_SERVER_ID = 'nexus-releases'
  }

  stages {
    stage('Fetch secrets from Delinea') {
      steps {
        // Wrap step: fetch secret id 1234 (replace with your Secret Server secret ID)
        // maps secret fields to environment variables available inside the closure
        script {
          try {
            wrap([$class: 'ServerBuildWrapper',
                  secrets: [[
                    id: 11270,
                    mappings: [
                      [environmentVariable: 'username', field: 'username'],
                      [environmentVariable: 'password', field: 'password']
                    ]
                  ]]
            ]) {
              // inside here, REPO_USER and REPO_PASS environment vars are available
              if (!env.username || !env.password) {
                error("Failed to fetch repository credentials from Secret Server (empty fields).")
              }
              echo "Successfully fetched repo credentials (user: ${env.username})"
            }
          } catch (err) {
            // fail fast and avoid checking out the branch if secrets could not be retrieved
            error("Could not fetch Delinea secret: ${err}")
          }
        }
      }
    }

    stage('Checkout source') {
      steps {
        // perform the actual checkout now that secrets are validated
        checkout scm
      }
    }

    stage('Build') {
      steps {
        // use withMaven if plugin installed, or call mvn directly
        withMaven(maven: 'Maven3') {
          sh 'mvn -B -DskipTests clean package'
        }
      }
    }

    stage('Prepare Maven settings.xml') {
      steps {
        script {
          // write settings.xml that contains server credentials for MAVEN_SERVER_ID
          // (this is a simple pattern — you can do encrypted settings-security.xml if required)
          def settings = """
          <settings xmlns="http://maven.apache.org/SETTINGS/1.0.0"
                    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                    xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                                        https://maven.apache.org/xsd/settings-1.0.0.xsd">
            <servers>
              <server>
                <id>${env.MAVEN_SERVER_ID}</id>
                <username>${env.REPO_USER}</username>
                <password>${env.REPO_PASS}</password>
              </server>
            </servers>
          </settings>
          """
          writeFile file: 'ci-settings.xml', text: settings
          echo "settings.xml created at workspace/ci-settings.xml"
        }
      }
    }

    stage('Publish artifact') {
      steps {
        // deploy using the newly created settings file
        sh 'mvn -B -s ci-settings.xml deploy -DskipTests'
      }
    }
  }

  post {
    always {
      // scrub logs or remove created settings from workspace
      sh 'rm -f ci-settings.xml || true'
    }
    success {
      echo "Build & deploy succeeded"
    }
    failure {
      echo "Build failed"
    }
  }
}
