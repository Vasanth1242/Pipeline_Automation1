pipeline {
    agent any

    stages {

        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }

        stage('Allure Report') {
            steps {
                allure([
                    results: [[path: 'Allure/allure-results']]
                ])
            }
        }

        stage('Generate Allure PDF') {
            steps {
                bat '''
                    echo Generating Allure HTML report...

                    if exist "Allure\\allure-report" rmdir /s /q "Allure\\allure-report"

                    allure generate "Allure\\allure-results" -o "Allure\\allure-report" --clean

                    echo Starting local web server...

                    start /B python -m http.server 8000 --directory "Allure\\allure-report"

                    timeout /t 5 /nobreak

                    echo Generating PDF...

                    if exist "Allure-Report.pdf" del /f /q "Allure-Report.pdf"

                    set "EDGE=C:\\Program Files (x86)\\Microsoft\\Edge\\Application\\msedge.exe"

                    if not exist "%EDGE%" set "EDGE=C:\\Program Files\\Microsoft\\Edge\\Application\\msedge.exe"

                    "%EDGE%" --headless --disable-gpu --no-sandbox --print-to-pdf="%WORKSPACE%\\Allure-Report.pdf" http://localhost:8000/index.html

                    timeout /t 5 /nobreak

                    echo Allure PDF generated.

                    taskkill /IM msedge.exe /F >nul 2>&1
                    taskkill /IM python.exe /F >nul 2>&1
                '''
            }
        }
    }

    post {

        always {
            echo 'CI/CD execution completed'

            emailext(
                to: 'YOUR_GMAIL_ADDRESS@gmail.com',
                subject: "[CI/CD] Pipeline1 - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                body: """
Hi Team,

The CI/CD pipeline execution has completed.

Project : Pipeline1
Build Number : #${env.BUILD_NUMBER}
Build Status : ${currentBuild.currentResult}

Test Execution : Completed
Allure Report : PDF attached
Build Log : Attached

Please find the Allure test report PDF attached for detailed test results.

Regards,
Automation Team
                """,
                attachmentsPattern: 'Allure-Report.pdf',
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