///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaConsoleApplication.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.util.Date;
import java.util.Properties;

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDMapper;
import org.dinopolis.utils.metadata.dinomeda.DinomedaMapper;

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
    System.out.println("Trader knows support for " + types.length
                       + " MIME types:");
    for (int count = 0; count < types.length; ++count)
    {
      System.out.println(types[count]);
    }
    System.out.println("----");
  } 
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void processFile(File file)
  {
    System.out.println("\n------------------\nProcessing file " + file);
     
  
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
      dinomeda_mapper.read("/");
    }
    catch (IOException exception)
    {
      exception.printStackTrace(System.err);
      return;
    }
    
    try
    {
      String[] creator = dinomeda_mapper.getCreator();
      for (int count = 0; count < creator.length; ++count)
      {
        System.out.println("Creator " + count + ": " + creator[count]);
      }

      String[] title = dinomeda_mapper.getTitle();
      for (int count = 0; count < title.length; ++count)
      {
        System.out.println("Title " + count + ": " + title[count]);
      }

      Date[] date = dinomeda_mapper.getDate();
      for (int count = 0; count < date.length; ++count)
      {
        System.out.println("Date " + count + ": " + date[count]);
      }

      String[] type = dinomeda_mapper.getType();
      for (int count = 0; count < type.length; ++count)
      {
        System.out.println("Type " + count + ": " + type[count]);
      }

      System.out.println(dinomeda_mapper.getMIMEType());

    }
    catch (ArrayIndexOutOfBoundsException e)
    {
        e.printStackTrace(System.err);
    } 
    
  }
}
