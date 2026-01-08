package unics;

import java.util.UUID;

public final class PublicIdGenerator {

    // Alphabet lisible (32 chars)
    private static final String ALPHABET = "ABCDEFGHJKLMNPQRSTUVWXYZ23456789";
    private static final int BASE = ALPHABET.length();
    private static final int LENGTH = 6;

    private PublicIdGenerator() {
        // utilitaire
    }

    public static String fromUuid(UUID uuid) {
        long value = uuid.getMostSignificantBits() ^ uuid.getLeastSignificantBits();
        value = Math.abs(value);

        StringBuilder sb = new StringBuilder();

        while (value > 0 && sb.length() < LENGTH) {
            int index = (int) (value % BASE);
            sb.append(ALPHABET.charAt(index));
            value /= BASE;
        }

        while (sb.length() < LENGTH) {
            sb.append(ALPHABET.charAt(0));
        }

        return sb.reverse().toString();
    }
}