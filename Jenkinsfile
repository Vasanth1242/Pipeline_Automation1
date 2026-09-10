pipeline {
	agent any
	
	stages {
		stage('Build & Test'){
			steps {
				bat 'mvn clean test'
			//	bat 'mvn clean test -Dsurefire.suiteXmlFiles=testng.xml'
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
	stage('prepare Allure Report'){
		steps{
			bat '''
			if exist Allure-Report.zip del /f /q Allure-Report.zip
			powershell -command "Compress-Archive -path 'Allure\\allure-results\\* -Destinationpath 'Allure-Report.zip' -Force"
			'''
		}
	}
	
	
	post {
		always {
			echo 'CI/CD execution completed'
			
			emailext(
				to: 'vasanthvj.kiaq@gmail.com',
				subject: "[CI/CD] Pipeline1 - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
				body: """
				
				Hi Team,
				
				The Ci/CD pipeline execution has completed.
				
				Project : Pipeline1
				Build Number: #${env.BUILD_NUMBER}
				Build Status: ${currentBuild.currentResult}
				
				Test Execution: completed
				
				Allure Report : Attached
				Build Log: Attached
				
				Please check the attached files for detailed execution information.
				
				Regards,
				WebAutomation Test
									"""
				
				attachmentsPattern: 'Allure-report.zip,build',
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