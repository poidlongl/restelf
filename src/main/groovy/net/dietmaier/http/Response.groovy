package net.dietmaier.http

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper
import groovy.util.logging.Log4j2
import groovy.xml.XmlSlurper

import java.net.http.HttpResponse

@Log4j2
class Response {
    @Delegate HttpResponse response
    Request requestHelper
    @Lazy Object value = parsed()
    @Lazy String contentType = {
        response.headers().allValues('Content-Type').join(';')
    }()
    @Lazy long contentLenth = {
        response.headers().firstValue('Content-Length'
        ).map ( String::toLong
        ).orElse( 0l)
    }() as long

    private def parsed() {
        switch (contentType) {
            case ~/.*json.*/ : return new JsonSlurper().parse(response.body() as InputStream)
            case ~/.*xml.*/  : return new XmlSlurper().parse(response.body() as InputStream)
            default          : return (response.body() as InputStream).text
        }
    }

    boolean isValid() {
        return (200..399).contains(response.statusCode())
    }
}