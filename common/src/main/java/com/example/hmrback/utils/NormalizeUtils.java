package com.example.hmrback.utils;

import jakarta.validation.constraints.NotNull;

import java.text.Normalizer;

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
}
