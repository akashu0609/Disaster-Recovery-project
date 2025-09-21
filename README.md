# Disaster-Recovery-project
built a CI/CD pipeline where code from GitHub is deployed to an S3 bucket with versioning enabled, fronted by CloudFront for global delivery. If a file accidentally deleted, can be restored instantly from version history without downtime. Setup also uses Route 53 for DNS and Jenkins for automation, ensuring both performance and disaster recovery.

<img width="341" height="538" alt="image" src="https://github.com/user-attachments/assets/048653a9-6ac5-4f5b-acef-97354f6bc5f6" />


# Architecture Overview Flow:

1. GitHub → Jenkins

* Developer pushes code to GitHub.

* Jenkins pipeline triggers automatically.

2. Jenkins → S3 (Versioning Enabled)

* Pipeline builds and deploys static site files to S3.

* Versioning ensures every change is stored, enabling rollback.

3. S3 → CloudFront → Route 53

* CloudFront caches and serves content globally.

* Route 53 maps your custom domain to CloudFront.

4. Disaster Recovery Loop

* If a file is deleted, S3’s version history allows instant restoration.

* No downtime for users because CloudFront continues serving cached content.



# Tech Stack

* AWS S3 (static hosting + versioning)

* AWS CloudFront (CDN)

* AWS Route 53 (DNS)

* Jenkins (CI/CD)

* GitHub (source control)

* Terraform (optional IaC)

* AWS CLI (automation scripts)


# Step-by-Step Implementation
1️⃣ Create S3 Bucket with Versioning

"aws s3api create-bucket \
  --bucket akash-portfolio-site \
  --region ap-south-1 \
  --create-bucket-configuration LocationConstraint=ap-south-1"

"aws s3api put-bucket-versioning \
  --bucket akash-portfolio-site \
  --versioning-configuration Status=Enabled"

* Enable static website hosting in the S3 console.


2️⃣ Create CloudFront Distribution

* Origin: S3 bucket endpoint.

* Default Behavior: Redirect HTTP → HTTPS.

* Cache Policy: Optimize for static assets.

* Optional: Add WAF for security.


3️⃣ Configure Route 53

* Create a hosted zone for your domain.

* Add an A record (Alias) pointing to the CloudFront distribution.


4️⃣ Jenkins CI/CD Pipeline

* Jenkinsfile.groovy


5️⃣ Disaster Recovery Simulation
* Delete a file from S3.
* Recover with below script:

aws s3api list-object-versions --bucket akash-portfolio-site
aws s3api delete-object \
  --bucket akash-portfolio-site \
  --key index.html \
  --version-id <DeleteMarkerVersionId>

* This removes the delete marker, restoring the file instantly.


6️⃣ Optional Enhancements

* Terraform to provision S3, CloudFront, Route 53.

* MFA Delete for extra protection.

* CloudWatch Alarms for unauthorized deletions.
