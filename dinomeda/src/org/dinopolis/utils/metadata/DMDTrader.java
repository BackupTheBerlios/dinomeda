///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDTrader.java,v 1.1 2003/02/27 21:56:11 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002
//
///////////////////////////////////////////////////////////////////////////////
//                                                                          
//   This program is free software; you can redistribute it and/or modify  
//   it under the terms of the GNU Lesser General Public License as        
//   published by the Free Software Foundation; either version 2 of the    
//   License, or (at your option) any later version.                       
//                                                                         
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.utils.metadata;

// local imports
import org.dinopolis.utils.metadata.DMDNoSuchProviderException;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The DMDTrader interface describes the main entry point for
 * applications using the Dinomeda framework, the Trader.
 * The Trader locates providers of services according to
 * runtime changeable configuration data.
 */
public interface DMDTrader
{
  //---------------------------------------------------------------
  /**
   * Searches for a service provider which will be able to provide a
   * service matching the query's requirements.
   *
   * @param query a DMDServiceQuery instance containing all requirements
   *              a potential candidate service has to offer.
   * @return a DMDServiceProvider which can offer all components matching the
   *         requirements specified by the query object or null if no
   *         configured service provider can offer such a service.
   * @throws IllegalArgumentException if query is null or empty.
   */
  public DMDServiceProvider findService(DMDServiceQuery query)
          throws IllegalArgumentException;

  //---------------------------------------------------------------
  /**
   * Searches for service providers which will be able to provide a
   * service matching the query's requirements.
   * If there is more than one match, the provider at index 0 should be
   * the same provider findService(DMDServiceQuery) would have returned.
   *
   * @param query a DMDServiceQuery instance containing all requirements
   *              a potential candidate service has to offer.
   * @return an array of service providers which will be able to offer all
   *         components matching the query's requirements or null if no
   *         configured service provider can offer such a service.
   * @throws IllegalArgumentException if query is null or empty.
   */
  public DMDServiceProvider[] findServices(DMDServiceQuery query)
          throws IllegalArgumentException;

  //---------------------------------------------------------------
  /**
   * Registers a new service provider at the trader.
   *
   * @param class_name the fully qualified class name of the service provider.
   * @throws IllegalArgumentException if class_name is null or empty.
   * @throws ClassNotFoundException if the class cannot be loaded.
   */
  public void addProvider(String class_name)
          throws IllegalArgumentException, ClassNotFoundException;

  //---------------------------------------------------------------
  /**
   * Adds service description data for a previously added provider.
   * The data describes the capabilites of a DMDStore implementation.
   * This can be called any number of times for any provider in any
   * order.
   * If no information is added for a provider the trader implementation
   * may ignore the trader when searching for services.
   *
   * @throws IllegalArgumentException if any of the String parameters is null
   *         or empty or the IO mode is an invalid number.
   * @throws DMDNoSuchProviderException if class_name does not match any
   *         provider added with addProvider(String).
   */
  public void addStoreInfo(String provider_class_name, String mime_type,
                           String io_method, int io_modes)
          throws IllegalArgumentException, DMDNoSuchProviderException;

  //---------------------------------------------------------------
  /**
   * Adds service description data for a previously added provider.
   * The data describes the capabilites of a DMDMapper implementation.
   * This can be called any number of times for any provider in any
   * order.
   * If no information is added for a provider the trader implementation
   * may ignore the trader when searching for services.
   *
   * @throws IllegalArgumentException if any of the parameters is null or empty.
   * @throws DMDNoSuchProviderException if class_name does not match any
   *         provider added with addProvider(String).
   */
  public void addMapperInfo(String provider_class_name, String mime_type,
                           String name_mapping)
          throws IllegalArgumentException, DMDNoSuchProviderException;

  //---------------------------------------------------------------
  /**
   * Removes a provider and all asociated service information.
   *
   * @param class_name the fully qualified class name of the service provider
   *                   as passed to the addProvider(String) method.
   * @throws IllegalArgumentException if class_name is null or empty.
   * @throws DMDNoSuchProviderException if the given provider is unknown in to
   *         this trader.
   */
  public void removeProvider(String provider_class_name)
          throws IllegalArgumentException, DMDNoSuchProviderException;
          
  //---------------------------------------------------------------
  /**
   */
  public String[] getStoreMIMETypes();

  //---------------------------------------------------------------
  /**
   */
  public String[] getIOMethods();
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getIOMethodsForMIME(String mime_type)
      throws IllegalArgumentException;
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getNameMappings();
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getNameMappingsForMIME(String mime_type)
      throws IllegalArgumentException;
  
}
