# hello-minio

![](minio.png)

- https://min.io/

## Requirements

- install minio on minio.minio:31183
- https://github.com/ndy2/helm-infrastructures/tree/main/helm-minio#helm-install

## structure

```
├── README.md
├── build.gradle.kts
└── src
    └── main
        ├── kotlin
        │   ├── MinioMain.kt                  - MinIO Example with MinioClient
        │   ├── S3Main.kt                     - MinIO Example with S3Client
        │   └── config
        │       ├── Certificates.kt           - ssl configuration
        │       └── MinioConfiguration.kt     - MinIO host/port/accessKey/secretKey
        └── resources
            └── ca.crt                              - certificate for minio api
```
