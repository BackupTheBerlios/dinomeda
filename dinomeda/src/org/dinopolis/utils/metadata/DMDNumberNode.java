///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNumberNode.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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
public class DMDNumberNode extends DMDNode
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDNumberNode()
  {
    nodetype_ = NUMBER_NODE;
  }
  
  public DMDNumberNode(String path, String name)
  {
    super(path, name);
    nodetype_ = NUMBER_NODE;
  }
  
  public DMDNumberNode(String path, String name, Number number)
  {
    super(path, name);
    nodetype_ = NUMBER_NODE;
    set(number);
  }
  
  public void set(Number number)
  {
    number_ = number;
    isnull_ = false;
  }
  
  public Number get()
  {
    return number_;
  }
  
  public String toString() 
  {
    return number_.toString();
  }

  
  protected Number number_ = null;
}
