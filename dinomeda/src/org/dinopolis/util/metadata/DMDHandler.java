///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDHandler.java,v 1.2 2003/03/03 12:35:18 osma Exp $
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
import java.io.IOException;

/**
 * @author: Kevin Krammer <voyager@sbox.tugraz.at>
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The base interface for all service modules.
 * It declares the basic IO operations.
 */

public interface DMDHandler
{
  public static final int NO_IO    = 0;
  public static final int READ     = 1;
  public static final int UPDATE   = 2;
  public static final int WRITE    = 4;
  public static final int ALL_IO   = 7;

  //---------------------------------------------------------------
  /**
   * Reads all metadata, that has been read and changed, again.
   * @return the amount of metadata that was read.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int read() throws IOException;

  //---------------------------------------------------------------
  /**
   * Reads the metadata, that is specified by the given path.
   * @param path the path of the metadata.
   * @return the amount of metadata that was read.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int read(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Reads the metadata, that is specified by the given joblist.
   * @param joblist a list containing all the metadata that has to be read.
   * @return the amount of metadata that was read.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int read(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Updates all metadata, that has been changed.
   * @return the amount of metadata that was updated.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int update() throws IOException;

  //---------------------------------------------------------------
  /**
   * Updates the metadata, that is specified by the given path and 
   * has been changed.
   * @param path the path of the metadata.
   * @return the amount of metadata that was updated.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int update(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Updates the metadata, that is specified by the given joblist and 
   * has been changed.
   * @param joblist a list containing all the metadata that has to be updated.
   * @return the amount of metadata that was updated.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int update(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Writes all metadata, that has been read or changed and deletes the 
   * rest of the metadata.
   * @return the amount of metadata that was written.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int write() throws IOException;

  //---------------------------------------------------------------
  /**
   * Writes all metadata, that is specified by the given path and deletes 
   * the rest of the metadata.
   * @param path the path of the metadata.
   * @return the amount of metadata that was written.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int write(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Writes all metadata, that is specified by the given joblist and deletes 
   * the rest of the metadata.
   * @param path the path of the metadata.
   * @return the amount of metadata that was written.
   * @throws IOException if the metadata cannot be accessed.
   */
  public int write(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Returns the MIMEType of the file or stream containing the metadata
   * @return the MIMEType of the file or stream containing the metadata
   */
  public String getMIMEType();

  //---------------------------------------------------------------
  /**
   * Returns the handlers IOMode
   * @return the handlers IOMode
   */
  public int getIOMode();
  
}
