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

    stage('Generate Allure Report to PDF') {
        steps {
            bat '''
                echo ================================
                echo GENERATING ALLURE PDF
                echo ================================

                if not exist "%ALLURE_REPORT%" (
                    echo ERROR: Allure report directory not found
                    exit /b 1
                )

                if not exist "%ALLURE_REPORT%\\complete.html" (
                    echo ERROR: complete.html not found
                    dir "%ALLURE_REPORT%"
                    exit /b 1
                )

                echo Allure report found.
                echo Generating PDF...

                if exist "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" (
                    "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" ^
                        --headless=new ^
                        --no-sandbox ^
                        --disable-gpu ^
                        --disable-dev-shm-usage ^
                        --hide-scrollbars ^
                        --virtual-time-budget=20000 ^
                        --run-all-compositor-stages-before-draw ^
                        --no-pdf-header-footer ^
                        --print-to-pdf="%WORKSPACE%\\%PDF_NAME%" ^
                        "file:///%WORKSPACE%/%ALLURE_REPORT%/complete.html"
                ) else if exist "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" (
                    "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" ^
                        --headless=new ^
                        --no-sandbox ^
                        --disable-gpu ^
                        --disable-dev-shm-usage ^
                        --hide-scrollbars ^
                        --virtual-time-budget=20000 ^
                        --run-all-compositor-stages-before-draw ^
                        --no-pdf-header-footer ^
                        --print-to-pdf="%WORKSPACE%\\%PDF_NAME%" ^
                        "file:///%WORKSPACE%/%ALLURE_REPORT%/complete.html"
                ) else (
                    echo ERROR: Google Chrome was not found
                    exit /b 1
                )

                if not exist "%WORKSPACE%\\%PDF_NAME%" (
                    echo ERROR: PDF was not generated
                    exit /b 1
                )

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
