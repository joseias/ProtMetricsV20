package protmetrics.utils;

import java.io.*;
import java.util.*;

public class PdbFilter implements FileFilter
{

  private String c_pattern;
  public PdbFilter(String a_pattern)
  {
      c_pattern=a_pattern;
  }
 public boolean accept(File a_file,String a_fileName)
 {

    return a_fileName.endsWith(c_pattern);
 }
 public boolean accept(File a_file)
 {

    return a_file.getName().endsWith(c_pattern);
 }

}