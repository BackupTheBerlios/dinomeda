///////////////////////////////////////////////////////////////////////////////
//
// $Id: StringDataPanel.java,v 1.1 2003/02/27 21:56:15 krake Exp $
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
import javax.swing.JComboBox;
import javax.swing.JOptionPane;

// external packages

// local packages
import org.dinopolis.utils.metadata.dinomeda.DinomedaMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class StringDataPanel extends DataPanel
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public StringDataPanel(String name)
  {
    super(name);
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void editElement()
  {
    if (getCount() == 0)
    {
      return;
    }
    
    int index = data_combo_.getSelectedIndex();
    String value = (String) data_.get(index);
    
    String result = (String) 
      JOptionPane.showInputDialog(this, "Edit the value", "Edit current value",
                                  JOptionPane.PLAIN_MESSAGE, null, null, value);  
    if (result != null)
    {
      data_.set(index, result);
      updateDataCombo(index);
      updateCountLabel();
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void addElement()
  {
    if (getCount() == max_count_)
    {
      return;
    }
    
    String result = (String) 
      JOptionPane.showInputDialog(this, "Edit the value", "Add a new value",
                                  JOptionPane.PLAIN_MESSAGE, null, null, "");  
    if (result != null)
    {
      int index = getCount();
      data_.add(result);
      updateDataCombo(index);
      updateCountLabel();
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void removeElement()
  {
    if (getCount() == 0)
    {
      return;
    }
    
    int index = data_combo_.getSelectedIndex();
    data_.remove(index);
    index--;
    if (index < 0)
    {
      index = 0;
    }
    updateDataCombo(index);
    updateCountLabel();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected String elementToString(Object element)
  {
    return element.toString();
  }
}
