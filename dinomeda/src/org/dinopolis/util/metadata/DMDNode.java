///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNode.java,v 1.2 2003/03/05 09:03:52 osma Exp $
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

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.2.0
 */

/**
 * The base class for data nodes holding metadata.
 */

public class DMDNode
{
  public static final short UNDEFINED_NODE = 0;
  public static final short TEXT_NODE = 1;
  public static final short NUMBER_NODE = 2;

  //---------------------------------------------------------------
  /**
   * Constructs an empty DMDNode
   */
  public DMDNode()
  {
  }
  
  //---------------------------------------------------------------
  /**
   * Constructs an empty DMDNode with a path and a name
   * @param path the path of the metadata
   * @param name the name of the metadata
   */
  public DMDNode(String path, String name)  
  {
    setPath(path);
    setName(name);    
  }

  //---------------------------------------------------------------
  /**
   * Sets th path of the DMDNode
   * @param path the path of the metadata
   */
  public void setPath(String path) 
  {
    path_ = path;
  }
  
  //---------------------------------------------------------------
  /**
   * Sets the name of the DMDNode
   * @param name the name of the metadata
   */
  public void setName(String name) {
    name_ = name;
  }
    
  //---------------------------------------------------------------
  /**
   * Sets the name and the path of the metadata
   * @param name the name of the metadata
   * @param path the path of the metadata
   */
  public void setName(String path, String name) {
    setPath(path);
    setName(name);
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the path of the metadata
   * @return the path of the metadata
   */
  public String getPath()
  {
    return path_;
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the name of the metadata
   * @return the name of the metadata
   */
  public String getName()
  {
    return name_;
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the type of the DMDNode
   * @return the type of the DMDNode
   */
  public short getNodeType()  
  {
    return nodetype_;
  }
    
  //---------------------------------------------------------------
  /**
   * Returns the string representation of the DMDNode
   * @return the string representation of the DMDNode
   */
  public String toString()
  {
    return super.toString();
  }
  
  //---------------------------------------------------------------
  /**
   * Returns true if the DMDNode contains no data
   * @return true if the DMDNode contains no data
   */
  public boolean isNull()
  {
    return isnull_;
  }
  
  protected boolean isnull_ = true;
  protected String name_ = null;
  protected String path_ = null;
  protected short nodetype_ = UNDEFINED_NODE;  
}
