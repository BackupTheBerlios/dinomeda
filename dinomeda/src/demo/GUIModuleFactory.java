///////////////////////////////////////////////////////////////////////////////
//
// $Id: GUIModuleFactory.java,v 1.2 2003/02/28 13:00:53 krake Exp $
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

// Java imports

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDHandler;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class GUIModuleFactory
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public static GUIModule createModule(String name)
  {
    if (name == null)
    {
      return null;
    }
    
    if (name.equals("Dinomeda"))
    {
      return new DinomedaGUIModule();
    }
    
    return null;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public static String[] getModuleNames()
  {
    return new String[]{"Dinomeda"}; 
  }
}
