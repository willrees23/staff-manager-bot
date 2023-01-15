package dev.wand.util;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author xWand
 */
@UtilityClass
public class TextUtil {

    public static String[] cleanArray(String[] array) {
        return Arrays.stream(array).filter(StringUtils::isNotBlank).toArray(String[]::new);
    }

    public static TextEntry[] toTextEntries(String string, String separator) {
        String[] split = string.split(String.valueOf(separator));
        TextEntry[] entries = {};
        for (String s : split) {
            entries = append(entries, new TextEntry(s));
        }
        return entries;
    }

    public long getTime(String str) {
        // 1d 2h 3m 4s
        String[] split = str.split(" ");
        long time = 0;

        for (String s : split) {
            try {
                long duration = Long.parseLong(s.substring(0, s.length() - 1));
                if (duration < 0) continue;
                if (s.endsWith("d")) {
                    time += TimeUnit.DAYS.toMillis(duration);
                } else if (s.endsWith("h")) {
                    time += TimeUnit.HOURS.toMillis(duration);
                } else if (s.endsWith("m")) {
                    time += TimeUnit.MINUTES.toMillis(duration);
                } else if (s.endsWith("s")) {
                    time += TimeUnit.SECONDS.toMillis(duration);
                }
            } catch (NumberFormatException ignored) {
                return -1;
            }
        }

        return time;
    }

    public ArrayList<String> restrictWidth(String text, int width) {
        ArrayList<String> lines = new ArrayList<>();
        StringBuilder line = new StringBuilder();
        for (String word : text.split(" ")) {
            if (line.length() + word.length() + 1 > width) {
                lines.add(line.toString());
                line = new StringBuilder();
            }
            line.append(word).append(" ");
        }
        if (line.length() > 0) {
            lines.add(line.toString());
        }
        return lines;
    }

    public TextEntry[] append(TextEntry[] array, Object... objects) {
        TextEntry[] newArray = new TextEntry[array.length + objects.length];
        System.arraycopy(array, 0, newArray, 0, array.length);
        System.arraycopy(objects, 0, newArray, array.length, objects.length);
        return newArray;
    }

    /**
     * Formats a TextEntry array into a code block.
     * Max 3 entries in the TextEntry object.
     *
     * @param entries the TextEntry array
     * @return the formatted code block
     */
    public ArrayList<String> formatCodeBlock(TextEntry... entries) {
        ArrayList<String> lines = new ArrayList<>();
        int longestEntry1 = 0;
        int longestEntry2 = 0;
        int longestEntry3 = 0;
        for (TextEntry entry : entries) {
            if (entry.getStrings()[0].length() > longestEntry1) {
                longestEntry1 = entry.getStrings()[0].length();
            }
            if (entry.getStrings()[1].length() > longestEntry2) {
                longestEntry2 = entry.getStrings()[1].length();
            }
            if (entry.getStrings()[2].length() > longestEntry3) {
                longestEntry3 = entry.getStrings()[2].length();
            }
        }
        for (TextEntry entry : entries) {
            String s = String.format("%-" + longestEntry1 + "s | %-" + longestEntry2 + "s | %-" + longestEntry3 + "s", entry.getStrings()[0], entry.getStrings()[1], entry.getStrings()[2]);
            lines.add(s);
        }
        return lines;
    }

    public String formatCodeBlock(String... strings) {
        ArrayList<String> lines = new ArrayList<>();
        int longestEntry1 = 0;
        int longestEntry2 = 0;
        int longestEntry3 = 0;
        for (String string : strings) {
            String[] split = string.split(" ");
            if (split[0].length() > longestEntry1) {
                longestEntry1 = split[0].length();
            }
            if (split[1].length() > longestEntry2) {
                longestEntry2 = split[1].length();
            }
            if (split[2].length() > longestEntry3) {
                longestEntry3 = split[2].length();
            }
        }
        for (String string : strings) {
            String[] split = string.split(" ");
            String s = String.format("%-" + longestEntry1 + "s | %-" + longestEntry2 + "s | %-" + longestEntry3 + "s", split[0], split[1], split[2]);
            lines.add(s);
        }
        return lines.toString();
    }

    /**
     * Formats a TextEntry array into a code block.
     * The entries in the TextEntry objects must be the same length.
     *
     * @param entries
     * @return
     */
    public ArrayList<String> formatCodeBlockInfinite(TextEntry... entries) {
        for (TextEntry entry : entries) {
            if (entry.getStrings().length != entries[0].getStrings().length) {
                throw new IllegalArgumentException("The entries in the TextEntry objects must be the same length.");
            }
        }
        ArrayList<String> lines = new ArrayList<>();
        int[] longestEntries = new int[entries[0].getStrings().length];
        for (TextEntry entry : entries) {
            for (int i = 0; i < entry.getStrings().length; i++) {
                if (entry.getStrings()[i].length() > longestEntries[i]) {
                    longestEntries[i] = entry.getStrings()[i].length();
                }
            }
        }
        for (TextEntry entry : entries) {
            StringBuilder s = new StringBuilder();
            for (int i = 0; i < entry.getStrings().length; i++) {
                s.append(String.format("| %-" + (longestEntries[i] + 1) + "s", entry.getStrings()[i]));
            }
            lines.add(s.toString());
        }
        return lines;
    }

    public TimeUnit getTimeUnit(String str) {
        //get last char of str
        char c = str.charAt(str.length() - 1);

        switch (c) {
            case 's':
                return TimeUnit.SECONDS;
            case 'm':
                return TimeUnit.MINUTES;
            case 'h':
                return TimeUnit.HOURS;
            case 'd':
                return TimeUnit.DAYS;
            default:
                return null;
        }
    }

    public String randomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = (int) (chars.length() * Math.random());
            sb.append(chars.charAt(index));
        }
        return sb.toString();
    }

    public int getNumber(String str) {
        //get last char of str
        char c = str.charAt(str.length() - 1);

        if (Character.isDigit(c)) {
            return Integer.parseInt(str);
        }

        return Integer.parseInt(str.substring(0, str.length() - 1));
    }
}
