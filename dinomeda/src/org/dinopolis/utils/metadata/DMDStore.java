///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDStore.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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


package org.dinopolis.utils.metadata;

// Java imports

// external packages

// local packages

/**
 * @author: Kevin Krammer <voyager@sbox.tugraz.at>
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The base interface for handlers doing the IO operations on the
 * data media, e.g File, Stream, Database.
 *
 */

public interface DMDStore extends DMDHandler
{
  //---------------------------------------------------------------
  /**
   * Gets the metadata for a specified "path".
   * If the path applies to more than one element, it returns the first.
   * This does not require an IO operation.
   *
   * @param path the stores internal name of the element in form of a
   *        file path
   *
   * @return the node holding the requested metadata or null
   */
  public DMDNode getElement(String path);

  //---------------------------------------------------------------
  /**
   * Puts the metadata for a specified "path" into the stores memory.
   * This does not require an IO operation.
   *
   * @param node the node holding the metadata and the path
   */
  public void setElement(DMDNode node);

  //---------------------------------------------------------------
  /**
   * Gets an iterator to the first element in the given path.
   */
  public DMDNodeIterator get(String path);

  //---------------------------------------------------------------
  /**
   * Sets all elements of the iterator to their paths-
   */
  public void set(DMDNodeIterator iterator);
}
