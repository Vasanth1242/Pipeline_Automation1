pipeline {

    agent any

    options {
        timeout(time: 30, unit: 'MINUTES')
        timestamps()
    }

    environment {
        ALLURE_RESULTS  = 'Allure/allure-results'
        ALLURE_REPORT   = 'allure-report'
        PDF_NAME        = 'Allure-Report.pdf'
        CUCUMBER_REPORT = 'target/CucumberReport.html'
    }

    stages {

        // ============================================================
        // 1. CLEAN PREVIOUS RESULTS
        // ============================================================
        stage('Clean') {
            steps {
                bat '''
                    if exist "%ALLURE_RESULTS%" rmdir /s /q "%ALLURE_RESULTS%"
                    if exist "%ALLURE_REPORT%" rmdir /s /q "%ALLURE_REPORT%"
                    if exist "%PDF_NAME%" del /f /q "%PDF_NAME%"
                '''
            }
        }


        // ============================================================
        // 2. BUILD & TEST
        // ============================================================
        stage('Build & Test') {
            steps {
                bat 'mvn clean test'
            }
        }


        // ============================================================
        // 3. VERIFY ALLURE RESULTS
        // ============================================================
        stage('Verify Results') {
            steps {
                bat '''
                    if not exist "%ALLURE_RESULTS%" (
                        echo ERROR: Allure results directory not found.
                        exit /b 1
                    )

                    dir /b "%ALLURE_RESULTS%\\*.json" >nul 2>&1

                    if errorlevel 1 (
                        echo ERROR: No Allure JSON result files found.
                        exit /b 1
                    )

                    echo Allure results verified.
                '''
            }
        }


        // ============================================================
        // 4. PUBLISH ALLURE REPORT IN JENKINS
        // ============================================================
        stage('Allure Report') {
            steps {
                allure(
                    includeProperties: false,
                    jdk: '',
                    results: [[path: "${ALLURE_RESULTS}"]]
                )
            }
        }


        // ============================================================
        // 5. VERIFY CUCUMBER REPORT
        // ============================================================
        stage('Verify Cucumber') {
            steps {
                bat '''
                    if not exist "%CUCUMBER_REPORT%" (
                        echo ERROR: Cucumber report not found.
                        exit /b 1
                    )

                    echo Cucumber report verified.
                '''
            }
        }


        // ============================================================
        // 6. GENERATE ALLURE PDF
        // ============================================================
       stage('Generate PDF') {
    steps {
        bat '''
            set "CHROME="

            if exist "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe" (
                set "CHROME=C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe"
            ) else if exist "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe" (
                set "CHROME=C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe"
            )

            if "%CHROME%"=="" (
                echo ERROR: Google Chrome not found.
                exit /b 1
            )

            if not exist "%ALLURE_REPORT%\\index.html" (
                echo ERROR: Allure report was not generated.
                exit /b 1
            )

            echo Allure report is ready.
            echo Waiting for report files to settle...

            timeout /t 3 /nobreak >nul

            if exist "%PDF_NAME%" (
                del /f /q "%PDF_NAME%"
            )

            echo Generating Allure PDF...

            "%CHROME%" ^
                --headless=new ^
                --no-sandbox ^
                --disable-gpu ^
                --disable-dev-shm-usage ^
                --allow-file-access-from-files ^
                --disable-web-security ^
                --disable-extensions ^
                --no-first-run ^
                --no-default-browser-check ^
                --virtual-time-budget=60000 ^
                --run-all-compositor-stages-before-draw ^
                --print-to-pdf="%WORKSPACE%\\%PDF_NAME%" ^
                "file:///%WORKSPACE%/%ALLURE_REPORT%/index.html"

            if errorlevel 1 (
                echo ERROR: Chrome PDF generation failed.
                exit /b 1
            )

            if not exist "%PDF_NAME%" (
                echo ERROR: Allure PDF was not generated.
                exit /b 1
            )

            echo Allure PDF generated successfully.
            dir "%PDF_NAME%"
        '''
    }
}
             
      


        // ============================================================
        // 7. FINAL VERIFICATION
        // ============================================================
        stage('Verify Reports') {
            steps {
                bat '''
                    if not exist "%PDF_NAME%" (
                        echo ERROR: Allure PDF not found.
                        exit /b 1
                    )

                    if not exist "%CUCUMBER_REPORT%" (
                        echo ERROR: Cucumber report not found.
                        exit /b 1
                    )

                    echo.
                    echo Reports generated successfully.
                    echo Allure PDF     : %PDF_NAME%
                    echo Cucumber Report : %CUCUMBER_REPORT%
                '''
            }
        }
    }


    // ================================================================
    // POST ACTIONS
    // ================================================================
    post {

        always {

            archiveArtifacts(
                artifacts: "${PDF_NAME},${CUCUMBER_REPORT}",
                allowEmptyArchive: true
            )

            emailext(
                to: 'vasanthvj.kiaq@gmail.com',
                subject: "[CI/CD] Pipeline1 - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                body: """
Hi Team,

The CI/CD pipeline execution has completed.

Build Number : #${env.BUILD_NUMBER}
Build Status : ${currentBuild.currentResult}

Reports:
- Allure PDF: ${PDF_NAME}
- Cucumber HTML: ${CUCUMBER_REPORT}

Regards,
Automation Team
""",
                attachmentsPattern: "${PDF_NAME},${CUCUMBER_REPORT}",
                attachLog: true
            )
        }

        success {
            echo 'Build and tests passed successfully.'
        }

        failure {
            echo 'Build or tests failed.'
        }

        unstable {
            echo 'Build is unstable.'
        }
    }
}
