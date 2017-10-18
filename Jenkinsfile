pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                cleanWs()
                checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/$BRANCH_NAME']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'git@github-jsposato_test:Mobiquity/androidunittests.git']]]
                sh './gradlew assembleDebug'
            }
        }
        stage('Test') {
            steps {
                echo 'Testing..'
                cleanWs deleteDirs: true, patterns: [[pattern: 'app/build/*', type: 'INCLUDE']]
                sh './gradlew testDebugUnitTest'

            } post {
                always {
                    junit keepLongStdio: true, testResults: 'app/build/test-reports/debug/*.xml'
                }
            }
        }
        stage('Static Code Analysis') {
            steps {
                echo 'Scanning with SonarQube...'

                withSonarQubeEnv('Mob Sonar') {
                    sh "/opt/sonar-runner/bin/sonar-scanner"
                }
            }
        }
        stage('Checkmarx Scan') {
            steps {
                echo 'Scanning with Checkmarx...'
            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}