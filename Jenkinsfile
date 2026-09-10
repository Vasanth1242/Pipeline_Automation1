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
		}
		
		success {
			echo 'Build and tests passed successfully'
		}
		failure {
			echo 'Build or tests failed'
		}
	}
}