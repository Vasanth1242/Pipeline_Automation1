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