///////////////////////////////////////////////////////////////////////////////
//
// $Id: StatusPanel.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class StatusPanel extends JPanel
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public StatusPanel()
  {
    setBorder(BorderFactory.createLoweredBevelBorder());
    setLayout(new BorderLayout());
    message_label_ = new JLabel();
    add(message_label_, BorderLayout.WEST);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setStatusText(String text)
  {
    message_label_.setText(text);
  }
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void clearStatusText()
  {
    message_label_.setText("Ready.");
  }
     
  protected JLabel message_label_;
}