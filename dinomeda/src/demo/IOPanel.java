///////////////////////////////////////////////////////////////////////////////
//
// $Id: IOPanel.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import javax.swing.JComponent;
import javax.swing.JPanel;

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class IOPanel extends JPanel
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public IOPanel()
  {
    setLayout(new BorderLayout());
    setBorder(BorderFactory.createRaisedBevelBorder());
    
    button_panel_ = new IOButtonPanel();
    add(button_panel_, BorderLayout.SOUTH);
    
    option_panel_ = new JPanel();
    option_panel_.setBorder(BorderFactory.createLoweredBevelBorder());
    add(option_panel_, BorderLayout.CENTER);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setIOOptions(JComponent options)
  {
    if (options_ != null)
    {
      option_panel_.remove(options_);
    }
    
    options_ = options;
    
    if (options_ != null)
    {
      option_panel_.add(options_);
    }
  }
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setIOMode(int mode)
  {
    button_panel_.setIOMode(mode);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void addIOActionListener(IOActionListener listener)
  {
    button_panel_.addIOActionListener(listener);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void removeIOActionListener(IOActionListener listener)
  {
    button_panel_.removeIOActionListener(listener);
  }
     
  protected IOButtonPanel button_panel_;
  protected JPanel option_panel_;
  
  protected JComponent options_ = null;
}
