///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceOffer.java,v 1.1 2003/03/02 19:59:08 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2003
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

// Java imports

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DMDServiceOffer
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDServiceOffer(String provider_class, String mime_type,
                         String name_mapping, 
                         String io_method, int io_mode)
  {
    provider_class_ = provider_class;
    mime_type_ = mime_type;
    name_mapping_ = name_mapping;
    io_method_ = io_method;
    io_mode_ = io_mode;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getProviderClass()
  {
    return provider_class_;
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getMIMEType()
  {
    return mime_type_;
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getNameMapping()
  {
    return name_mapping_;
  }
  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getIOMethod()
  {
    return io_method_;
  }      
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode()
  {
    return io_mode_;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();
    
    buffer.append("DMDServiceOffer{provider=");
    buffer.append(provider_class_);
    buffer.append(",mime=");
    buffer.append(mime_type_);
    buffer.append(",mapping=");
    buffer.append(name_mapping_);
    buffer.append(",method=");
    buffer.append(io_method_);
    buffer.append(",mode=");
    buffer.append(io_mode_);
    
    return buffer.toString();
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean matchesStore(DMDServiceOffer offer)
  {
    boolean result = true;
    result = result &&
             (offer.provider_class_ == null 
              || offer.provider_class_.equals(provider_class_));
    
    result = result && 
             (offer.mime_type_ == null || offer.mime_type_.equals(mime_type_));
             
    result = result &&
             (offer.io_method_ == null || offer.io_method_.equals(io_method_));
             
    result = result &&
             ((offer.io_mode_ & io_mode_) == offer.io_mode_);
    
    return result;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean matchesMapper(DMDServiceOffer offer)
  {
    boolean result = true;
    
    result = result &&
             (offer.provider_class_ == null 
              || offer.provider_class_.equals(provider_class_));
    
    result = result && 
             (offer.mime_type_ == null || offer.mime_type_.equals(mime_type_));
             
    result = result &&
             (offer.name_mapping_ == null || offer.name_mapping_.equals(name_mapping_));
    
    return result;
  }
  
  protected String provider_class_;
  protected String mime_type_;
  protected String name_mapping_;
  protected String io_method_;
  protected int    io_mode_;  
}
