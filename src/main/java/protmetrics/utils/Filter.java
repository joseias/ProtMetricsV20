package protmetrics.utils;

import java.io.*;

/**
 * Utility class to filter files by extension within a folder.
 */
public class Filter implements FileFilter {

    private final String pattern;

    /**
     * @param pattern search pattern.
     */
    public Filter(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public boolean accept(File file) {

        return file.getName().endsWith(pattern);
    }
}
