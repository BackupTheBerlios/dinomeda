///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaTrader.java,v 1.2 2003/02/28 13:00:53 krake Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

// local imports
import org.dinopolis.utils.metadata.DMDHandler;
import org.dinopolis.utils.metadata.DMDServiceProvider;
import org.dinopolis.utils.metadata.DMDServiceOffer;
import org.dinopolis.utils.metadata.DMDTrader;
import org.dinopolis.utils.metadata.DMDNoSuchProviderException;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
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
    stores_by_mime_ = new HashMap();
    mappers_by_mime_ = new HashMap();
    provider_classes_ = new HashMap();
  }

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
   * @throws IllegalArgumentException if query is null
   */
  public DMDServiceProvider findService(DMDServiceOffer query)
      throws IllegalArgumentException
  {
    if (query == null)
    {
      throw new IllegalArgumentException();
    }

    DMDServiceProvider provider = null;
    DMDServiceOffer[] offers = findMatchingOffers(query);
    if (offers != null)
    {
      String class_name = offers[0].getProviderClass();
      Class provider_class = (Class) provider_classes_.get(class_name);
      if (provider_class != null)
      {
        try
        {
          provider = (DMDServiceProvider) provider_class.newInstance();
          if (!provider.canProvide(query))
          {
            System.err.println("DinomedaTrader: provider-offer missmatch");
            System.err.println("Provider=" + class_name);
            System.err.println("offer=" + offers[0]);
            provider = null;
          }
        }
        catch (Exception exception)
        {
          exception.printStackTrace(System.err);
        }
      }
    }

    return provider;
  }

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
   * @throws IllegalArgumentException if query is null
   */
  public DMDServiceProvider[] findServices(DMDServiceOffer query)
      throws IllegalArgumentException
  {
    if (query == null)
    {
      throw new IllegalArgumentException();
    }

    DMDServiceOffer[] offers = findMatchingOffers(query);
    if (offers == null)
    {
      return null;
    }
    
    List result_list = new ArrayList();
    for (int count = 0; count < offers.length; ++count)
    {
      String class_name = offers[count].getProviderClass();
      Class provider_class = (Class) provider_classes_.get(class_name);
      if (provider_class != null)
      {
        try
        {
          DMDServiceProvider provider =
            (DMDServiceProvider) provider_class.newInstance();
          if (!provider.canProvide(query))
          {
            System.err.println("DinomedaTrader: provider-offer missmatch");
            System.err.println("Provider=" + class_name);
            System.err.println("offer=" + offers[count]);
            provider = null;
          }
          else
          {
            result_list.add(provider);
          }
        }
        catch (Exception exception)
        {
          exception.printStackTrace(System.err);
        }
      }
    }
    
    DMDServiceProvider[] result = new DMDServiceProvider[result_list.size()];

    return (DMDServiceProvider[]) result_list.toArray(result);
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDServiceOffer[] findMatchingOffers(DMDServiceOffer query)
      throws IllegalArgumentException
  {
    if (query == null)
    {
      throw new IllegalArgumentException();
    }
    
    List stores = new ArrayList();
    List mappers = new ArrayList();
    
    Collection offers = stores_by_mime_.values();
    Iterator iter = offers.iterator();
    while (iter.hasNext())
    {
      List offer_list = (List) iter.next();
      
      Iterator list_iter = offer_list.iterator();
      while (list_iter.hasNext())
      {
        DMDServiceOffer offer = (DMDServiceOffer) list_iter.next();
        if (offer != null)
        {
          if (offer.matchesStore(query))
          {
            stores.add(offer);
          }
        }
      }
    }
    
    offers = mappers_by_mime_.values();
    iter = offers.iterator();
    while (iter.hasNext())
    {
      List offer_list = (List) iter.next();
      
      Iterator list_iter = offer_list.iterator();
      while (list_iter.hasNext())
      {
        DMDServiceOffer offer = (DMDServiceOffer) list_iter.next();
        if (offer != null)
        {
          if (offer.matchesMapper(query))
          {
            mappers.add(offer);
          }
        }
      }
    }
        
    int mode = 0;
    if (query.getProviderClass() != null)
    {
      mode |= 1;
    }
    if (query.getMIMEType() != null)
    {
      mode |= 2;
    }
    if (query.getIOMethod() != null || query.getIOMode() != DMDHandler.NO_IO)
    {
      mode |= 4;
    }
    if (query.getNameMapping() != null)
    {
      mode |= 8;
    }
    
    List result_list = new ArrayList();
    switch (mode)
    {
      case 0:
      case 1:
      case 2:
      case 3:
        result_list.addAll(stores);
        result_list.addAll(mappers);
        break;
        
      case 4:
      case 5:
      case 6:
      case 7:
        result_list.addAll(stores);
        break;
        
      case 8:
      case 9:
      case 10:
      case 11:
        result_list.addAll(mappers);
        break;
        
      case 12:
      case 13:
      case 14:
      case 15:
        for (int scount = 0; scount < stores.size(); ++scount)
        {
          for (int mcount = 0; mcount < mappers.size(); ++mcount)
          {
            DMDServiceOffer store = (DMDServiceOffer) stores.get(scount);
            DMDServiceOffer mapper = (DMDServiceOffer) mappers.get(mcount);
            if (store.matchesStore(mapper)) // provider and mime equal
            {
              result_list.add(
                new DMDServiceOffer(store.getProviderClass(), store.getMIMEType(),
                                    mapper.getNameMapping(),
                                    store.getIOMethod(), store.getIOMode()));
            }
          }
        }
        break;
      
      default:
        break;
        
    }
    
    if (result_list.size() == 0)
    {
      return null;
    }
    
    DMDServiceOffer[] result = new DMDServiceOffer[result_list.size()];
    
    return (DMDServiceOffer[])result_list.toArray(result);
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

    if (provider_classes_.containsKey(class_name))
    {
      return;
    }
    
    Class provider_class = Class.forName(class_name);
    provider_classes_.put(class_name, provider_class);
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
    
    if (!provider_classes_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }

    DMDServiceOffer offer = 
      new DMDServiceOffer(provider_class_name, mime_type, "", io_method, io_mode);
    
    List offers = (List) stores_by_mime_.get(mime_type);
    if (offers == null)
    {
      offers = new ArrayList();
      stores_by_mime_.put(mime_type, offers);
    }
    offers.add(offer);
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

    if (!provider_classes_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }

    DMDServiceOffer offer = 
      new DMDServiceOffer(provider_class_name, mime_type, name_mapping, null, DMDHandler.NO_IO);
    
    List offers = (List) mappers_by_mime_.get(mime_type);
    if (offers == null)
    {
      offers = new ArrayList();
      mappers_by_mime_.put(mime_type, offers);
    }
    offers.add(offer);
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
  public void removeProvider(String provider_class_name)
      throws IllegalArgumentException, DMDNoSuchProviderException
  {
    if (!provider_classes_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }
    
    //TODO: remove offers
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getStoreMIMETypes()
  {
    Set type_set = stores_by_mime_.keySet();
       
    String[] types = new String[type_set.size()];
    types = (String[]) type_set.toArray(types);
    
    return types;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getIOMethods()
  {
    Collection values = stores_by_mime_.values();
    Set all_methods = new HashSet();
    
    Iterator iter = values.iterator();
    while (iter.hasNext())
    {
      List offers = (List) iter.next();
      if (offers != null)
      {
        Iterator offer_iter = offers.iterator();
        while (offer_iter.hasNext())
        {
          DMDServiceOffer offer = (DMDServiceOffer) offer_iter.next();
          if (offer != null)
          {
            all_methods.add(offer.getIOMethod());
          }
        }
      }
    } 
  
    String[] result = new String[all_methods.size()];
    
    return (String[]) all_methods.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getIOMethodsForMIME(String mime_type)
      throws IllegalArgumentException
  {
    if (mime_type == null || mime_type.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    
    List offers = (List) stores_by_mime_.get(mime_type);
    if (offers == null)
    {
      return new String[0];
    }
    
    Set methods = new HashSet();
    
    Iterator iter = offers.iterator();
    while (iter.hasNext())
    {
      DMDServiceOffer offer = (DMDServiceOffer) iter.next();
      if (offer != null)
      {
        methods.add(offer.getIOMethod());
      }
    }
    
    String[] result = new String[methods.size()];
    
    return (String[]) methods.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getNameMappings()
  {
    Collection values = mappers_by_mime_.values();
    Set all_mappings = new HashSet();
    
    Iterator iter = values.iterator();
    while (iter.hasNext())
    {
      List offers = (List) iter.next();
      if (offers != null)
      {
        Iterator offer_iter = offers.iterator();
        while (offer_iter.hasNext())
        {
          DMDServiceOffer offer = (DMDServiceOffer) offer_iter.next();
          if (offer != null)
          {
            all_mappings.add(offer.getNameMapping());
          }
        }
      }
    } 
  
    String[] result = new String[all_mappings.size()];
    
    return (String[]) all_mappings.toArray(result);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String[] getNameMappingsForMIME(String mime_type)
      throws IllegalArgumentException
  {
    if (mime_type == null || mime_type.length() == 0)
    {
      throw new IllegalArgumentException();
    }
    
    List offers = (List) mappers_by_mime_.get(mime_type);
    if (offers == null)
    {
      return new String[0];
    }
    
    Set mappings = new HashSet();
    
    Iterator iter = offers.iterator();
    while (iter.hasNext())
    {
      DMDServiceOffer offer = (DMDServiceOffer) iter.next();
      if (offer != null)
      {
        mappings.add(offer.getNameMapping());
      }
    }
    
    String[] result = new String[mappings.size()];
    
    return (String[]) mappings.toArray(result);
  }
     
  protected Map stores_by_mime_;
  protected Map mappers_by_mime_;
  protected Map provider_classes_;
}
