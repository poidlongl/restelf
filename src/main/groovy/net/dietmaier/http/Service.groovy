package net.dietmaier.http


import groovy.util.logging.Log4j2

import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.security.SecureRandom
import java.security.cert.X509Certificate

@Log4j2
class Service {
    Request defaultRequest = {
        def helper = new Request()
//        helper.headers.'Content-Type' = "application/json"
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
        def response
        do {
            def builder = HttpRequest.newBuilder(helper.uri)
            if ( helper.body ) {
                builder.method(helper.method, HttpRequest.BodyPublishers.ofString(helper.bodyContent))
            } else {
                builder.method(helper.method,  HttpRequest.BodyPublishers.noBody())
            }
            helper.headers.each {  k,  v ->
                builder.header(k as String, v as String)
            }
            response = new Response( response: httpClient.send(builder.build(), HttpResponse.BodyHandlers.ofInputStream()), requestHelper: helper )
            if ( response.valid) return response
        } while ( --(helper.retries) > 0 )
        log.warn "$helper.uri ${response.statusCode()} ${response.contentType ? '/'+response.value  : ''}"
        return response
    }

    def get(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'GET'
        request(desc, helper)
    }
    def put(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'PUT'
        request(desc, helper)
    }
    def post(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'POST'
        request(desc, helper)
    }
    def delete(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'DELETE'
        request(desc, helper)
    }
    def patch(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'PATCH'
        request(desc, helper)
    }
    def head(@DelegatesTo(Request) Closure desc ) {
        def helper = defaultRequest.clone()
        helper.method = 'HEAD'
        request(desc, helper)
    }
}
