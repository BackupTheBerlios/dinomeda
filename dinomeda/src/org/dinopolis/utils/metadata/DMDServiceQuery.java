///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceQuery.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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


package org.dinopolis.utils.metadata;

// Java imports

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DMDServiceQuery
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDServiceQuery()
  {
    mime_type_ = null;
    name_mapping_ = null;
    io_method_ = null;
    io_mode_ = DMDHandler.NO_IO; // No io mode required, matches any IO mode
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
  public String getMIMEType()
  {
    return mime_type_;
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
  public String getNameMapping()
  {
    return name_mapping_;
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
  public String getIOMethod()
  {
    return io_method_;
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
    
    buffer.append("DMDServiceQuery{mime=");
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
  public boolean matchesStore(DMDServiceQuery query)
  {
    boolean result = true;
    
    result = result && 
             (query.mime_type_ == null || query.mime_type_.equals(mime_type_));
             
    result = result &&
             (query.io_method_ == null || query.io_method_.equals(io_method_));
             
    result = result &&
             ((query.io_mode_ & io_mode_) == query.io_mode_);
    
    return result;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean matchesMapper(DMDServiceQuery query)
  {
    boolean result = true;
    
    result = result && 
             (query.mime_type_ == null || query.mime_type_.equals(mime_type_));
             
    result = result &&
             (query.name_mapping_ == null || query.name_mapping_.equals(name_mapping_));
    
    return result;
  }
  
  protected String mime_type_;
  protected String name_mapping_;
  protected String io_method_;
  protected int    io_mode_;  
}
