///////////////////////////////////////////////////////////////////////////////
//
// $Id: GUIModule.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.io.File;
import javax.swing.JComponent;

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDHandler;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public interface GUIModule
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setApplication(DinomedaApplication application);
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean setFile(File file);

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public JComponent getMainPanel();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public JComponent getIOOptionPanel();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public IOActionListener getIOActionListener();
  
    //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode();
}