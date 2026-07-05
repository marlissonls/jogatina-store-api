package br.com.jogatinastore.shared.utils;

import java.text.Normalizer;

public class SlugUtils {

    public static String toSlug(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);

        return normalized
            .replaceAll("\\p{M}", "")          // remove accents
            .toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")   // remove punctuation/symbols
            .replaceAll("\\s+", "-")           // spaces -> hyphen
            .replaceAll("-{2,}", "-")          // multiple hyphen
            .replaceAll("^-|-$", "");          // remove hyphen from the ends
    }
}