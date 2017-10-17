pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                echo 'Building..'
                cleanWs()
                checkout changelog: false, poll: false, scm: [$class: 'GitSCM', branches: [[name: '*/develop']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'git@github-jsposato_test:Mobiquity/androidunittests.git']]]
                sh './gradlew assembleDebug'
            }
        }
        stage('Static Code Analysis') {
            steps {
                echo 'Scanning with SonarQube...'
                // requires SonarQube Scanner 2.8+
                //def scannerHome = tool 'Mob SonarQube Runner';

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
        stage('Test') {
            steps {
                echo 'Testing..'
                sh './gradlew testDebugUnitTest'

            }
        }
        stage('Deploy') {
            steps {
                echo 'Deploying....'
            }
        }
    }
}