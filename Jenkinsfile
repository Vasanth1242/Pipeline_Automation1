environment {
    ALLURE_RESULTS = 'Allure/allure-results'
    ALLURE_REPORT  = 'allure-report'
    PDF_NAME       = 'Allure-Report.pdf'
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
