///////////////////////////////////////////////////////////////////////////////
//
// $Id:
//
// Copyright: Martin Oswald <ossi1@sbox.tugraz.at>, 2003
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
import java.util.*;

// external packages

// local packages

/**
 * @author Martin Oswald <ossi1@sbox.tugraz.at>
 * @version 0.1.0
 *//**
 * Iterator for DMDNodes
 */

public class DMDNodeIterator implements java.util.Iterator
{
  /** 
   * Constructs a DMDNodeIterator
   * @param iterator iterator of DMDNodes
   */  public DMDNodeIterator(Iterator iterator)
  {
    iterator_ = iterator;
  }
  
  /** 
   * Returns the next DMDNode in the iteration
   */  public DMDNode nextNode()
  {
    return (DMDNode)iterator_.next();
  }
 
  /** 
   * Removes the current DMDNode in the iteration
   */  public void remove()
  {
    iterator_.remove();
  }
    /**
   * Returns true if the iteration has more elements.
   */  
  public boolean hasNext()
  {
    return iterator_.hasNext();
  }
  
  /** 
   * Returns the next DMDNode in the iteration
   */  public Object next()
  {
    return iterator_.next();
  }
  
  protected Iterator iterator_; 
}
