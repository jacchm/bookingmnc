package com.mnc.booking

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import spock.lang.Specification

class SimpleGroovyTest extends Specification {

    @Autowired
    private Environment environment;

    def "one plus one should equal two"() {
        expect:
        1 + 1 == 2
    }
}
