///////////////////////////////////////////////////////////////////////////////
//
// $Id: DateDataPanel.java,v 1.2 2003/03/02 19:42:20 krake Exp $
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.Date;
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

public class DateDataPanel extends DataPanel
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DateDataPanel(String name)
  {
    super(name);
    
    formatter_ = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
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
    Date value = (Date) data_.get(index);

    String value_text = formatter_.format(value);
    String result_text = (String)    
      JOptionPane.showInputDialog(this, "Date in ISO8601", "Edit current value",
                                  JOptionPane.PLAIN_MESSAGE, null, null, value_text);  
    
    Date result = null;
    if (result_text != null)
    {
      try
      {
        result = formatter_.parse(result_text);
      }
      catch (ParseException exception)
      {
        JOptionPane.showMessageDialog(this, exception.getMessage(), 
          "Loading application configuration", JOptionPane.ERROR_MESSAGE);
        exception.printStackTrace(System.err);
      }
    }
    
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

    Date value = new Date();
    String value_text = formatter_.format(value);
    String result_text = (String)    
      JOptionPane.showInputDialog(this, "Date in ISO8601", "Edit current value",
                                  JOptionPane.PLAIN_MESSAGE, null, null, value_text);  
    
    Date result = null;
    if (result_text != null)
    {
      try
      {
        result = formatter_.parse(result_text);
      }
      catch (ParseException exception)
      {
        JOptionPane.showMessageDialog(this, exception.getMessage(), 
          "Loading application configuration", JOptionPane.ERROR_MESSAGE);
        exception.printStackTrace(System.err);
      }
    }
        
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
    if (!(element instanceof Date))
    {
      return element.toString();
    }
    
    Date date = (Date) element;
    return formatter_.format(date);
  }
  
  protected DateFormat formatter_ = null;
}
