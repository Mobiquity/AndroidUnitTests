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
        stage('Test') {
            steps {
                echo 'Testing..'
                cleanWs()
                sh './gradlew testDebugUnitTest'

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