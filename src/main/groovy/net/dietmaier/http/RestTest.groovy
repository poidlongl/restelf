package net.dietmaier.http

import groovy.util.logging.Log4j2

import java.net.http.HttpRequest

@Log4j2
class RestTest {
    static void main(String[] args) {
        def github = Github.instance

        def response = github.request {
            method =  'GET'
            path = 'users/poidlongl'
        }

        log.info response.value

        response = github.put {
            path = 'repos/poidlongl/restelf/topics'
            body = [
                names: [ 'topic1', 'topic2' ]
            ]
        }
        println response.request().headers().map()
        println response.request().uri()
        println response.request().method()
        println response.requestHelper.bodyContent

    }
}
