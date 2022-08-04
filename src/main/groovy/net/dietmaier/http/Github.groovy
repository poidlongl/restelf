package net.dietmaier.http

import groovy.util.logging.Log4j2

@Log4j2
class Github extends Service {
    static Github getInstance() {
        def instance = new Github()
        instance.sslCheck = false
        def token = new File("${System.properties.'user.home'}/.config/local/github/token").text.trim()
        instance.defaultRequest.headers.Authorization = "token $token"
        instance.defaultRequest.headers.Accept = 'application/vnd.github+json'
        instance.defaultRequest.url = 'https://api.github.com'
        instance
    }
}
