///////////////////////////////////////////////////////////////////////////////
//
// $Id: IOActionListener.java,v 1.1 2003/02/27 21:56:14 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package demo;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public interface IOActionListener
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void read();
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void update();
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void write();
}
