///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNumberNode.java,v 1.2 2003/03/05 09:03:52 osma Exp $
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


package org.dinopolis.util.metadata;

// Java imports

// external packages

// local packages

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 */

/**
 * Class for data nodes holding numeric metadata.
 */
public class DMDNumberNode extends DMDNode
{
  //---------------------------------------------------------------
  /**
   * Constructs an empty DMDNumberNode
   */
  public DMDNumberNode()
  {
    nodetype_ = NUMBER_NODE;
  }
  
  //---------------------------------------------------------------
  /**
   * Constructs a DMDNumberNode with path, name and content
   * @param path the path of the metadata
   * @param name the name of the metadata
   * @param number the number representation of the metadata
   */
  public DMDNumberNode(String path, String name, Number number)
  {
    super(path, name);
    nodetype_ = NUMBER_NODE;
    set(number);
  }
  
  //---------------------------------------------------------------
  /**
   * Sets the content of the DMDNumberNode
   * @param the number representation of the metadata
   */
  public void set(Number number)
  {
    number_ = number;
    isnull_ = false;
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the content of the DMDNumberNode
   * @return the content of the DMDNumberNode
   */
  public Number get()
  {
    return number_;
  }
  
  //---------------------------------------------------------------
  /**
   * Returens the string representation of the DMDNumberNode
   * @return the string representation of the DMDNumberNode
   */
  public String toString() 
  {
    return number_.toString();
  }

  
  protected Number number_ = null;
}
