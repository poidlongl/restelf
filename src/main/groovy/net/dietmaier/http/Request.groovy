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

    private static String encodedQuery(String k, v ) {
        if ( v == null) {
            return k
        }
        "$k=${URLEncoder.encode(v.toString(), 'UTF-8')}"
    }
    static String appendQuery(String uri, Map<String,Object> query) {
        if ( ! query ) return uri
        uri + '?' + query.collect { key, val ->

            switch ( val ) {
                case Collection:
                    return val.collect {encodedQuery(key, it )}
                default:
                    return encodedQuery(key, val )
            }
        }.flatten().join('&')
    }

    URI getUri() {
        URI.create(appendQuery(appendPath(url, _path), query as Map<String, String>))
    }

    String getBodyContent() {
        body ? body instanceof String ? body : JsonOutput.toJson(body): ''
    }
}
