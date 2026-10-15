package org.example.reassembly;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReassemblyServiceTest {

    private final ReassemblyService service = new ReassemblyService();

    @Test
    void reassemblesOverlappingFragments() {
        String input =
                "This ;s is an exam;xample probl;blem.";

        assertEquals(
                "This is an example problem.",
                service.reassemble(input)
        );
    }

    @Test
    void removesDuplicateFragments() {
        assertEquals(
                "hello world",
                service.reassemble("hello ;world;hello ;world")
        );
    }

    @Test
    void handlesFragmentsWithNoOverlap() {
        assertEquals(
                "abcdef",
                service.reassemble("abc;def")
        );
    }

    @Test
    void handlesOneFragment() {
        assertEquals(
                "hello world",
                service.reassemble("hello world")
        );
    }
}
