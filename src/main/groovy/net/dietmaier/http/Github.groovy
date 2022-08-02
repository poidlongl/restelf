package net.dietmaier.http

import groovy.json.JsonOutput

import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.Charset

class Github extends Service {
    static Github getInstance() {
        def instance = new Github()
        instance.sslCheck = false
        def token = new File("${System.properties.'user.home'}/.config/local/github/token").text.trim()
        instance.defaultRequest.headers.Authentication = "token $token"
        instance.defaultRequest.url = 'https://api.github.com'
        instance
    }
}
