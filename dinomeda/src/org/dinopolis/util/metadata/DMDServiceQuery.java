///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceQuery.java,v 1.2 2003/03/04 23:00:41 krake Exp $
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

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 */

/**
 * The DMDServiceQuery is basically a DMDServiceOffer with
 * SET methods.
 * The intended use is querying a DMDTrader implementation for services.
 *
 * @see DMDTrader
 */

public class DMDServiceQuery extends DMDServiceOffer
{
  //---------------------------------------------------------------
  /**
   * Creates an empty query.
   * Empty means that class name, MIME type, name mapping and IO method
   * are null and IO mode is NO_IO.
   *
   * @see DMDHandler
   */
  public DMDServiceQuery()
  {
    super(null, null, null, null, DMDHandler.NO_IO);
  }
  
  //---------------------------------------------------------------
  /**
   * Sets the MIME type to query for.
   * It this is set to null, it will match any MIME type.
   *
   * @param mime_type the MIME type to match or null for any
   */

  public void setMIMEType(String mime_type)
  {
    mime_type_ = mime_type;
  }

  //---------------------------------------------------------------
  /**
   * Sets the name mapping to query for.
   * It this is set to null, it will match any name mapping.
   *
   * @param mapping the name mapping to match or null for any
   */
  public void setNameMapping(String mapping)
  {
    name_mapping_ = mapping;
  }

  //---------------------------------------------------------------
  /**
   * Sets the IO method to query for.
   * It this is set to null, it will match any IO method.
   *
   * @param io_method the IO method to match or null for any
   */
  public void setIOMethod(String io_method)
  {
    io_method_ = io_method;
  }

  //---------------------------------------------------------------
  /**
   * Sets the IO mode to query for.
   * It this is set to DMDHandler.NO_IO, it will match any IO method.
   *
   * @param io_mode the IO mode to match or NO_IO for any
   *
   * @see DMDHandler
   */
  public void setIOMode(int mode_flags)
  {
    io_mode_ = mode_flags;
  }

  //---------------------------------------------------------------
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
