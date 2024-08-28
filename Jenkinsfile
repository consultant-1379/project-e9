// Have jenkins (of course) and SonarQube running locally. In jenkins download the sonarqube plugin and set the name
// of the SonarQube server in the general settings to "SonarQube".

pipeline{
    agent any
    stages{
        stage("Build"){
            steps{
                bat "mvn package"
                bat "docker-compose build" // build but don't run (as run will run infinitely)
            }
        }
        stage("Unit tests"){
            steps{
                echo "Run java unit tests here"
            }
        }
        stage("Integration tests"){
            steps{
                echo "Run python integration tests here"
                // bat "docker-compose up"
                // This doesnt have to be pytho but i think itll be easier
            }
        }
        stage("SonarQube"){
            steps{
                withSonarQubeEnv("SonarQube"){
                    bat "mvn clean package sonar:sonar"
                }
            }
        }
//         stage("Quality Gate"){
//             steps{
//                 timeout(time: 1, unit: 'HOURS'){
//                     def qg = waitForQualityGate()
//                     if (dq.status != 'OK'){
//                         error "Pipeline aborted due to quality gate failure: ${qg.status}"
//                     }
//                 }
//             }
//         }
        // This should mark the end of Pre-code review steps
        stage("Package code"){
            steps{
                echo "Package the code to be released. Provide download option in archive (war file? or maybe image)"
            }
        }
    }
    post{
        always {
            bat "docker-compose down" // clean up docker containers
        }
    }
}