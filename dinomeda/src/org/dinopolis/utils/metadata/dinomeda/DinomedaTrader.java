///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaTrader.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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


package org.dinopolis.utils.metadata.dinomeda;

// Java imports
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Vector;

// local imports
import org.dinopolis.utils.metadata.DMDServiceProvider;
import org.dinopolis.utils.metadata.DMDServiceQuery;
import org.dinopolis.utils.metadata.DMDTrader;
import org.dinopolis.utils.metadata.DMDNoSuchProviderException;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * This class is the Dinomeda project's implementation of a DMDTrader.
 */
public class DinomedaTrader implements DMDTrader
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaTrader()
  {
    providers_ = new TreeMap();
    mime_types_ = new HashSet();
    io_methods_by_mime_ = new HashMap();
    name_mappings_by_mime_ = new HashMap();
  }

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
      throws IllegalArgumentException
  {
    if (query == null)
    {
      throw new IllegalArgumentException();
    }

    DMDServiceProvider provider = null;

    Iterator iter = providers_.values().iterator();
    while (iter.hasNext())
    {
      provider = (DMDServiceProvider) iter.next();
      if (provider != null)
      {
        if (provider.canProvide(query))
        {
          return provider;
        }
      }
    }
    return null;
  }

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
      throws IllegalArgumentException
  {
    if (query == null)
    {
      throw new IllegalArgumentException();
    }

    Vector providers = new Vector(providers_.size());
    DMDServiceProvider provider = null;

    Iterator iter = providers_.values().iterator();
    while (iter.hasNext())
    {
      provider = (DMDServiceProvider) iter.next();
      if (provider != null)
      {
        if (provider.canProvide(query))
        {
          providers.add(provider);
        }
      }
    }

    DMDServiceProvider[] result = new DMDServiceProvider[providers.size()];

    return (DMDServiceProvider[]) providers.toArray(result);
  }

  //---------------------------------------------------------------
  /**
   * Registers a new service provider at the trader.
   *
   * @param class_name the fully qualified class name of the service provider.
   * @throws IllegalArgumentException if class_name is null or empty.
   * @throws ClassNotFoundException if the class cannot be loaded.
   */
  public void addProvider(String class_name)
      throws IllegalArgumentException, ClassNotFoundException
  {
    if (class_name == null || class_name.length() == 0)
    {
      throw new IllegalArgumentException();
    }

    if (providers_.containsKey(class_name))
    {
      return;
    }

    Class provider_class = Class.forName(class_name);
    DMDServiceProvider provider = null;
    try
    {
      provider = (DMDServiceProvider) provider_class.newInstance();
    }
    catch (Exception exception)
    {
      exception.printStackTrace(System.err);
      throw new ClassNotFoundException();
    }

    providers_.put(class_name, provider);
  }

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
                           String io_method, int io_mode)
          throws IllegalArgumentException, DMDNoSuchProviderException
  {
    if (provider_class_name == null || provider_class_name.length() == 0
        || mime_type == null || mime_type.length() == 0
        || io_method == null || io_method.length() == 0
        || io_mode < 0)
    {
      throw new IllegalArgumentException();
    }
    
    if (!providers_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }

    mime_types_.add(mime_type);

    Set io_methods = (Set) io_methods_by_mime_.get(mime_type);
    if (io_methods == null)
    {
      io_methods = new HashSet();
      io_methods_by_mime_.put(mime_type, io_methods);
    }
    io_methods.add(io_method);
  }

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
          throws IllegalArgumentException, DMDNoSuchProviderException
  {
    if (provider_class_name == null || provider_class_name.length() == 0
        || mime_type == null || mime_type.length() == 0
        || name_mapping == null || name_mapping.length() == 0)
    {
      throw new IllegalArgumentException();
    }

    if (!providers_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }

    Set mappings = (Set) name_mappings_by_mime_.get(mime_type);
    if (mappings == null)
    {
      mappings = new HashSet();
      name_mappings_by_mime_.put(mime_type, mappings);
    }
    mappings.add(name_mapping);
  }

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
  public void removeProvider(String class_name)
      throws IllegalArgumentException, DMDNoSuchProviderException
  {
  }
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getStoreMIMETypes()
  {
    String[] types = new String[mime_types_.size()];
    types = (String[]) mime_types_.toArray(types);
    
    return types;
  }

  //---------------------------------------------------------------
  /**
   */
  public String[] getIOMethods()
  {
    Set all_methods = new HashSet();
    
    Iterator iter = io_methods_by_mime_.values().iterator();
    while (iter.hasNext())
    {
      Set methods = (Set) iter.next();
      if (methods != null)
      {
        all_methods.addAll(methods);
      }
    } 
  
    String[] result = new String[all_methods.size()];
    
    return (String[]) all_methods.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getIOMethodsForMIME(String mime_type)
      throws IllegalArgumentException
  {
    if (mime_type == null || mime_type.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    
    Set methods = (Set) io_methods_by_mime_.get(mime_type);
    if (methods == null)
    {
      return new String[0];
    }
    
    String[] result = new String[methods.size()];
    
    return (String[]) methods.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getNameMappings()
  {
    Set all_mappings = new HashSet();
    
    Iterator iter = name_mappings_by_mime_.values().iterator();
    while (iter.hasNext())
    {
      Set mappings = (Set) iter.next();
      if (mappings != null)
      {
        all_mappings.addAll(mappings);
      }
    } 
  
    String[] result = new String[all_mappings.size()];
    
    return (String[]) all_mappings.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   */
  public String[] getNameMappingsForMIME(String mime_type)
      throws IllegalArgumentException
  {
    if (mime_type == null || mime_type.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    
    Set mappings = (Set) name_mappings_by_mime_.get(mime_type);
    if (mappings == null)
    {
      return new String[0];
    }
    
    String[] result = new String[mappings.size()];
    
    return (String[]) mappings.toArray(result);
  }
  
  protected Map providers_;
  protected Set mime_types_;
  protected Map io_methods_by_mime_;
  protected Map name_mappings_by_mime_;
}
