///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDTrader.java,v 1.3 2003/03/10 17:54:30 krake Exp $
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


package org.dinopolis.util.metadata;

// local imports
import org.dinopolis.util.metadata.DMDNoSuchProviderException;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 */

/**
 * The DMDTrader interface describes the main entry point for
 * applications using the Dinomeda framework, the Trader.
 * The Trader locates providers of services according to
 * runtime changeable configuration data.
 * <p>
 * It can be used to query for services and their providers
 * and, for convenience, also creating provider instances.
 * <p>
 * Thus the operations on the trader can be separated into three groups:<p>
 * - configure<br>
 * - query<br>
 * - creation<br>
 * <p>
 * <b>Configure</b><br>
 * <pre>
 * // creating a trader instance. in this example a the implemetation
 * // of the Dinomeda project
 * DMDTrader trader = new DinomedaTrader;
 *
 * // adding a provider
 * trader.addProvider("mypackage.MyProvider");
 *
 * // adding information about the provider's stores
 * // the class name specified above is used an identifier
 *
 * // the store handles HTML data, from files and provides all three
 * // IO operations
 * trader.addStoreInfo("mypackage.MyProvider", "text/html", "File", DMDHandler.ALL_IO);
 *
 * // adding information about the provider's name mappers
 * // again the class name for the provider serves as an identifier
 *
 * // the name mapper is mapping between HTML and the Dinomeda naming scheme
 * trader.addMapperInfo("mypackage.MyProvider", "text/html", "Dinomeda");
 * </pre>
 * <p>
 * <b>Query</b><br>
 * <pre>
 * // common queries
 *
 * // ask for all (store) supported MIME types
 * // using the configuration from above, this returns a one element
 * // array: ["text/html"]
 * String[] mimes_types = trader.getStoreMIMETypes();
 *
 * // ask for all available IO methods for the queried MIME type
 * // for the above configuration this returns a one element array:
 * // ["File"]
 * String[] io_methods = trader.getIOMethodsForMIME(mime_types[0]);
 *
 * // ask for all available name mappings for the queried MIME type
 * // for the above configuration this returns a one element array:
 * // ["Dinomeda"]
 * String[] mappings = trader.getNameMappingsForMIME(mime_types[0]);
 *
 * // custom queries - using the general purpose query iterface
 *
 * // query for all stores handling text/html and provide at least
 * // reading
 * DMDServiceQuery store_query = new DMDServiceQuery();
 * store_query.setMIMEType("text/html");
 * store_query.setIOMode(DMDHandler.READ);
 *
 * // using the above configuration, this returns a one element array:
 * // [("mypackage.MyProvider", "text/html", null, "File", ALL_IO)]
 * DMDServiceOffer[] store_offers = trader.findMatchingOffers(store_query);
 *
 * // query for all mappers mapping from text/html to Dinomeda
 * DMDServiceQuery mapper_query = new DMDServiceQuery();
 * mapper_query.setMIMEType("text/html");
 * mapper_query.setNameMapping("Dinomeda");
 *
 * // using the above configuration, this returns a one element array:
 * // [("mypackage.MyProvider", "text/html", "Dinomeda", null, NO_IO)]
 * DMDServiceOffer[] mapper_offers = trader.findMatchingOffers(mapper_query);
 *
 * // querying for a Stream store using above configuration will return
 * // null
 * store_query.setIOMethod("Stream");
 * DMDServiceOffer[] null_offer = trader.findMatchingOffers(store_query);
 *
 * // query for a mapped filestore, provided by a single provider
 * DMDServiceQuery provider_query = new DMDServiceQuery();
 * provider_query.setMIMEType("text/html");
 * provider_query.setNameMapping("Dinomeda");
 * provider_query.setIOMethod("File");
 * provider_query.setIOMode(DMDHandler.READ | DMDHandler.WRITE);
 *
 * // using the example configuration this returns a one element array:
 * // [("mypackage.MyProvider", "text/html", "Dinomeda", "File", ALL_IO)]
 * DMDServicOffer[] provider_offers = trader.findMatchingOffers(provider_query);
 *
 * // all offers returned by findMatchingOffers can be used to
 * // get the appropriate service provider by using findService
 * </pre>
 * <p>
 * <b>Creation</b><br>
 * All offers returned by DMDTrader.findMatchingOffer contain information about
 * the provider which would be able to provide this service.<br>
 * The DMDServiceOffer.getProviderClass() will return the class name used to
 * configure the trader for the service.<br>
 * So the user of the trader could load the class and create an instance itself.
 * <p>
 * For convenience, the trader can do this as well.<br>
 * More over, it can, if a matching provide is known, create a provider instance
 * directly from a service query.<p>
 * <pre>
 * // create the provider instance by using an offer the trader created
 * // for a query
 * DMDServiceProvider provider_by_offer = trader.findService(provider_offers[0]);
 *
 * // create a provider instance by using the query without finding offers first
 * DMDServiceProvider provider_by_query = trader.findService(provider_query);
 * </pre>
 */
