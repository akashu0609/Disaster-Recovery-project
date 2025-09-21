pipeline {
    agent any
    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/your-repo.git'
            }
        }
        stage('Build') {
            steps {
                sh 'npm install && npm run build' // if using a JS framework
            }
        }
        stage('Deploy to S3') {
            steps {
                withAWS(region: 'ap-south-1', credentials: 'aws-creds') {
                    sh 'aws s3 sync ./dist s3://akash-portfolio-site --delete'
                }
            }
        }
        stage('Invalidate CloudFront Cache') {
            steps {
                withAWS(region: 'ap-south-1', credentials: 'aws-creds') {
                    sh 'aws cloudfront create-invalidation --distribution-id YOUR_DIST_ID --paths "/*"'
                }
            }
        }
    }
}
