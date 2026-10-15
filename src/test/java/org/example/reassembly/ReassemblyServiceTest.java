package org.example.reassembly;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ReassemblyServiceTest {

    private final ReassemblyService service = new ReassemblyService();

    @ParameterizedTest(name = "[{index}] {0} -> {1}")
    @MethodSource("reassemblyCases")
    void reassemblesFragments(String input, String expected) {
        assertEquals(expected, service.reassemble(input));
    }

    static Stream<Arguments> reassemblyCases() {
        return Stream.of(
                // Basic reconstruction
                Arguments.of(
                        "This ;s is an exam;xample problem.",
                        "This is an example problem."
                ),

                // Duplicate fragments
                Arguments.of(
                        "hello ;world;hello ;world",
                        "hello world"
                ),

                // Exact duplicate of the entire document
                Arguments.of(
                        "hello world;hello world",
                        "hello world"
                ),

                // Maximum overlap
                Arguments.of(
                        "abcXYZ;XYZdef;defghi",
                        "abcXYZdefghi"
                ),

                // Fragment contained in another fragment
                Arguments.of(
                        "hello world;world",
                        "hello world"
                ),

                // Fragment contained after merging
                Arguments.of(
                        "hello wor;world",
                        "hello world"
                ),

                // Single fragment
                Arguments.of(
                        "hello world",
                        "hello world"
                ),

                // Empty input
                Arguments.of(
                        "",
                        ""
                ),

                // Empty fragments between separators
                Arguments.of(
                        ";hello;;world;",
                        "helloworld"
                ),

                // Whitespace-only fragments are ignored
                Arguments.of(
                        "   ;hello;   ",
                        "hello"
                ),

                // No overlap: preserves input order when all overlaps tie
                Arguments.of(
                        "abc;def",
                        "abcdef"
                ),

                // Partial overlap at the end of fragments
                Arguments.of(
                        "running;ing fast;fast now",
                        "running fast now"
                ),

                // Repeated characters
                Arguments.of(
                        "aaaa;aaab;abcd",
                        "aaaabcd"
                ),

                //input order with no overlap
                Arguments.of(
                        "cat;dog;fish",
                        "catdogfish"
                )
        );
    }

    @Test
    void mergesFragmentsWithDifferentOverlapLengths() {
        assertEquals(
                "prefix-middle-suffix",
                service.reassemble(
                        "prefix-middle;middle-suffix;prefix-middle"
                )
        );
    }
}