///////////////////////////////////////////////////////////////////////////////
//
// $Id:
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

// Java imports
import java.io.InputStream;
import java.io.OutputStream;

// external packages

// local packages

/**
 * @author: Kevin Krammer <voyager@sbox.tugraz.at>
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 */

/**
 * The base interface for all stream stores.
 * It declares the basic stream operations.
 */

public interface DMDStreamStore extends DMDStore
{
  //---------------------------------------------------------------
  /**
   * Returns the inputstream which contains the original metadata
   * @return the inputstream which contains the original metadata
   */
  public InputStream getInputStream();

  //---------------------------------------------------------------
  /**
   * Sets the inputstream which contains the original metadata
   * @param stream which contains the original metadata
   */
  public void setInputStream(InputStream stream);

  //---------------------------------------------------------------
  /**
   * Returns the outputstream which contains the modified metadata
   * @return the outputstream which contains the modified metadata
   */
  public OutputStream getOutputStream();

  //---------------------------------------------------------------
  /**
   * Sets the outstream which contains the modified metadata
   * @param stream which contains the modified metadata
   */
  public void setOutputStream(OutputStream stream);
}
