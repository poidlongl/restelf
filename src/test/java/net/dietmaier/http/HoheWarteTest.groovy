package net.dietmaier.http

import groovy.json.JsonOutput
import spock.lang.Specification

class HoheWarteTest extends Specification {
    def hoheWarte
    void setup() {
        hoheWarte = HoheWarte.instance
    }

    def "GetMetadata"() {
        when:
            println (JsonOutput.prettyPrint(JsonOutput.toJson(hoheWarte.metadata)))
        then:
            true
    }

    def "GetCurrent"() {
        when: println ( hoheWarte.current )
        then: true 
    }
}
