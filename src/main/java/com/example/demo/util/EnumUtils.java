package com.example.demo.util;

public class EnumUtils {
    public static <T extends Enum<T>> T getEnumFromString(Class<T> enumClass, String value) {
        if (enumClass == null) {
            throw new IllegalArgumentException("Enum class must be specified.");
        }

        for (Enum<?> enumValue : enumClass.getEnumConstants()) {
            if (enumValue.toString().equalsIgnoreCase(value)) {
                return (T) enumValue;
            }
        }

        throw new IllegalArgumentException("No constant with value " + value + " found in " + enumClass.getName());
    }

    public static String toReadableString(Enum<?> enumValue) {
        String status = enumValue.name().toLowerCase().replace("_", " ");
        StringBuilder sb = new StringBuilder(status.length());
        boolean capitalizeNext = true;
        for (char c : status.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            } else if (capitalizeNext) {
                c = Character.toTitleCase(c);
                capitalizeNext = false;
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
