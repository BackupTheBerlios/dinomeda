///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaProvider.java,v 1.8 2003/05/06 16:47:50 krake Exp $
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


package org.dinopolis.util.metadata.dinomeda;

// Java imports

// external packages

// local packages
import org.dinopolis.util.metadata.DMDHandler;
import org.dinopolis.util.metadata.DMDMapper;
import org.dinopolis.util.metadata.DMDServiceProvider;
import org.dinopolis.util.metadata.DMDServiceOffer;
import org.dinopolis.util.metadata.DMDStore;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 */

/**
 * The provider of the Dinomeda Project.
 */

public class DinomedaProvider implements DMDServiceProvider
{
  /**
   * Number of known stores
   */
  public static final int STORES  = 6;
  /**
   * Number of known mappers
   */
  public static final int MAPPERS = 4;
  /**
   * The class name of this provider
   */
  public static final String CLASS_NAME =
    "org.dinopolis.utils.metadata.dinomeda.DinomedaProvider";

  //---------------------------------------------------------------
  /**
   * Creates the provider instance and initializes its offers.
   */
  public DinomedaProvider()
  {
    store_offers_ = new DMDServiceOffer[STORES];
    
    store_offers_[0] = 
      new DMDServiceOffer(CLASS_NAME, "audio/x-mp3", null, "File", DMDHandler.ALL_IO);
        
    store_offers_[1] = 
      new DMDServiceOffer(CLASS_NAME, "application/pdf", null, "File", DMDHandler.ALL_IO);

    store_offers_[2] = 
      new DMDServiceOffer(CLASS_NAME, "image/png", null, "File", DMDHandler.READ);

    store_offers_[3] = 
      new DMDServiceOffer(CLASS_NAME, "text/html", null, "File", DMDHandler.ALL_IO);

    store_offers_[4] =
      new DMDServiceOffer(CLASS_NAME, "image/png", null, "Stream", DMDHandler.READ);

    store_offers_[5] =
      new DMDServiceOffer(CLASS_NAME, "text/html", null, "Stream", DMDHandler.ALL_IO);
    
    mapper_offers_ = new DMDServiceOffer[MAPPERS];
    
    mapper_offers_[0] = 
      new DMDServiceOffer(CLASS_NAME, "audio/x-mp3", "Dinomeda", null, DMDHandler.NO_IO);
  
    mapper_offers_[1] = 
      new DMDServiceOffer(CLASS_NAME, "application/pdf", "Dinomeda", null, DMDHandler.NO_IO);
  
    mapper_offers_[2] = 
      new DMDServiceOffer(CLASS_NAME, "image/png", "Dinomeda", null, DMDHandler.NO_IO);
  
    mapper_offers_[3] = 
      new DMDServiceOffer(CLASS_NAME, "text/html", "Dinomeda", null, DMDHandler.NO_IO);
  }
   
  //---------------------------------------------------------------
  /**
   * Implementation of getStore as required by DMDProvider
   */
  public DMDStore getStore(DMDServiceOffer query)
  {
    int count = -1;
    for (count = 0; count < STORES; ++count)
    {
      if (store_offers_[count].matchesStore(query))
      {
        break;
      }
    }

    DMDStore store = null;
    switch (count)
    {
      case 0: store = new MP3v1FileStore();
        break;

      case 1: store = new PDFv13FileStore();
        break;

      case 2: store = new PNGFileStore();
        break;

      case 3: store = new HTMLFileStore();
        break;

      case 4: store = new PNGStreamStore();
        break;

      case 5: store = new HTMLStreamStore();
        break;

      default:
        break;
    }

    return store;
  }

  //---------------------------------------------------------------
  /**
   * Implementation of getMapper as required by DMDProvider
   */
  public DMDMapper getMapper(DMDServiceOffer query)
  {
    int count = -1;
    for (count = 0; count < MAPPERS; ++count)
    {
      if (mapper_offers_[count].matchesMapper(query))
      {
        break;
      }
    }

    DMDMapper mapper = null;
    switch (count)
    {
      case 0: mapper = new MP3v1Mapper();
        break;

      case 1: mapper = new PDFv13Mapper();
        break;

      case 2: mapper = new PNGMapper();
        break;

      case 3: mapper = new HTMLMapper();
        break;

      default:
        break;
    }

    return mapper;
  }

  //---------------------------------------------------------------
  /**
   * Implementation of canProvide as required by DMDProvider
   */
  public boolean canProvide(DMDServiceOffer query)
  {
    if (query == null)
    {
      return false;
    }

    boolean provides_store = false;

    // if no store is needed, we don't look for one
    if (query.getIOMethod() == null && query.getIOMode() == DMDHandler.NO_IO)
    {
      provides_store = true;
    }
    else
    {
      for (int count = 0; count < STORES; ++count)
      {
        if (store_offers_[count].matchesStore(query))
        {
          provides_store = true;
          break;
        }
      }
    }

    boolean provides_mapper = false;
    // if no mapper is needed, we don't look for one
    if (query.getNameMapping() == null)
    {
      provides_mapper =true;
    }
    else
    {
      for (int count = 0; count < MAPPERS; ++count)
      {
        if (mapper_offers_[count].matchesMapper(query))
        {
          provides_mapper = true;
          break;
        }
      }
    }

    return provides_mapper && provides_store;
  }
  
  protected DMDServiceOffer[] store_offers_;
  protected DMDServiceOffer[] mapper_offers_;
}
