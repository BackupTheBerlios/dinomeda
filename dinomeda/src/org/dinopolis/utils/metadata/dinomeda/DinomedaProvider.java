///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaProvider.java,v 1.1 2003/02/27 21:56:13 krake Exp $
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
import org.dinopolis.utils.metadata.DMDServiceQuery;
import org.dinopolis.utils.metadata.DMDStore;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaProvider implements DMDServiceProvider
{
  public static final int STORES  = 4;
  public static final int MAPPERS = 4;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaProvider()
  {
    store_queries_ = new DMDServiceQuery[STORES];
    
    store_queries_[0] = new DMDServiceQuery();
    store_queries_[0].setMIMEType("audio/x-mp3");
    store_queries_[0].setIOMethod("File");
    store_queries_[0].setIOMode(DMDHandler.ALL_IO);
        
    store_queries_[1] = new DMDServiceQuery();
    store_queries_[1].setMIMEType("application/x-pdf");
    store_queries_[1].setIOMethod("File");
    store_queries_[1].setIOMode(DMDHandler.ALL_IO);

    store_queries_[2] = new DMDServiceQuery();
    store_queries_[2].setMIMEType("image/png");
    store_queries_[2].setIOMethod("File");
    store_queries_[2].setIOMode(DMDHandler.ALL_IO);

    store_queries_[3] = new DMDServiceQuery();
    store_queries_[3].setMIMEType("text/html");
    store_queries_[3].setIOMethod("File");
    store_queries_[3].setIOMode(DMDHandler.ALL_IO);
            
    mapper_queries_ = new DMDServiceQuery[MAPPERS];
    
    mapper_queries_[0] = new DMDServiceQuery();
    mapper_queries_[0].setMIMEType("audio/x-mp3");
    mapper_queries_[0].setNameMapping("Dinomeda");
  
    mapper_queries_[1] = new DMDServiceQuery();
    mapper_queries_[1].setMIMEType("application/x-pdf");
    mapper_queries_[1].setNameMapping("Dinomeda");
  
    mapper_queries_[2] = new DMDServiceQuery();
    mapper_queries_[2].setMIMEType("image/png");
    mapper_queries_[2].setNameMapping("Dinomeda");
  
    mapper_queries_[3] = new DMDServiceQuery();
    mapper_queries_[3].setMIMEType("text/html");
    mapper_queries_[3].setNameMapping("Dinomeda");
  }
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDStore getStore(DMDServiceQuery query)
  {
    int count = -1;
    for (count = 0; count < STORES; ++count)
    {
      if (store_queries_[count].matchesStore(query))
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
        
      default:
        break;
    }
    
    return store;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDMapper getMapper(DMDServiceQuery query)
  {
    int count = -1;
    for (count = 0; count < MAPPERS; ++count)
    {
      if (mapper_queries_[count].matchesMapper(query))
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
  public boolean canProvide(DMDServiceQuery query)
  {
    boolean provides_store = false;
    
    for (int count = 0; count < STORES; ++count)
    {
      if (store_queries_[count].matchesStore(query))
      {
        provides_store = true;
        break;
      }
    }

    if (provides_store)
    {
      for (int count = 0; count < MAPPERS; ++count)
      {
        if (mapper_queries_[count].matchesMapper(query))
        {
          return true;
        }
      }
    }
    
    return false;
  }
  
  protected DMDServiceQuery[] store_queries_;
  protected DMDServiceQuery[] mapper_queries_;
}
