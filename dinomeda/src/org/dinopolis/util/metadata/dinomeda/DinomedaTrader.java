///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaTrader.java,v 1.3 2003/03/03 23:17:33 krake Exp $
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

// local imports
import org.dinopolis.util.metadata.DMDHandler;
import org.dinopolis.util.metadata.DMDServiceProvider;
import org.dinopolis.util.metadata.DMDServiceOffer;
import org.dinopolis.util.metadata.DMDTrader;
import org.dinopolis.util.metadata.DMDNoSuchProviderException;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 */
 
/**
 * This class is the Dinomeda project's implementation of a DMDTrader.<br>
 * It implements all methods as required by the DMDTrader documentation.
 * <p>
 * The trader stores the configuration data in DMDServiceOffer objects.
 */
public class DinomedaTrader implements DMDTrader
{
  //---------------------------------------------------------------
  /**
   * Creates a DinomedaTrader with empty configuration
   */
  public DinomedaTrader()
  {
    stores_by_mime_ = new HashMap();
    mappers_by_mime_ = new HashMap();
    provider_classes_ = new HashMap();
  }

  //---------------------------------------------------------------
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
  public void removeProvider(String provider_class_name)
      throws IllegalArgumentException, DMDNoSuchProviderException
  {
    if (!provider_classes_.containsKey(provider_class_name))
    {
      throw new DMDNoSuchProviderException();
    }

    provider_classes_.remove(provider_class_name);

    List offers = new ArrayList();
    Collection values;
    Iterator iter;
    values = stores_by_mime_.values();
    iter = values.iterator();
    while (iter.hasNext())
    {
      List list = (List) iter.next();
      Iterator list_iter = list.iterator();
      while (list_iter.hasNext())
      {
        DMDServiceOffer offer = (DMDServiceOffer) list_iter.next();
        if (provider_class_name.equals(offer.getProviderClass()))
        {
          list_iter.remove();
        }
      }
      if (list.isEmpty())
      {
        iter.remove();
      }
    }

    values = mappers_by_mime_.values();
    iter = values.iterator();
    while (iter.hasNext())
    {
      List list = (List) iter.next();
      Iterator list_iter = list.iterator();
      while (list_iter.hasNext())
      {
        DMDServiceOffer offer = (DMDServiceOffer) list_iter.next();
        if (provider_class_name.equals(offer.getProviderClass()))
        {
          list_iter.remove();
        }
      }
      if (list.isEmpty())
      {
        iter.remove();
      }
    }
  }

  //---------------------------------------------------------------
  public String[] getProviderClasses()
  {
    Set names = provider_classes_.keySet();

    String[] result = new String[names.size()];

    return (String[])names.toArray(result);
  }

  //---------------------------------------------------------------
  public String[] getStoreMIMETypes()
  {
    Set type_set = stores_by_mime_.keySet();

    String[] types = new String[type_set.size()];
    types = (String[]) type_set.toArray(types);

    return types;
  }

  //---------------------------------------------------------------
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

  //---------------------------------------------------------------
  /**
   * A map from String to List where the string is a MIME type
   * and the list contains DMDServiceOffers of the stores for this MIME type.
   */
  protected Map stores_by_mime_;

  /**
   * A map from String to List where the string is a MIME type
   * and the list contains DMDServiceOffers of the mappers for this MIME type.
   */
  protected Map mappers_by_mime_;

  /**
   * A map from String to Class where the string is a fully qualified
   * class name of a provider and the Class is its class object.
   */
  protected Map provider_classes_;
}
