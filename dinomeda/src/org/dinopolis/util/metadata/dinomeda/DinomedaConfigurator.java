///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaConfigurator.java,v 1.3 2003/03/06 21:53:37 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU Lesser General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.util.metadata.dinomeda;

// Java imports
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

// local imports
import org.dinopolis.util.metadata.DMDHandler;
import org.dinopolis.util.metadata.DMDServiceQuery;
import org.dinopolis.util.metadata.DMDTrader;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.3
 */
 
/**
 * The DinomedaConfigurator can configure DMDTraders using data read
 * from a java.util.Properties style config file.
 *
 * The configuration file looks like this:
 * <pre>
 * providers=1
 *
 * provider0.class=ord.dinopolis.utils.metadata.dinomeda.DinomedaProvider
 * provider0.stores=2
 * provder0.mappers=1
 *
 * provider0.store0.name=MP3FileStore
 * provider0.store0.mime=audio/x-mp3
 * provider0.store0.iomethod=File
 * provider0.store0.iomodes=rwu
 *
 * provider0.store1.name=MP3StreamStore
 * provider0.store1.mime=audio/x-mp3
 * provider0.store1.iomethod=Stream
 * provider0.store1.iomodes=r
 *
 * provider0.mapper0.name=MP3Mapper
 * provider0.mapper0.mime=audio/x-mp3
 * provider0.mapper0.mapping=Dinomeda
 * </pre>
 */
public class DinomedaConfigurator
{
  //---------------------------------------------------------------
  /**
   * Creates a configurator with an empty configuration
   */
  public DinomedaConfigurator()
  {
    config_ = null;
  }

  //---------------------------------------------------------------
  /**
   * Tries to read a trader configuration from a file.
   * 
   * @param filename the filename of the trader configuration file
   */
  public void readConfig(String filename) throws IOException
  {
    FileInputStream file_stream = new FileInputStream(filename);
    
    config_ = new Properties();
    config_.load(file_stream);
  }
  
  //---------------------------------------------------------------
  /**
   * Configures a trader using the previously read config.
   * If some parts of the config are rejected, the configurator skips them.
   * 
   * @param trader the trader to configure
   */
  public void configure(DMDTrader trader)
  {
    if (config_ == null || trader == null)
    {
      return;
    }
    
    String value = config_.getProperty("providers");
    
    int provider_count = parseInt(value, 0);
    
    for (int count = 0; count < provider_count; ++count)
    {
      String provider = "provider" + count;
      value = config_.getProperty(provider + ".class");
      if (value != null && value.length() > 0)
      {
        addProvider(provider, value, trader);
      }
      else
      {
        System.err.println("Configurator: " + provider + " class not found");
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Utility method for converting a String into an int.
   * If the String is null, empty or does not contain an int, it returns
   * the specified default value
   *
   * @param value the String to parse
   * @param def the default value to use if parsing fails
   *
   * @return the int value of the given String or def
   */
  protected int parseInt(String value, int def)
  {
    if (value == null || value.length() == 0)
    {
      return def;
    }

    int number = def;
    try
    {
      number = Integer.parseInt(value);
    }
    catch (NumberFormatException exception)
    {
      exception.printStackTrace(System.err);
    }

    return number;
  }

  //---------------------------------------------------------------
  /**
   * Utility method for parsing an IO mode String.
   *
   * @param value the mode string to parse
   *
   * @return the IO mode as a combination of bit flags defined in DMDHandler
   */
  protected int parseMode(String value)
  {
    if (value == null || value.length() == 0 || value.length() > 3)
    {
      return 0;
    }

    int number = 0;
    if (value.indexOf("r") != -1)
    {
      number |= DMDHandler.READ;
    }
    if (value.indexOf("w") != -1)
    {
      number |= DMDHandler.WRITE;
    }
    if (value.indexOf("u") != -1)
    {
      number |= DMDHandler.UPDATE;
    }

    return number;
  }

  //---------------------------------------------------------------
  /**
   * Reads the values for the given provider and tried to add them
   * to the trader.
   *
   * @param provider the provider ID of the config keys
   * @param provider_class the provider's fully qualified class name
   * @param trader the trader to configure
   */
  protected void addProvider(String provider, String provider_class,
                             DMDTrader trader)
  {
    try
    {
      trader.addProvider(provider_class);
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.err);
      return;
    }

    String value = config_.getProperty(provider + ".stores");
    int store_count = parseInt(value, 0);
    for (int count = 0; count < store_count; ++count)
    {
      String store = provider + ".store" + count;
      value = config_.getProperty(store + ".name");
      if (value != null && value.length() > 0)
      {
        addStoreInfo(provider_class, store, trader);
      }
    }

    value = config_.getProperty(provider + ".mappers");
    int mapper_count = parseInt(value, 0);
    for (int count = 0; count < mapper_count; ++count)
    {
      String mapper = provider + ".mapper" + count;
      value = config_.getProperty(mapper + ".name");
      if (value != null && value.length() > 0)
      {
        addMapperInfo(provider_class, mapper, trader);
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Reads a store's configuration and adds it to the trader.
   *
   * @param provider_class the provider's class, used as an identifier
   *        when modifying the trader.
   * @param store the config keys store ID
   * @param trader the trader to configure
   */
  protected void addStoreInfo(String provider_class, String store,
                              DMDTrader trader)
  {
    String mime = config_.getProperty(store + ".mimetype");
    String io = config_.getProperty(store + ".iomethod");
    String mode = config_.getProperty(store + ".iomodes");

    int io_mode = parseMode(mode);

    if (io_mode != 0)
    {
      try
      {
        trader.addStoreInfo(provider_class, mime, io, io_mode);
      }
      catch (Exception exception)
      {
        exception.printStackTrace(System.err);
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Reads a mappers's configuration and adds it to the trader.
   *
   * @param provider_class the provider's class, used as an identifier
   *        when modifying the trader.
   * @param mapper the config keys mapper ID
   * @param trader the trader to configure
   */
  protected void addMapperInfo(String provider_class, String mapper,
                               DMDTrader trader)
  {
    String mime = config_.getProperty(mapper + ".mimetype");
    String mapping = config_.getProperty(mapper + ".mapping");

    try
    {
      trader.addMapperInfo(provider_class, mime, mapping);
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.err);
    }
  }

  protected Properties config_;
}
