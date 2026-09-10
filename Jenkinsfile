pipeline {
	agent any
	
	stages {
		stage('Build & Test'){
			steps {
				bat 'mvn clean test'
			//	bat 'mvn clean test -Dsurefire.suiteXmlFiles=testng.xml'
			// run
			}
		}
		stage('Allure Report'){
			steps{
				allure([
					results: [[path: 'Allure/allure-results']]
				])
			}
		}
	}
	
	post {
		always {
			echo 'Test execution completed'
			
			emailext(
				to: 'vasanthvj.kiaq@gmail.com',
				subject: "Jenkins Build ${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
				body: "CI/CD execution completed. status: ${currentBuild.currentResult}",
				attachLog: true
			)
		}
		
		success {
			echo 'Build and tests passed successfully'
		}
		failure {
			echo 'Build or tests failed'
		}
	}
}