public interface DMDTrader
{
  //---------------------------------------------------------------
  /**
   * Searches for a service provider which will be able to provide a
   * service matching the query's requirements.
   *
   * @param query a DMDServiceOffer instance containing all requirements
   *              a potential candidate service has to offer.
   * @return a DMDServiceProvider which can offer all components matching the
   *         requirements specified by the query object or null if no
   *         configured service provider can offer such a service.
   * @throws IllegalArgumentException if query is null.
   */
  public DMDServiceProvider findService(DMDServiceOffer query)
          throws IllegalArgumentException;

  //---------------------------------------------------------------
  /**
   * Searches for service providers which will be able to provide a
   * service matching the query's requirements.
   * If there is more than one match, the provider at index 0 should be
   * the same provider findService(DMDServiceOffer) would have returned.
   *
   * @param query a DMDServiceOffer instance containing all requirements
   *              a potential candidate service has to offer.
   * @return an array of service providers which will be able to offer all
   *         components matching the query's requirements or null if no
   *         configured service provider can offer such a service.
   * @throws IllegalArgumentException if query is null.
   */
  public DMDServiceProvider[] findServices(DMDServiceOffer query)
          throws IllegalArgumentException;

  //---------------------------------------------------------------
  /**
   * Finds service offers that match the give query.
   * This can be used to find alternative to queries that could not be
   * matches or to get information about the services known to the
   * trader.
   * In contrast to the findService(s) method, this method should not
   * create provider instances.
   *
   * @param query a DMDServiceOffer instance containing all requirements
   *              the returned offers have to match.
   *
   * @return an array containing all offers matching the query or null, if
   *         the query cannot be satisfied.
   *
   * @throws IllegalArgumentException if query is null.
   */
  public DMDServiceOffer[] findMatchingOffers(DMDServiceOffer query)
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
   * Queries the trader for all providers' class names.
   *
   * @return an array containing all known provider classes
   */
  public String[] getProviderClasses();

  //---------------------------------------------------------------
  /**
   * Queries the trader for all MIME types it knows stores for.
   *
   * @return an array containing all known store MIME types
   */
  public String[] getStoreMIMETypes();

  //---------------------------------------------------------------
  /**
   * Queries the trader for all IO methods it known stores for.
   *
   * @return an array containing a union of all stores IO methods.
   */
  public String[] getIOMethods();

  //---------------------------------------------------------------
  /**
   * Queries the trader for all IO methods available for a certain
   * MIME type.
   *
   * @param mime_type a MIME type string.
   * @return an array containing a union of the IO methods of stores
   *         which can handle the given MIME type
   *
   * @throws IllegalArgumentException if the parameter is null or empty.
   */
  public String[] getIOMethodsForMIME(String mime_type)
      throws IllegalArgumentException;

  //---------------------------------------------------------------
  /**
   * Queries the trader for all name mappings it knows mappers for.
   *
   * @return an array containing a union of all mappers' name mappings.
   */
  public String[] getNameMappings();

  //---------------------------------------------------------------
  /**
   * Queries the trader for all name mappings available for a certain
   * MIME type.
   *
   * @param mime_type a MIME type string.
   * @return an array containing a union of the name mappings of mappers
   *         which can handle the given MIME type.
   * @throws IllegalArgumentException if the parameter is null or empty.
   */
  public String[] getNameMappingsForMIME(String mime_type)
      throws IllegalArgumentException;

}
