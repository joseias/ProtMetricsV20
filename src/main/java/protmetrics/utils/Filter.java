package protmetrics.utils;

import java.io.*;

public class Filter implements FileFilter {

    private final String c_pattern;

    public Filter(String a_pattern) {
        c_pattern = a_pattern;
    }

    public boolean accept(File a_file, String a_fileName) {

        return a_fileName.endsWith(c_pattern);
    }

    public boolean accept(File a_file) {

        return a_file.getName().endsWith(c_pattern);
    }

}
