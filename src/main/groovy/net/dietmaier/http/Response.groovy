package net.dietmaier.http

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

import java.net.http.HttpResponse

class Response {
    @Delegate HttpResponse response
    @Lazy Object value = parsed()

    private def parsed() {
        new JsonSlurper().parse(response.body() as InputStream)
    }
}