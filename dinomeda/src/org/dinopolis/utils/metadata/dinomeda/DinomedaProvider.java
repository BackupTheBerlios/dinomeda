///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaProvider.java,v 1.2 2003/02/28 13:00:53 krake Exp $
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


package org.dinopolis.utils.metadata.dinomeda;

// Java imports

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDHandler;
import org.dinopolis.utils.metadata.DMDMapper;
import org.dinopolis.utils.metadata.DMDServiceProvider;
import org.dinopolis.utils.metadata.DMDServiceOffer;
import org.dinopolis.utils.metadata.DMDStore;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaProvider implements DMDServiceProvider
{
  public static final int STORES  = 6;
  public static final int MAPPERS = 4;
  public static final String CLASS_NAME = 
    "org.dinopolis.utils.metadata.dinomeda.DinomedaProvider";

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaProvider()
  {
    store_offers_ = new DMDServiceOffer[STORES];
    
    store_offers_[0] = 
      new DMDServiceOffer(CLASS_NAME, "audio/x-mp3", null, "File", DMDHandler.ALL_IO);
        
    store_offers_[1] = 
      new DMDServiceOffer(CLASS_NAME, "application/x-pdf", null, "File", DMDHandler.ALL_IO);

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
      new DMDServiceOffer(CLASS_NAME, "application/x-pdf", "Dinomeda", null, DMDHandler.NO_IO);
  
    mapper_offers_[2] = 
      new DMDServiceOffer(CLASS_NAME, "image/png", "Dinomeda", null, DMDHandler.NO_IO);
  
    mapper_offers_[3] = 
      new DMDServiceOffer(CLASS_NAME, "text/html", "Dinomeda", null, DMDHandler.NO_IO);
  }
   
  //---------------------------------------------------------------
  /**
   * Method description
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
   * Method description
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
   * Method description
   */
  public boolean canProvide(DMDServiceOffer query)
  {
    boolean provides_store = false;
    
    for (int count = 0; count < STORES; ++count)
    {
      if (store_offers_[count].matchesStore(query))
      {
        provides_store = true;
        break;
      }
    }

    if (provides_store)
    {
      for (int count = 0; count < MAPPERS; ++count)
      {
        if (mapper_offers_[count].matchesMapper(query))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  protected DMDServiceOffer[] store_offers_;
  protected DMDServiceOffer[] mapper_offers_;
}
