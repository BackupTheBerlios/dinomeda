///////////////////////////////////////////////////////////////////////////////
//
// $Id: DataPanel.java,v 1.2 2003/03/02 19:59:08 krake Exp $
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
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

// external packages

// local packages
import org.dinopolis.util.metadata.dinomeda.DinomedaMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public abstract class DataPanel extends JPanel implements ActionListener
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DataPanel(String name)
  {
    max_count_ = 0;
    data_ = new ArrayList();
    
    name_label_ = new JLabel(name);
    count_label_ = new JLabel("0/0", SwingConstants.CENTER);
    updateCountLabel();
    JPanel label_panel = new JPanel();
    label_panel.setLayout(new GridLayout());
    label_panel.add(name_label_);
    label_panel.add(count_label_);
        
    data_combo_ = new JComboBox();
    
    button_edit_ = new JButton("Edit");
    button_edit_.addActionListener(this);
    button_add_ = new JButton("Add");
    button_add_.addActionListener(this);
    button_remove_ = new JButton("Remove");
    button_remove_.addActionListener(this);
    JPanel button_panel = new JPanel();
    button_panel.add(button_edit_);
    button_panel.add(button_add_);
    button_panel.add(button_remove_);
      
    setLayout(new GridLayout(1, 3));
    add(label_panel);
    add(data_combo_);
    add(button_panel);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setData(Object[] data)
  {
    original_data_ = data;
    data_.clear();
    for (int count = 0; count < data.length; ++count)
    {
      data_.add(data[count]);
    }
    updateDataCombo(0);
    updateCountLabel();
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public Object[] getData()
  {
    return data_.toArray(original_data_);  
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void actionPerformed(ActionEvent event)
  {
    Object source = event.getSource();
    if (source == button_edit_)
    {
      editElement();
    }
    else if (source == button_add_)
    {
      addElement();
    }
    else if (source == button_remove_)
    {
      removeElement();
    }
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setMaxCount(int max)
  {
    max_count_ = max;
    updateCountLabel();
    boolean enable = (max_count_ > 0);
    data_combo_.setEnabled(enable);
    button_edit_.setEnabled(enable);
    button_add_.setEnabled(enable);
    button_remove_.setEnabled(enable);
  }
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected abstract void editElement();
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected abstract void addElement();
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected abstract void removeElement();
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected abstract String elementToString(Object element);
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void updateCountLabel()
  {
    count_label_.setText(String.valueOf(getCount())
                         + "/" + String.valueOf(max_count_)); 
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void updateDataCombo(int select_index)
  {
    data_combo_.removeAllItems();
    if (data_ != null)
    {
      for (int count = 0; count < data_.size(); ++count)
      {
        data_combo_.addItem(elementToString(data_.get(count)));
      }
      if (select_index >= 0 && select_index < data_.size())
      {
        data_combo_.setSelectedIndex(select_index);
      }
    }
    
    int item_count = data_combo_.getItemCount();
    if (item_count == 0)
    {
      data_combo_.setEnabled(false);
      button_edit_.setEnabled(false);
      button_add_.setEnabled(max_count_ > 0);
      button_remove_.setEnabled(false);
    }
    else if (item_count < max_count_)
    {
      data_combo_.setEnabled(true);
      button_edit_.setEnabled(true);
      button_add_.setEnabled(true);
      button_remove_.setEnabled(true);
    }
    else
    {
      data_combo_.setEnabled(true);
      button_edit_.setEnabled(true);
      button_add_.setEnabled(false);
      button_remove_.setEnabled(true);
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected int getCount()
  {
    if (data_ == null)
    {
      return 0;
    }
  
    return data_.size();
  } 
    
  protected JLabel name_label_;
  protected JLabel count_label_;
  protected JComboBox data_combo_;
  protected JButton button_edit_;
  protected JButton button_add_;
  protected JButton button_remove_;
  
  protected int max_count_;
  protected ArrayList data_ = null;
  protected Object[] original_data_ = null;
}
