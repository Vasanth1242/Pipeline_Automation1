pipeline {
    agent any

    stages {

        stage('Build & Test') {
            steps {
                bat '''
                    echo ================================
                    echo BUILD AND TEST
                    echo ================================

                    mvn clean test
                '''
            }
        }

        stage('Allure Report') {
            steps {
                echo 'Publishing Allure report...'

                allure([
                    results: [[path: 'Allure/allure-results']]
                ])
            }
        }

        stage('Generate Allure HTML') {
            steps {
                bat '''
                    echo ================================
                    echo GENERATING ALLURE HTML REPORT
                    echo ================================

                    if exist "Allure\\allure-report" (
                        rmdir /s /q "Allure\\allure-report"
                    )

                    allure generate "Allure\\allure-results" ^
                        -o "Allure\\allure-report" ^
                        --clean

                    if not exist "Allure\\allure-report\\index.html" (
                        echo ERROR: Allure HTML report was not generated.
                        exit /b 1
                    )

                    echo Allure HTML report generated successfully.

                    dir "Allure\\allure-report"
                '''
            }
        }

        stage('Generate Allure PDF') {
            steps {
                bat '''
                    echo ================================
                    echo STARTING JAVA HTTP SERVER
                    echo ================================

                    cd Allure

                    start "" /B java -cp "%JAVA_HOME%\\lib\\tools.jar" com.sun.net.httpserver.SimpleFileServer 8000

                    cd ..

                    timeout /t 5 /nobreak >nul

                    echo ================================
                    echo CHECKING GOOGLE CHROME
                    echo ================================

                    set "CHROME=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"

                    if not exist "%CHROME%" (
                        set "CHROME=C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
                    )

                    if not exist "%CHROME%" (
                        echo ERROR: Google Chrome was not found.
                        exit /b 1
                    )

                    echo Chrome found:
                    echo %CHROME%

                    echo ================================
                    echo GENERATING PDF
                    echo ================================

                    if exist "Allure-Report.pdf" (
                        del /f /q "Allure-Report.pdf"
                    )

                    "%CHROME%" ^
                        --headless=new ^
                        --disable-gpu ^
                        --no-sandbox ^
                        --disable-dev-shm-usage ^
                        --no-first-run ^
                        --no-default-browser-check ^
                        --print-to-pdf="%WORKSPACE%\\Allure-Report.pdf" ^
                        "http://127.0.0.1:8000/allure-report/index.html"

                    timeout /t 5 /nobreak >nul

                    if not exist "%WORKSPACE%\\Allure-Report.pdf" (
                        echo ERROR: Allure PDF was not generated.
                        exit /b 1
                    )

                    echo ================================
                    echo PDF GENERATED SUCCESSFULLY
                    echo ================================

                    dir "%WORKSPACE%\\Allure-Report.pdf"

                    taskkill /IM chrome.exe /F >nul 2>&1
                '''
            }
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

The CI/CD pipeline execution has completed.

Project       : Pipeline1
Build Number  : #${env.BUILD_NUMBER}
Build Status  : ${currentBuild.currentResult}

Test Execution : Completed
Allure Report  : PDF attached
Build Log      : Attached

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
