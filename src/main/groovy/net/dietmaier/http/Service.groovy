package net.dietmaier.http

import groovy.json.JsonOutput

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset
import java.security.SecureRandom
import java.security.cert.X509Certificate

class Service {
    Request defaultRequest = {
        def helper = new Request()
        helper.headers.'Content-Type' = "application/json"
        helper
    }()

    boolean sslCheck = true

    @Lazy HttpClient httpClient = {
        def client = HttpClient.newBuilder()
        if ( ! sslCheck ) {
            def sslContext = SSLContext.getInstance('TLS')
            sslContext.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        X509Certificate[] getAcceptedIssuers() {
                            return null
                        }

                        void checkClientTrusted(
                                X509Certificate[] certs, String authType) {
                        }

                        void checkServerTrusted(
                                X509Certificate[] certs, String authType) {
                        }
                    }
            }, new SecureRandom())
            client.sslContext(sslContext)
        }
        client.build()
    }()

    def request(@DelegatesTo(Request) Closure desc, helper = defaultRequest.clone()) {
        helper.with desc
        def builder = HttpRequest.newBuilder(helper.uri)
        if ( helper.body ) {
            builder.method(helper.method, HttpRequest.BodyPublishers.ofString(helper.bodyContent))
        } else {
            builder.method(helper.method,  HttpRequest.BodyPublishers.noBody())
        }
        helper.headers.each {  k,  v ->
            builder.header(k as String, v as String)
        }
        def response = httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream())
        new Response(response: response)
    }

    def get(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'GET'
        request(desc, helper)
    }
}
