///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaApplication.java,v 1.4 2003/03/06 21:53:37 krake Exp $
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
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// external packages

// local packages
import org.dinopolis.util.metadata.DMDFileStore;
import org.dinopolis.util.metadata.DMDHandler;
import org.dinopolis.util.metadata.DMDMapper;
import org.dinopolis.util.metadata.DMDServiceProvider;
import org.dinopolis.util.metadata.DMDServiceQuery;
import org.dinopolis.util.metadata.DMDStore;
import org.dinopolis.util.metadata.DMDTrader;
import org.dinopolis.util.metadata.dinomeda.DinomedaTrader;
import org.dinopolis.util.metadata.dinomeda.DinomedaConfigurator;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

abstract public class DinomedaApplication
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaApplication(String[] args)
  {
    config_ = new Properties();
  
    trader_ = new DinomedaTrader();
    
    mime_detector_ = new MIME();
    
    parseArguments(args);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] args()
  {
    return program_arguments_;
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void loadConfig() throws IOException
  {
    if (config_file_ == null)
    {
      return;
    }

    FileInputStream file_in = null;
    file_in = new FileInputStream(config_file_);
    config_.load(file_in);
    File file = new File(config_file_);
    String path = file.getParent();
    config_.setProperty("dinomeda.configpath", path + System.getProperty("file.separator"));
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void configureTrader() throws IOException
  {
    String config_path = config_.getProperty("dinomeda.configpath");
    System.err.println("cp:" + config_path);
    String trader_config = config_.getProperty("dinomeda.traderconfig");

    DinomedaConfigurator configurator = new DinomedaConfigurator();
    configurator.readConfig(config_path+"/"+trader_config);
    configurator.configure(trader_);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getSupportedMIMETypes()
  {
    return trader_.getStoreMIMETypes();
  }  

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getNameMappingsForFile(File file)
  {
    if (file == null)
    {
      return new String[0];
    }
    
    String mime_type = MIME.getMIMETypeForFile(file);
    
    if (mime_type == null)
    {
      return new String[0];
    }
    
    return trader_.getNameMappingsForMIME(mime_type);
  }  
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDMapper getMappedFileStore(File file, String name_mapping)
  {
    if (file == null)
    {
      return null;
    }
    System.err.println("Checking MIME type");
    String mime_type = mime_detector_.getMIMETypeForFile(file);
    if (mime_type == null)
    {
      System.err.println("Unknown format");
      return null;
    }
    System.err.println("MIME type: " + mime_type);
    
    DMDServiceQuery query = new DMDServiceQuery();
    query.setIOMethod("File");
    query.setNameMapping(name_mapping);
    query.setMIMEType(mime_type);
    
    System.err.println("Looking up provider for " + query);
    DMDServiceProvider provider = trader_.findService(query);
    if (provider == null)
    {
      System.err.println("No provider available");
      return null;
    }
    
    System.err.println("Retrieving store");
    DMDStore store = provider.getStore(query);
    if (store == null)
    {
      System.err.println("No store available");
      return null;
    }
    
    if (!(store instanceof DMDFileStore))
    {
      System.err.println("Store is not a DMDFileStore");
      return null;
    }
    
    DMDFileStore file_store = (DMDFileStore) store;
    
    System.err.println("Retrieving mapper");
    DMDMapper mapper = provider.getMapper(query);
    if (mapper == null)
    {
      System.err.println("No mapper available");
      return null;
    }
    
    file_store.setFile(file);
    mapper.setStore(file_store);
    
    return mapper;
  }
     
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public abstract void run();
      
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void parseArguments(String[] args)
  {
    String config_file = null;

    int pos = -1;
    for (int count = 0; count < args.length; ++count)
    {
      if (args[count].equals("--cfg"))
      {
        if (count < (args.length - 1))
        {
          config_file = args[count+1];
        }

        if (config_file == null)
        {
          System.err.println("Option cfg missing configfile");
        }

        pos = count;
        break;
      }
    }

    if (pos > -1)
    {
      int after_file = pos+1;
      int length = args.length - 1;
      if (config_file != null)
      {
        length--;
        after_file++;
      }

      program_arguments_ = new String[length];

      System.arraycopy(args, 0, program_arguments_, 0, pos);

      System.arraycopy(args, after_file, program_arguments_, pos, (length-pos));
    }
    else
    {
      program_arguments_ = args;
    }

    config_file_ = config_file;
  }
    
  protected String[] program_arguments_;
  
  protected String config_file_;
  protected Properties config_;
  protected DMDTrader trader_;
  protected MIME mime_detector_;
}
