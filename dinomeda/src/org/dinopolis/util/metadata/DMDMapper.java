///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDMapper.java,v 1.1 2003/03/02 19:59:08 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
// Copyright: Martin Oswald <ossi1@sbox.tugraz.at>, 2002-2003
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2002-2003
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
 * @author: Kevin Krammer <voyager@sbox.tugraz.at>
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The base interface for handlers providing name mapping from the
 * internal namespace of a store to an external naming scheme
 * and vice versa.
 */

public interface DMDMapper extends DMDHandler
{
  //---------------------------------------------------------------
  /**
   * Sets a DMDStore instance
   *
   * @param store the store the mapper works with
   */
  public void setStore(DMDStore store);

  //---------------------------------------------------------------
  /**
   * Gets the DMDStore instance the mapper is currently working with.
   *
   * @return the mapper's current store
   */
  public DMDStore getStore();

  //---------------------------------------------------------------
  /**
   * Returns the name of the naming scheme the mapper is mapping to/from.
   *
   * @return the name of the naming mapper's scheme
   */
  public String getNameMapping();  
}
