package hello.haha

import hello.haha.config.MinioConfiguration.accessKey
import hello.haha.config.MinioConfiguration.host
import hello.haha.config.MinioConfiguration.port
import hello.haha.config.MinioConfiguration.secretKey
import hello.haha.config.Certificates
import io.minio.MinioClient
import okhttp3.OkHttpClient


fun main() {
    val minio = MinioClient.builder()
        .credentials(accessKey, secretKey)
        .endpoint("https://$host", port, true)
        .httpClient(getTlsHttpClient()) // or use minio.ignoreCertCheck()
        .build()

    val buckets = minio.listBuckets()
    println("버킷 목록:")
    for (bucket in buckets) {
        println(bucket.name())
    }
}


private fun getTlsHttpClient(): OkHttpClient {
    val trustManager = Certificates.getTrustManager()
    val sslContext = Certificates.getSSlContext(trustManager)

    return OkHttpClient.Builder()
        .sslSocketFactory(sslContext.socketFactory, trustManager)
        .build()
}
