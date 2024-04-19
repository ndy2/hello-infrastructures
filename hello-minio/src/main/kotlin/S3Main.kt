package hello.haha

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import hello.haha.config.MinioConfiguration.accessKey
import hello.haha.config.MinioConfiguration.host
import hello.haha.config.MinioConfiguration.port
import hello.haha.config.MinioConfiguration.secretKey
import hello.haha.config.Certificates
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.SSLConnectionSocketFactory

fun main() {
    val credentials = BasicAWSCredentials(accessKey, secretKey)
    val endpoint = "https://${host}:${port}"
    val region = "us-east-1"

    val minio = AmazonS3ClientBuilder.standard()
        .withCredentials(AWSStaticCredentialsProvider(credentials))
        .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
        .withClientConfiguration(getTlsClientConfig())
        .build()

    val buckets = minio.listBuckets()
    println("버킷 목록:")
    for (bucket in buckets) {
        println(bucket.name)
    }
}

private fun getTlsClientConfig(): ClientConfiguration {
    val clientConfig = ClientConfiguration()

    // load sslContext, trustManager
    val trustManager = Certificates.getTrustManager()
    val sslContext = Certificates.getSSlContext(trustManager)

    clientConfig.apacheHttpClientConfig.sslSocketFactory = SSLConnectionSocketFactory(
        sslContext,
        arrayOf("TLSv1.2", "TLSv1.1"),
        null,
        NoopHostnameVerifier.INSTANCE
    )
    return clientConfig
}
