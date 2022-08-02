package net.dietmaier.http

import groovy.util.logging.Log4j2

import java.net.http.HttpRequest

@Log4j2
class RestTest {
    static void main(String[] args) {
        def github = Github.instance

//        def response = github.get('users/poidlongl')
//        println(response.text)

        def response = github.request {
            method =  'GET'
            path 'users/poidlongl'
        }

        log.info response.value

        response = github.get {
            path 'users/poidlongl'
        }
        log.info response.value.login

    }
}
