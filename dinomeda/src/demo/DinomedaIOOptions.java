///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaIOOptions.java,v 1.2 2003/03/02 19:42:20 krake Exp $
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
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

// external packages

// local packages
import org.dinopolis.utils.metadata.dinomeda.DinomedaMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaIOOptions extends JPanel implements ActionListener
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaIOOptions()
  {
    checkboxes_ = new JCheckBox[DinomedaMapper.FIELD_COUNT];

    for (int count = 0; count < DinomedaMapper.FIELD_COUNT; ++count)
    {
      checkboxes_[count] = new JCheckBox(DinomedaMapper.FIELD_NAMES[count]);
    }

    button_all_ = new JButton("Select all");
    button_all_.addActionListener(this);
    button_none_ = new JButton("Clear all");
    button_none_.addActionListener(this);
    
    
    int rows = checkboxes_.length / 2;
    rows += checkboxes_.length % 2; // if the number is odd we need another row
    rows += 1; // for the buttons
    
    setLayout(new GridLayout(rows, 2));
    insets_ = new Insets(5, 10, 5, 10);

    for (int count = 0; count < checkboxes_.length; ++count)
    {
      add(checkboxes_[count]);
    }
    
    // odd number of checkboxes, need a dummy element in the second column
    if ((checkboxes_.length % 2) == 1)
    {
      add(new JPanel());
    }
    
    add(button_all_);
    add(button_none_);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setMapper(DinomedaMapper mapper)
  {
    mapper_ = mapper;
    
    for (int count = 0; count < checkboxes_.length; ++count)
    {
      checkboxes_[count].setSelected(false);
      boolean disable = mapper_ == null || mapper_.getMaxCount(count) == 0;
      checkboxes_[count].setEnabled(!disable);
    }    
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean useField(int field)
  {
    if (field < 0 || field >= checkboxes_.length)
    {
      return false;
    }
    
    return checkboxes_[field].isSelected();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean useAllFields()
  {
    boolean result = true;
    
    for (int count = 0; count < checkboxes_.length; ++count)
    {
      result = result && checkboxes_[count].isSelected();
    }
    
    return result;    
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
  public void actionPerformed(ActionEvent event)
  {
    Object source = event.getSource();

    if (source == button_all_)
    {
      for (int count = 0; count < checkboxes_.length; ++count)
      {
        if (checkboxes_[count].isEnabled())
        {
          checkboxes_[count].setSelected(true);
        }
      }
    }
    else if (source == button_none_)
    {
      for (int count = 0; count < checkboxes_.length; ++count)
      {
        checkboxes_[count].setSelected(false);
      }
    }
  }
   
  protected Insets insets_;
  protected JCheckBox[] checkboxes_;
  protected JButton button_all_;
  protected JButton button_none_;
  protected DinomedaMapper mapper_;
}
