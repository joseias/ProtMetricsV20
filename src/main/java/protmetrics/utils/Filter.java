package protmetrics.utils;

import java.io.*;

/**
 * Utility class to filter files by extension within a folder.
 */
public class Filter implements FileFilter {

    private final String c_pattern;

    /**
     *
     * @param a_pattern
     */
    public Filter(String a_pattern) {
        c_pattern = a_pattern;
    }

    @Override
    public boolean accept(File a_file) {

        return a_file.getName().endsWith(c_pattern);
    }
}
