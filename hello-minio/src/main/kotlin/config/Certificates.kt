package hello.haha.config

import java.io.IOException
import java.io.InputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

object Certificates {

    fun getSSlContext(trustManager: X509TrustManager): SSLContext {
        // Set up SSL context with the trust manager
        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(null, arrayOf(trustManager), null)
        return sslContext
    }

    fun getTrustManager(): X509TrustManager {
        // Load your certificate from resources/private.crt
        val certificate = loadCertificate()

        // Create a trust manager that trusts the certificate
        val trustManager = trustManagerForCertificate(certificate)

        return trustManager
    }

    private fun loadCertificate(): X509Certificate {
        return try {
            val inputStream: InputStream? = Certificates::class.java.getResourceAsStream("/ca.crt")
            val certificateFactory = CertificateFactory.getInstance("X.509")
            certificateFactory.generateCertificate(inputStream) as X509Certificate
        } catch (e: IOException) {
            throw RuntimeException("Failed to load certificate", e)
        }
    }

    private fun trustManagerForCertificate(certificate: X509Certificate): X509TrustManager {
        return try {
            // Create a KeyStore containing our trusted certificate
            val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
            keyStore.load(null, null)
            keyStore.setCertificateEntry("custom", certificate)

            // Create a TrustManager that trusts the certificate in our KeyStore
            val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
            trustManagerFactory.init(keyStore)
            val trustManagers = trustManagerFactory.trustManagers
            if (trustManagers.size != 1 || trustManagers[0] !is X509TrustManager) {
                throw IllegalStateException("Unexpected default trust managers:$trustManagers")
            }
            trustManagers[0] as X509TrustManager
        } catch (e: Exception) {
            throw RuntimeException("Failed to create trust manager", e)
        }
    }
}
