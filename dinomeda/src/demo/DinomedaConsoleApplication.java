///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaConsoleApplication.java,v 1.4 2003/04/24 09:08:49 osma Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package demo;

// Java imports
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

// external packages

// local packages
import org.dinopolis.util.metadata.DMDMapper;
import org.dinopolis.util.metadata.dinomeda.DinomedaMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaConsoleApplication extends DinomedaApplication
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaConsoleApplication(String[] args)
  {
    super(args);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void run()
  {
    try
    {
      loadConfig();
      configureTrader();
    }
    catch (IOException io)
    {
      io.printStackTrace(System.err);
    }  

    parseArguments(args());

    printMIMETypes();

    File file = null;
    String[] args = args();
    for (int count = 0; count < args.length; ++count)
    {
      file = new File(args[count]);
      if (file.exists() && !file.isDirectory())
      {
        processFile(file);
      }
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void printMIMETypes()
  {
    String[] types = getSupportedMIMETypes();
    System.err.println("Trader knows support for " + types.length
                       + " MIME types:");
    for (int count = 0; count < types.length; ++count)
    {
      System.err.println(types[count]);
    }
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void processFile(File file)
  {
    DMDMapper mapper = getMappedFileStore(file, "Dinomeda");
    if (mapper == null)
    {
      System.err.println("No mapped file store for "
                        + file);
      return;
    }

    if (!(mapper instanceof DinomedaMapper))
    {
      System.err.println("Mapper is not a Dinomeda Mapper");
      return;
    }

    DinomedaMapper dinomeda_mapper = (DinomedaMapper) mapper;

    try
    {
      dinomeda_mapper.readMetaData("/");
    }
    catch (IOException exception)
    {
      exception.printStackTrace(System.err);
      return;
    }

    if (metagrep_search_ == null)
    {
      extractMetaData(file, dinomeda_mapper);
    }
    else
    {
      metagrep(file, dinomeda_mapper);
    }
  }


  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void extractMetaData(File file, DinomedaMapper dinomeda_mapper)
  {

    System.out.println(file + ":");
    for (int fcount = 0; fcount < DinomedaMapper.FIELD_COUNT; ++fcount)
    {
      Object[] values = dinomeda_mapper.get(fcount);
      for (int vcount = 0; vcount < values.length; ++vcount)
      {
        System.out.print(DinomedaMapper.FIELD_NAMES[fcount]);
        System.out.print(vcount);
        System.out.print(": ");
        System.out.println(values[vcount]);
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void metagrep(File file, DinomedaMapper dinomeda_mapper)
  {
    for (int fcount = 0; fcount < DinomedaMapper.FIELD_COUNT; ++fcount)
    {
      Object[] values = dinomeda_mapper.get(fcount);
      for (int vcount = 0; vcount < values.length; ++vcount)
      {
        String value;
        if (values[vcount] instanceof Date)
        {
          if (date_formatter_ == null)
          {
            date_formatter_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
          }
          value = date_formatter_.format((Date)values[vcount]);
        }
        else
        {
          value = values[vcount].toString();
        }
        if (value.indexOf(metagrep_search_) != -1)
        {
          System.out.print(file);
          System.out.print(":(");
          System.out.print(DinomedaMapper.FIELD_NAMES[fcount]);
          System.out.print(vcount);
          System.out.print("):");
          System.out.println(value);
        }
      }
    }
  }

  private void parseArguments(String[] args)
  {
    String search_value = null;

    int pos = -1;
    for (int count = 0; count < args.length; ++count)
    {
      if (args[count].equals("--metagrep"))
      {
        if (count < (args.length - 1))
        {
          search_value = args[count+1];
        }

        if (search_value == null)
        {
          System.err.println("Option metagrep missing searchstring");
        }

        pos = count;
        break;
      }
    }

    if (pos > -1)
    {
      int after_value = pos+1;
      int length = args.length - 1;
      if (search_value != null)
      {
        length--;
        after_value++;
      }

      program_arguments_ = new String[length];

      System.arraycopy(args, 0, program_arguments_, 0, pos);

      System.arraycopy(args, after_value, program_arguments_, pos, (length-pos));
    }
    else
    {
      program_arguments_ = args;
    }

    metagrep_search_ = search_value;
  }

  String metagrep_search_ = null;
  SimpleDateFormat date_formatter_ = null;
}
