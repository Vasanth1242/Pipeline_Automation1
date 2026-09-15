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
        // 1. BUILD AND TEST
        // ============================================================
        stage('Build & Test') {
            steps {
                bat '''
                    echo ========================================
                    echo BUILD AND TEST
                    echo ========================================

                    mvn clean test

                    if errorlevel 1 (
                        echo.
                        echo ERROR: Maven test execution failed
                        exit /b 1
                    )

                    echo.
                    echo Maven test execution completed.
                '''
            }
        }


        // ============================================================
        // 2. CHECK ALLURE RESULTS
        // ============================================================
        stage('Check Allure Results') {
            steps {
                bat '''
                    echo ========================================
                    echo CHECKING ALLURE RESULTS
                    echo ========================================

                    if not exist "%ALLURE_RESULTS%" (
                        echo ERROR: Allure results directory does not exist
                        echo Expected:
                        echo %WORKSPACE%\\%ALLURE_RESULTS%
                        exit /b 1
                    )

                    echo Allure results directory found:
                    echo %WORKSPACE%\\%ALLURE_RESULTS%
                    echo.

                    echo Allure result files:
                    dir "%ALLURE_RESULTS%" /s /b

                    echo.
                    echo Checking JSON result files...

                    dir /b "%ALLURE_RESULTS%\\*.json" > "%TEMP%\\allure_files.txt" 2>nul

                    if not exist "%TEMP%\\allure_files.txt" (
                        echo ERROR: No Allure JSON files found
                        echo Allure report cannot be generated without result files.
                        exit /b 1
                    )

                    for /f %%A in ('find /c /v "" ^< "%TEMP%\\allure_files.txt"') do (
                        echo Allure JSON file count: %%A
                    )

                    del "%TEMP%\\allure_files.txt" 2>nul

                    echo.
                    echo Allure results are available.
                '''
            }
        }


        // ============================================================
        // 3. GENERATE ALLURE REPORT
        // ============================================================
        stage('Allure Report') {
            steps {
                echo 'Generating Allure HTML report...'

                allure(
                    includeProperties: false,
                    jdk: '',
                    results: [[path: "${ALLURE_RESULTS}"]]
                )

                echo 'Allure Jenkins report step completed.'
            }
        }


        // ============================================================
        // 4. CHECK ALLURE REPORT
        // ============================================================
        stage('Check Allure Report') {
            steps {
                bat '''
                    echo ========================================
                    echo CHECKING ALLURE REPORT
                    echo ========================================

                    if not exist "%ALLURE_REPORT%" (
                        echo ERROR: Allure report directory does not exist
                        exit /b 1
                    )

                    if not exist "%ALLURE_REPORT%\\index.html" (
                        echo ERROR: Allure index.html does not exist
                        exit /b 1
                    )

                    echo.
                    echo Allure report found successfully.
                    echo Report:
                    echo %WORKSPACE%\\%ALLURE_REPORT%\\index.html
                    echo.

                    echo Allure report files:
                    dir "%ALLURE_REPORT%" /s /b
                '''
            }
        }


        // ============================================================
        // 5. CHECK CUCUMBER REPORT
        // ============================================================
        stage('Check Cucumber Report') {
            steps {
                bat '''
                    echo ========================================
                    echo CHECKING CUCUMBER REPORT
                    echo ========================================

                    if not exist "%CUCUMBER_REPORT%" (
                        echo ERROR: Cucumber HTML report was not found.
                        echo Expected:
                        echo %WORKSPACE%\\%CUCUMBER_REPORT%
                        exit /b 1
                    )

                    echo.
                    echo Cucumber report found successfully.
                    echo %WORKSPACE%\\%CUCUMBER_REPORT%
                    echo.

                    dir "%CUCUMBER_REPORT%"
                '''
            }
        }


        // ============================================================
        // 6. GENERATE ALLURE PDF
        // ============================================================
        stage('Generate PDF') {
            steps {
                bat '''
                    echo ========================================
                    echo GENERATING ALLURE PDF
                    echo ========================================

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


                    // ------------------------------------------------
                    // Generate a fresh Allure report
                    // ------------------------------------------------
                    echo Generating fresh Allure HTML report...

                    if exist "%ALLURE_REPORT%" (
                        echo Removing old Allure report...
                        rmdir /s /q "%ALLURE_REPORT%"
                    )

                    allure generate "%ALLURE_RESULTS%" ^
                        -o "%ALLURE_REPORT%" ^
                        --clean

                    if errorlevel 1 (
                        echo ERROR: Allure report generation failed
                        exit /b 1
                    )

                    if not exist "%ALLURE_REPORT%\\index.html" (
                        echo ERROR: Allure index.html was not generated
                        exit /b 1
                    )

                    echo.
                    echo Allure HTML report generated successfully.
                    echo.


                    // ------------------------------------------------
                    // Remove old PDF
                    // ------------------------------------------------
                    if exist "%WORKSPACE%\\%PDF_NAME%" (
                        echo Removing old PDF...
                        del /f /q "%WORKSPACE%\\%PDF_NAME%"
                    )


                    // ------------------------------------------------
                    // Generate PDF using Chrome
                    // ------------------------------------------------
                    echo Starting Chrome...
                    echo.

                    "%CHROME%" ^
                        --headless=new ^
                        --no-sandbox ^
                        --disable-gpu ^
                        --disable-dev-shm-usage ^
                        --allow-file-access-from-files ^
                        --disable-web-security ^
                        --virtual-time-budget=30000 ^
                        --run-all-compositor-stages-before-draw ^
                        --print-to-pdf="%WORKSPACE%\\%PDF_NAME%" ^
                        "file:///%WORKSPACE%/%ALLURE_REPORT%/index.html"

                    if errorlevel 1 (
                        echo ERROR: Chrome PDF generation command failed
                        exit /b 1
                    )


                    // ------------------------------------------------
                    // Verify PDF
                    // ------------------------------------------------
                    echo.
                    echo Checking generated PDF...

                    if not exist "%WORKSPACE%\\%PDF_NAME%" (
                        echo ERROR: Allure PDF was not generated
                        exit /b 1
                    )

                    echo.
                    echo ========================================
                    echo PDF GENERATED SUCCESSFULLY
                    echo ========================================

                    dir "%WORKSPACE%\\%PDF_NAME%"
                '''
            }
        }


        // ============================================================
        // 7. FINAL REPORT CHECK
        // ============================================================
        stage('Final Report Check') {
            steps {
                bat '''
                    echo ========================================
                    echo FINAL REPORT CHECK
                    echo ========================================

                    echo.
                    echo Allure PDF:
                    if exist "%WORKSPACE%\\%PDF_NAME%" (
                        echo FOUND - %WORKSPACE%\\%PDF_NAME%
                        dir "%WORKSPACE%\\%PDF_NAME%"
                    ) else (
                        echo NOT FOUND
                    )

                    echo.
                    echo Cucumber HTML:
                    if exist "%WORKSPACE%\\%CUCUMBER_REPORT%" (
                        echo FOUND - %WORKSPACE%\\%CUCUMBER_REPORT%
                        dir "%WORKSPACE%\\%CUCUMBER_REPORT%"
                    ) else (
                        echo NOT FOUND
                    )

                    echo.
                    echo ========================================
                    echo REPORT CHECK COMPLETED
                    echo ========================================
                '''
            }
        }
    }


    // ================================================================
    // POST ACTIONS
    // ================================================================
    post {

        always {

            echo 'CI/CD execution completed'


            // --------------------------------------------------------
            // Archive Allure PDF + Cucumber HTML
            // --------------------------------------------------------
            archiveArtifacts(
                artifacts: "${PDF_NAME},${CUCUMBER_REPORT}",
                allowEmptyArchive: true
            )


            // --------------------------------------------------------
            // Send Email
            // --------------------------------------------------------
            emailext(
                to: 'vasanthvj.kiaq@gmail.com',

                subject: "[CI/CD] Pipeline1 - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",

                body: """
Hi Team,

The CI/CD pipeline execution has completed.

========================================
BUILD DETAILS
========================================

Project        : Pipeline1
Build Number   : #${env.BUILD_NUMBER}
Build Status   : ${currentBuild.currentResult}

========================================
TEST REPORTS
========================================

Allure Report:
Allure-Report.pdf

Cucumber Report:
CucumberReport.html

The Allure PDF contains the detailed Allure test execution results.

The Cucumber HTML report contains the detailed Cucumber execution results.

The Jenkins build log is also attached for reference.

========================================
REPORT LOCATIONS
========================================

Allure PDF:
${PDF_NAME}

Cucumber HTML:
${CUCUMBER_REPORT}

========================================

Regards,

Automation Team
""",

                attachmentsPattern: "${PDF_NAME},${CUCUMBER_REPORT}",

                attachLog: true
            )
        }


        // ------------------------------------------------------------
        // SUCCESS
        // ------------------------------------------------------------
        success {
            echo '========================================'
            echo 'BUILD AND TESTS PASSED SUCCESSFULLY'
            echo '========================================'
        }


        // ------------------------------------------------------------
        // FAILURE
        // ------------------------------------------------------------
        failure {
            echo '========================================'
            echo 'BUILD OR TESTS FAILED'
            echo '========================================'
        }


        // ------------------------------------------------------------
        // UNSTABLE
        // ------------------------------------------------------------
        unstable {
            echo '========================================'
            echo 'BUILD IS UNSTABLE'
            echo '========================================'
        }
    }
}
