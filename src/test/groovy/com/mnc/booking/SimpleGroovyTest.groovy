package com.mnc.booking

import spock.lang.Specification

class SimpleGroovyTest extends Specification {

    def "one plus one should equal two"() {
        expect:
        1 + 1 == 2
    }
}
