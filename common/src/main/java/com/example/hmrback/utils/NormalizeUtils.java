package com.example.hmrback.utils;

import jakarta.validation.constraints.NotNull;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class NormalizeUtils {

    private NormalizeUtils() {
    }

    /**
     * Normalizes a text by:
     * 1) Trimming leading and trailing whitespace
     * 2) Decomposing Unicode characters and removing diacritics
     * 3) Converting to lower-case
     * 4) Removing all non-alphanumeric characters (including spaces)
     *
     * @param value The input text to normalize
     * @return The normalized text
     */
    public static String normalizeText(
        @NotNull
        String value) {
        // 1) trim
        String s = value.trim();
        // 2) decompose and remove diacritics
        s = Normalizer.normalize(s, Normalizer.Form.NFD).replaceAll("\\p{M}", "");
        // 3) lower-case
        s = s.toLowerCase();
        // 4) remove non-alphanumeric characters (including spaces)
        s = s.replaceAll("[^a-z0-9]+", "");
        return s;
    }

    /**
     * Normalizes a comma-separated list in a string by:
     * 1) Splitting the string by commas
     * 2) Trimming leading and trailing whitespace from each item
     * 3) Converting each item to lower-case
     * 4) Collecting the items into a Set to remove duplicates
     *
     * @param rawListInString The raw comma-separated list in a string
     * @return A Set of normalized items
     */
    public static Set<String> normalizeListInString(String rawListInString) {
        return Arrays.stream(rawListInString.split(","))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(String::toLowerCase)
            .collect(Collectors.toSet());
    }
}
