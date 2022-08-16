package net.dietmaier.http

import groovy.util.logging.Log4j2

import java.net.http.HttpRequest

@Log4j2
class RestTest {
    static void main(String[] args) {
        def github = Github.instance

        github.get  { path = 'users/poidlongl'
        }.success   { println "Success: $it"
        }.fail      { it.raiseException()
        }

        github.put {
            path = 'repos/poidlongl/restelf/topics'
            body = [
                names: [ 'topic1', 'topic2' ]
            ]
        }.success {
            println "Success: $it"
        }.fail {
            it.raiseException()
        }
    }
}
