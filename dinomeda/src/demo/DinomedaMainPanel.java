///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaMainPanel.java,v 1.2 2003/03/02 19:42:20 krake Exp $
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

public class DinomedaMainPanel extends JPanel
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaMainPanel()
  {
    panels_ = new DataPanel[DinomedaMapper.FIELD_COUNT];

    panels_[0] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[0]);
    panels_[1] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[1]);
    panels_[2] = new DateDataPanel(DinomedaMapper.FIELD_NAMES[2]);
    panels_[3] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[3]);
    panels_[4] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[4]);
    panels_[5] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[5]);
    panels_[6] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[6]);
    panels_[7] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[7]);
    panels_[8] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[8]);
    panels_[9] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[9]);
    panels_[10] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[10]);
    panels_[11] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[11]);
    panels_[12] = new StringDataPanel(DinomedaMapper.FIELD_NAMES[12]);

    setLayout(new GridLayout(panels_.length, 1));

    for (int count = 0; count < panels_.length; ++count)
    {
      add(panels_[count]);
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setMapper(DinomedaMapper mapper)
  {
    mapper_ = mapper;
    dataFromMapper();
  }  

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void dataToMapper()
  {
    if (mapper_ == null)
    {
      return;
    }
    
    for (int count = 0; count < panels_.length; ++count)
    {
      if (mapper_.getMaxCount(count) > 0)
      {
        mapper_.set(count, panels_[count].getData());
      }
    }    
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void dataFromMapper()
  {
    if (mapper_ == null)
    {
      return;
    }
    
    for (int count = 0; count < panels_.length; ++count)
    {
      panels_[count].setMaxCount(mapper_.getMaxCount(count));
      panels_[count].setData(mapper_.get(count));
    }    
  }
  
  protected DataPanel[] panels_;
  protected DinomedaMapper mapper_ = null;
}
