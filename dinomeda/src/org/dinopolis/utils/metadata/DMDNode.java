///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNode.java,v 1.2 2003/02/28 13:00:53 krake Exp $
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

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.2.0
 *
 * The base class for data nodes holding meta data.
 */

public class DMDNode
{
  public static final short UNDEFINED_NODE = 0;
  public static final short TEXT_NODE = 1;
  public static final short NUMBER_NODE = 2;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDNode()
  {
  }

  public DMDNode(String path, String name)  
  {
    setPath(path);
    setName(name);    
  }

  public void setPath(String path) 
  {
    path_ = path;
  }
  
  public void setName(String name) {
    name_ = name;
  }
    
  public void setName(String path, String name) {
    setPath(path);
    setName(name);
  }
  
  public String getPath()
  {
    return path_;
  }
  
  public String getName()
  {
    return name_;
  }
  
  public short getNodeType()  
  {
    return nodetype_;
  }
    
  public String toString()
  {
    return super.toString();
  }
  
  public boolean isNull()
  {
    return isnull_;
  }
  
  protected boolean isnull_ = true;
  protected String name_ = null;
  protected String path_ = null;
  protected short nodetype_ = UNDEFINED_NODE;  
}
