///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceQuery.java,v 1.2 2003/02/28 13:00:53 krake Exp $
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


package org.dinopolis.utils.metadata;

// Java imports

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 *
 * Class or interface description (mandatory)
 */

public class DMDServiceQuery extends DMDServiceOffer
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDServiceQuery()
  {
    super(null, null, null, null, DMDHandler.NO_IO);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */

  public void setMIMEType(String mime_type)
  {
    mime_type_ = mime_type;
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setNameMapping(String mapping)
  {
    name_mapping_ = mapping;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setIOMethod(String io_method)
  {
    io_method_ = io_method;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setIOMode(int mode_flags)
  {
    io_mode_ = mode_flags;
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
}
