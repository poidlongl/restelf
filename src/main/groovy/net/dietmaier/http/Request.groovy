package net.dietmaier.http

import groovy.json.JsonOutput
import groovy.transform.AutoClone

@AutoClone
class Request {
    String url
    String _path
    Map headers =[:]
    Map query =[:]
    Object body
    String method

    void path(String p ) {
        _path = p
    }

    static String appendPath(String base, String path ) {
        while ( base.endsWith('/')) {
            base = base[0..-2]
        }
        while ( path.startsWith('/')) {
            path = path[1..-1]
        }
        def tokens = [base]
        tokens.addAll(path.tokenize('/').collect{URLEncoder.encode(it, 'UTF-8')})
        tokens.join('/')
    }

    static String appendQuery(String uri, Map<String,String> query) {
        if ( ! query ) return uri
        uri + '?' + query.collect { key, val ->
            "$key=${URLEncoder.encode(val, 'UTF-8')}"
        }.join('&')
    }

    URI getUri() {
        URI.create(appendQuery(appendPath(url, _path), query as Map<String, String>))
    }

    String getBodyContent() {
        body ? body instanceof String ? body : JsonOutput.toJson(body): ''
    }
}
