#!/usr/bin/env groovy
pipeline {

    agent none

    options {
        buildDiscarder(logRotator(numToKeepStr: '1'))
        disableConcurrentBuilds()
    }

    environment {
        MAVEN_OPTS = "-Djansi.force=true -Dmaven.test.failure.ignore"
    }

    stages {

        stage('Build') {

            agent {
                docker {
                    label 'docker'
                    image 'glx-artifactory-developer-docker.repository.deere.com/deere/maven'
                    args "--volume /var/lib/jenkins/.m2/repository:/home/node/.m2/repository:rw --volume /var/lib/jenkins:/var/lib/jenkins:rw --volume /tmp:/tmp:rw"
                    reuseNode true
                }
            }

            steps {
                ansiColor('gnome-terminal') {
                    checkout scm

                    withCredentials([usernamePassword(credentialsId: 'artifactory-maven', passwordVariable: 'artifactory_password', usernameVariable: 'artifactory_user')]) {
                        sh "mvn -B -V -e -s settings.xml -Dartifactory.user=$artifactory_user -Dartifactory.password=$artifactory_password clean install deploy"
                    }
                }
            }
        }
    }
}
