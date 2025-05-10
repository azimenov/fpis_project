#!/bin/bash
# Wait for LocalStack to be fully up
echo "Waiting for LocalStack to start..."
until aws --endpoint-url=http://localhost:4566 s3 ls > /dev/null 2>&1; do
  sleep 1
done

# Create bucket
echo "Creating S3 bucket..."
aws --endpoint-url=http://localhost:4566 s3 mb s3://my-bucket

# Configure CORS
echo "Configuring S3 bucket CORS..."
aws --endpoint-url=http://localhost:4566 s3api put-bucket-cors --bucket my-bucket --cors-configuration '{
  "CORSRules": [
    {
      "AllowedOrigins": ["*"],
      "AllowedHeaders": ["*"],
      "AllowedMethods": ["GET", "PUT", "POST", "DELETE", "HEAD", "OPTIONS"],
      "MaxAgeSeconds": 3000,
      "ExposeHeaders": ["ETag"]
    }
  ]
}'

echo "S3 bucket setup complete"