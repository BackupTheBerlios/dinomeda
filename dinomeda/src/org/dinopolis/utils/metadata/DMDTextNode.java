///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDTextNode.java,v 1.1 2003/02/27 21:56:11 krake Exp $
//
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2002
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
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DMDTextNode extends DMDNode
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDTextNode()
  {
    nodetype_ = TEXT_NODE;
  }
  
  public DMDTextNode(String path, String name, String text)
  {
    super(path, name);
    nodetype_ = TEXT_NODE;
    set(text);
  }
  
  public void set(String text)
  {
    text_ = text;
    isnull_ = (text == null);
  }
  
  public String get()
  {
    return text_;
  }
  
  public String toString() 
  {
    return text_;
  }
  
  
  protected String text_ = null;
}
