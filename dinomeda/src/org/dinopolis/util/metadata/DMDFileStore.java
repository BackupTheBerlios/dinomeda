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
import java.io.File;
import java.io.FileNotFoundException;

// external packages

// local packages

/**
 * @author: Kevin Krammer <voyager@sbox.tugraz.at>
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 */

/**
 * The base interface for all file stores.
 * It declares the basic file operations.
 */

public interface DMDFileStore extends DMDStore
{
  //---------------------------------------------------------------
  /**
   * Returns the file which contains the metadata
   * @return a file which contains the metadata
   */
  public File getFile();

  //---------------------------------------------------------------
  /**
   * Sets the file which is processed by the store
   * @param file which contains metadata
   */
  public void setFile(File file); 

  //---------------------------------------------------------------
  /**
   * Sets the filename of a file which is processed by the store
   * @param filename of a file which contains metadata
   * @throws FileNotFoundException if file not exists
   */
  public void setFileName(String file_name) throws FileNotFoundException;
}
