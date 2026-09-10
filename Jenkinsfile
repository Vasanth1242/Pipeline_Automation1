pipeline {
agent any

environment {
    ALLURE_RESULTS = 'Allure/allure-results'
    ALLURE_REPORT = 'allure-report'
    PDF_NAME = 'Allure-Report.pdf'
}

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
            echo 'Generating Allure report...'

            allure(
                includeProperties: false,
                jdk: '',
                results: [[path: "${ALLURE_RESULTS}"]]
            )
        }
    }

    stage('Check Allure Report') {
        steps {
            bat '''
                echo ================================
                echo CHECKING ALLURE REPORT
                echo ================================

                if not exist "%ALLURE_REPORT%" (
                    echo ERROR: Allure report directory does not exist
                    exit /b 1
                )

                echo Allure report directory found.
                echo.

                dir "%ALLURE_REPORT%" /s /b
            '''
        }
    }

    stage('Generate PDF') {
        steps {
            bat '''
                echo ================================
                echo GENERATING PDF
                echo ================================

                set "CHROME="

                if exist "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" (
                    set "CHROME=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
                )

                if exist "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" (
                    set "CHROME=C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
                )

                if "%CHROME%"=="" (
                    echo ERROR: Google Chrome was not found
                    exit /b 1
                )

                echo Chrome found:
                echo %CHROME%
                echo.

                if exist "%ALLURE_REPORT%\\index.html" (
                    set "REPORT_FILE=index.html"
                ) else if exist "%ALLURE_REPORT%\\complete.html" (
                    set "REPORT_FILE=complete.html"
                ) else (
                    echo ERROR: No Allure HTML entry file found
                    exit /b 1
                )

                echo Report file:
                echo %REPORT_FILE%
                echo.

                "%CHROME%" ^
                    --headless=new ^
                    --no-sandbox ^
                    --disable-gpu ^
                    --disable-dev-shm-usage ^
                    --print-to-pdf="%WORKSPACE%\\%PDF_NAME%" ^
                    "file:///%WORKSPACE%/%ALLURE_REPORT%/%REPORT_FILE%"

                if not exist "%WORKSPACE%\\%PDF_NAME%" (
                    echo ERROR: PDF was not generated
                    exit /b 1
                )

                echo.
                echo ================================
                echo PDF GENERATED SUCCESSFULLY
                echo ================================

                dir "%WORKSPACE%\\%PDF_NAME%"
            '''
        }
    }
}

post {

    always {
        echo 'CI/CD execution completed'

        archiveArtifacts(
            artifacts: "${PDF_NAME}",
            allowEmptyArchive: true
        )

        emailext(
            to: 'vasanthvj.kiaq@gmail.com',
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
attachmentsPattern: "${PDF_NAME}",
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