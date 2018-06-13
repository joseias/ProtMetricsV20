package protmetrics.utils;

import java.io.*;

public class Filter implements FileFilter {

    private final String c_pattern;

    public Filter(String a_pattern) {
        c_pattern = a_pattern;
    }

    @Override
    public boolean accept(File a_file) {

        return a_file.getName().endsWith(c_pattern);
    }

}
