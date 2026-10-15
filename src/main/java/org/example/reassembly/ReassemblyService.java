package org.example.reassembly;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

@Service
public class ReassemblyService {

    private static final Logger log =
            LoggerFactory.getLogger(ReassemblyService.class);

    public String reassemble(String input) {
        List<String> fragments = new ArrayList<>(
                new LinkedHashSet<>(List.of(input.split(";", -1)))
        );

        fragments.removeIf(String::isBlank);

        log.debug("Processing {} unique fragments", fragments.size());

        removeContainedFragments(fragments);

        if (fragments.isEmpty()) {
            log.debug("No fragments to reassemble");
            return "";
        }

        while (fragments.size() > 1) {
            int bestI = 0, bestJ = 1, maxOverlap = -1;

            for (int i = 0; i < fragments.size(); i++) {
                for (int j = 0; j < fragments.size(); j++) {
                    if (i == j) continue;

                    int overlap = overlap(fragments.get(i), fragments.get(j));

                    if (overlap > maxOverlap) {
                        maxOverlap = overlap;
                        bestI = i;
                        bestJ = j;
                    }
                }
            }

            log.debug(
                    "Merging fragments: overlap={}, remaining={}",
                    maxOverlap, fragments.size()
            );

            String merged = merge(
                    fragments.get(bestI),
                    fragments.get(bestJ),
                    maxOverlap
            );

            // Keep the merged fragment in its original position.
            fragments.set(bestI, merged);
            fragments.remove(bestJ);

            removeContainedFragments(fragments);
        }

        String result = fragments.getFirst();
        log.debug("Reassembly finished");
        return result;
    }

    private int overlap(String a, String b) {
        int maxLength = Math.min(a.length(), b.length());

        for (int length = maxLength; length > 0; length--) {
            if (a.regionMatches(
                    a.length() - length,
                    b,
                    0,
                    length
            )) {
                return length;
            }
        }

        return 0;
    }

    private String merge(String a, String b, int overlap) {
        return a + b.substring(overlap);
    }

    private void removeContainedFragments(List<String> fragments) {
        fragments.removeIf(fragment ->
                fragments.stream().anyMatch(other ->
                        !Objects.equals(other, fragment)
                                && other.contains(fragment)
                )
        );
    }
}