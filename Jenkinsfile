pipeline {
    agent any

    environment {
        ALLURE_RESULTS = 'Allure/allure-results'
        ALLURE_REPORT  = 'allure-report'
        PDF_NAME       = 'Allure-Report.pdf'
    }

    stages {

        stage('Build & Test') {
            steps {
                sh '''
                    echo "================================"
                    echo "BUILD AND TEST"
                    echo "================================"

                    mvn clean test
                '''
            }
        }

        stage('Allure Report') {
            steps {
                echo 'Publishing Allure report...'

                allure([
                    results: [[path: "${ALLURE_RESULTS}"]]
                ])
            }
        }

        stage('Generate Allure Report to PDF') {
            steps {
                sh '''
                    set -e

                    echo "================================"
                    echo "GENERATING ALLURE PDF"
                    echo "================================"

                    python3 -m venv .venv
                    . .venv/bin/activate

                    pip install --quiet --upgrade pip
                    pip install --quiet allure-combine

                    allure-combine \
                        "$ALLURE_REPORT" \
                        --dest "$ALLURE_REPORT"

                    google-chrome \
                        --headless=new \
                        --no-sandbox \
                        --disable-gpu \
                        --disable-dev-shm-usage \
                        --hide-scrollbars \
                        --virtual-time-budget=20000 \
                        --run-all-compositor-stages-before-draw \
                        --no-pdf-header-footer \
                        --print-to-pdf="$WORKSPACE/$PDF_NAME" \
                        "file://$WORKSPACE/$ALLURE_REPORT/complete.html"

                    ls -lh "$WORKSPACE/$PDF_NAME"
                '''
            }
        }
    }

    post {

        always {
            echo 'Publishing Allure results...'

            allure(
                includeProperties: false,
                jdk: '',
                results: [[path: "${ALLURE_RESULTS}"]]
            )

            archiveArtifacts(
                artifacts: "${PDF_NAME}",
                allowEmptyArchive: true
            )

            echo 'CI/CD execution completed'

            emailext(
                to: 'vasanthvj.kiaq@gmail.com',
                subject: "[CI/CD] Pipeline1 - Build #${env.BUILD_NUMBER} - ${currentBuild.currentResult}",
                body: """
Hi Team,

The CI/CD pipeline execution has completed.

Project        : Pipeline1
Build Number   : #${env.BUILD_NUMBER}
Build Status   : ${currentBuild.currentResult}

Test Execution : Completed
Allure Report  : PDF attached
Build Log      : Attached

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
