///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDHandler.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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
   * Method description
   */
  public int read() throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int read(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int read(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update() throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write() throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write(String path) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write(DMDJobList joblist) throws IOException;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getMIMEType();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode();
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
//  public DMDJobList createJobList(String item);
}
