///////////////////////////////////////////////////////////////////////////////
//
// $Id: IOButtonPanel.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class IOButtonPanel extends JPanel implements ActionListener
{
  public static final int READ   = 1;
  public static final int UPDATE = 2;
  public static final int WRITE  = 4;

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public IOButtonPanel()
  {
    io_listeners_ = new Vector();
    
    button_read_ = new JButton("Read");
    button_read_.addActionListener(this);
    
    button_update_ = new JButton("Update");
    button_update_.addActionListener(this);
  
    button_write_ = new JButton("Write");
    button_write_.addActionListener(this);
    
    setLayout(new GridLayout(1, 3));
    insets_ = new Insets(2,0,0,0);
    setBorder(BorderFactory.createLoweredBevelBorder());
    
    add(button_read_);
    add(button_update_);
    add(button_write_);    
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public Insets getInsets()
  {
    return insets_;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setIOMode(int mode)
  {
    boolean read = (mode & READ) != 0;
    boolean update = (mode & UPDATE) != 0;
    boolean write = (mode & WRITE) != 0;

    button_read_.setEnabled(read);    
    button_update_.setEnabled(update);    
    button_write_.setEnabled(write);    
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void addIOActionListener(IOActionListener listener)
  {
    if (listener != null)
    {
      io_listeners_.add(listener);
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void removeIOActionListener(IOActionListener listener)
  {
    if (listener != null)
    {
      io_listeners_.remove(listener);
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void actionPerformed(ActionEvent event)
  {
    Object source = event.getSource();
    if (source == button_read_)
    {
      for (int count = 0; count < io_listeners_.size(); ++count)
      {
        IOActionListener listener = (IOActionListener) io_listeners_.elementAt(count);
        if (listener !=  null)
        {
          listener.read();
        }
      }
    }
    else if (source == button_update_)
    {
      for (int count = 0; count < io_listeners_.size(); ++count)
      {
        IOActionListener listener = (IOActionListener) io_listeners_.elementAt(count);
        if (listener !=  null)
        {
          listener.update();
        }
      }
    }
    else if (source == button_write_)
    {
      for (int count = 0; count < io_listeners_.size(); ++count)
      {
        IOActionListener listener = (IOActionListener) io_listeners_.elementAt(count);
        if (listener !=  null)
        {
          listener.write();
        }
      }
    }
  }

  protected Insets insets_;
    
  protected JButton button_read_;
  protected JButton button_update_;
  protected JButton button_write_;
  
  protected Vector io_listeners_;
}
