package edu.harvard.hul.fbt;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

public class LogWriter {

  private Map<String, List<String>> mAggregatedLogs;

  private List<String> mGlobalLogs;

  public LogWriter() {
    mAggregatedLogs = new HashMap<String, List<String>>();
    mGlobalLogs = new ArrayList<String>();
  }

  public void submitLog( String file, String log ) {
    List<String> list = mAggregatedLogs.get( log );

    if (list == null) {
      list = new ArrayList<String>();
      list.add( file );
      mAggregatedLogs.put( log, list );
    } else {
      list.add( file );
    }
  }

  public void submitGlobalLog( String log ) {
    mGlobalLogs.add( log );
  }

  public void flush( String key ) {
    // TODO pick up file and write.
    StringBuffer buffer = new StringBuffer();
    newLine( buffer, "###" );
    newLine( buffer, "Comparing Base with " + key + " at " + new Date() );
    newLine( buffer, "");
    
    if (mGlobalLogs.size() > 0) {
      newLine( buffer, "Global findings" );
      for (String l : mGlobalLogs) {
        newLine( buffer, l );
      }
    } else {
      newLine( buffer, "No global findings. Please look at the details" );
    }
    
    newLine( buffer, "" );

    for (String k : mAggregatedLogs.keySet()) {
      List<String> list = mAggregatedLogs.get( k );
      newLine( buffer, k );
      for (String l : list) {
        newLine( buffer, "\t\t" + l );
      }
    }

    newLine( buffer, "" );
    //System.out.println( buffer.toString() );
    write(buffer);
    mAggregatedLogs.clear();
  }
  
  private void write(StringBuffer buffer) {
    File logFile = new File(System.getProperty( "java.io.tmpdir" ) + File.separator + "/bbt-logs/log.txt");
    System.out.println("Writing logs to: " + logFile.getAbsolutePath());
    try {
      FileUtils.writeStringToFile(logFile, buffer.toString(), true);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void newLine( StringBuffer buffer, String line ) {
    buffer.append( line + "\n" );
  }
}
