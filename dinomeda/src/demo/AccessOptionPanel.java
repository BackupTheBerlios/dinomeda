///////////////////////////////////////////////////////////////////////////////
//
// $Id: AccessOptionPanel.java,v 1.5 2003/03/11 09:58:54 krake Exp $
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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.TreeSet;
import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.1
 *
 * Class or interface description (mandatory)
 */

public class AccessOptionPanel extends JPanel
  implements PropertyChangeListener, ItemListener

{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public AccessOptionPanel(DinomedaApplication application)
  {
    application_ = application;

    setLayout(new GridLayout(4, 1));
    setBorder(BorderFactory.createLoweredBevelBorder());
    insets_ = new Insets(0, 10, 10, 10);

    add(new JLabel("MIME type"));

    mime_label_ = new JLabel();
    add(mime_label_);
    
    add(new JLabel("Name mapping"));
    
    mapping_combo_ = new JComboBox();
    mapping_combo_.addItemListener(this);
    add(mapping_combo_);
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
  public void propertyChange(PropertyChangeEvent event)
  {
    mapping_combo_.removeAllItems();
    mime_label_.setText("");

    String property_name = event.getPropertyName();
    if (JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(property_name))
    {
      File file = (File)event.getNewValue();
      if (file != null)
      {
        last_selected_mapping_ = null;
        String mime_type = MIME.getMIMETypeForFile(file);
        if (mime_type != null)
        {
          mime_label_.setText(mime_type);

          String[] mappings =
            application_.getNameMappingsForFile(file);

          if (mappings != null && mappings.length > 0)
          {
            String[] guis = GUIModuleFactory.getModuleNames();
            if (guis.length > 0)
            {
              for (int count = 0; count < mappings.length; ++count)
              {
                for (int count2 = 0; count2 < guis.length; ++count2)
                {
                  if (guis[count2].equals(mappings[count]))
                  {
                    mapping_combo_.addItem(mappings[count]);
                    break;
                  }
                }
              }
            }
          }
          last_selected_mapping_ = (String) mapping_combo_.getSelectedItem();
        }
      }
    }
    mapping_combo_.setEnabled(mapping_combo_.getItemCount() > 0);
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void itemStateChanged(ItemEvent event)
  {
    if (event.getStateChange() == ItemEvent.SELECTED)
    {
      last_selected_mapping_ = (String) event.getItem();
    }
  }

  public String getSelectedNameMapping()
  {
    return last_selected_mapping_;
  }

  protected Insets insets_;
  protected DinomedaApplication application_;
  protected JLabel mime_label_;
  protected JComboBox mapping_combo_;
  protected String last_selected_mapping_;
}
