///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaSwingApplication.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.io.IOException;
import java.util.Properties;

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDTrader;
import org.dinopolis.utils.metadata.dinomeda.DinomedaTrader;
import org.dinopolis.utils.metadata.dinomeda.DinomedaConfigurator;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaSwingApplication extends DinomedaApplication
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaSwingApplication(String[] args)
  {
    super(args);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void run()
  {
    DinomedaMainWindow main_window = new DinomedaMainWindow(this);
    
    main_window.setVisible(true);
    
    main_window.configureApplication();
  }
}
