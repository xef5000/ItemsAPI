package com.github.xef5000.itemsapi.api.utils;

import org.bukkit.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorHelper {

    // Pattern to match hex codes like &#RRGGBB
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([A-Fa-f0-9]{6})");

    // Pattern to match gradient formats like <gradient:#RRGGBB:#RRGGBB>text</gradient>
    private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient:#([A-Fa-f0-9]{6}):#([A-Fa-f0-9]{6})>(.*?)</gradient>");

    public static String[] translate(String[] text) {
        for (int i = 0; i < text.length; i++) {
            text[i] = translate(text[i]);
        }
        return text;
    }

    public static java.util.List<String> translate(java.util.List<String> text) {
        text.replaceAll(ColorHelper::translate);
        return text;
    }

    /**
     * Translates a string containing legacy color codes ('&'), hex codes ('&#RRGGBB'),
     * and gradient codes ('<gradient:#start:#end>text</gradient>').
     *
     * @param text The string to translate.
     * @return The translated string with all color codes applied.
     */
    public static String translate(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        // The order of translation is important:
        // 1. Gradients, as they are the most complex.
        // 2. Single Hex Codes.
        // 3. Legacy '&' codes, which is handled by Bukkit's ChatColor.
        String withGradients = translateGradients(text);
        String withHex = translateHexCodes(withGradients);

        return ChatColor.translateAlternateColorCodes('&', withHex);
    }

    /**
     * Translates gradient color codes in the format <gradient:#RRGGBB:#RRGGBB>text</gradient>.
     *
     * @param text The text to process.
     * @return The text with gradients applied.
     */
    private static String translateGradients(String text) {
        Matcher matcher = GRADIENT_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            // Extract the colors and the content
            String startHex = matcher.group(1);
            String endHex = matcher.group(2);
            String content = matcher.group(3);

            // Create Color objects from hex codes
            Color startColor = Color.decode("#" + startHex);
            Color endColor = Color.decode("#" + endHex);

            // Generate the colored string
            String gradedContent = applyGradient(content, startColor, endColor);

            // Replace the original gradient tag with the processed content
            matcher.appendReplacement(builder, gradedContent);
        }
        matcher.appendTail(builder);
        return builder.toString();
    }

    /**
     * Applies a color gradient to a string of text.
     *
     * @param text       The text to apply the gradient to.
     * @param startColor The starting color of the gradient.
     * @param endColor   The ending color of the gradient.
     * @return The color-graded string.
     */
    private static String applyGradient(String text, Color startColor, Color endColor) {
        StringBuilder graded = new StringBuilder();
        int length = text.length();

        // Handle edge case for single-character text
        if (length <= 1) {
            return net.md_5.bungee.api.ChatColor.of(startColor) + text;
        }

        for (int i = 0; i < length; i++) {
            // Calculate the interpolation factor (0.0 to 1.0)
            float t = (float) i / (length - 1);

            // Linearly interpolate the RGB components
            int r = (int) (startColor.getRed() + t * (endColor.getRed() - startColor.getRed()));
            int g = (int) (startColor.getGreen() + t * (endColor.getGreen() - startColor.getGreen()));
            int b = (int) (startColor.getBlue() + t * (endColor.getBlue() - startColor.getBlue()));

            // Create the interpolated color
            Color interpolatedColor = new Color(r, g, b);

            // Append the color and the character to the string
            graded.append(net.md_5.bungee.api.ChatColor.of(interpolatedColor));
            graded.append(text.charAt(i));
        }
        return graded.toString();
    }
    /**
     * Translates hex color codes in the format &#RRGGBB.
     *
     * @param text The text to process.
     * @return The text with hex codes applied.
     */
    private static String translateHexCodes(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder builder = new StringBuilder();

        while (matcher.find()) {
            // Extract the hex code (e.g., "FFAA00")
            String hexCode = matcher.group(1);

            // Use BungeeCord's ChatColor.of() to create the color object
            // and append its string representation (e.g., §x§F§F§A§A§0§0)
            matcher.appendReplacement(builder, net.md_5.bungee.api.ChatColor.of("#" + hexCode).toString());
        }
        matcher.appendTail(builder);
        return builder.toString();
    }
}
