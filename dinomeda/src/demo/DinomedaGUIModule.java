///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaGUIModule.java,v 1.6 2003/04/24 09:08:49 osma Exp $
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
import java.io.File;
import java.io.IOException;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JLabel;

// external packages

// local packages
import org.dinopolis.util.metadata.DMDHandler;
import org.dinopolis.util.metadata.DMDJobList;
import org.dinopolis.util.metadata.DMDJobListItem;
import org.dinopolis.util.metadata.DMDMapper;
import org.dinopolis.util.metadata.dinomeda.DinomedaMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaGUIModule implements GUIModule, IOActionListener
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaGUIModule()
  {
    main_panel_ = new DinomedaMainPanel();
    option_panel_ = new DinomedaIOOptions(); 
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setApplication(DinomedaApplication application)
  {
    application_ = application;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean setFile(File file)
  {
    mapper_ = null;
    if (application_ == null || file == null)
    {
      return false;
    }
    
    DMDMapper mapper = application_.getMappedFileStore(file, "Dinomeda");
    if (mapper == null)
    {
      String message = "Trader could not create mapped file store for " + file;
      JOptionPane.showMessageDialog(main_panel_, message, 
        "Retrieving store and mapper", JOptionPane.ERROR_MESSAGE);
      return false; 
    }
    
    if (!(mapper instanceof DinomedaMapper))
    {
      String message = "Mapper returned by trader is not a Dinomeda mapper";
      JOptionPane.showMessageDialog(main_panel_, message, 
        "Retrieving store and mapper", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    
    mapper_ = (DinomedaMapper) mapper;
    
    main_panel_.setMapper(mapper_);
    option_panel_.setMapper(mapper_);
    
    return true;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public JComponent getMainPanel()
  {
    return main_panel_;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public JComponent getIOOptionPanel()
  {
    return option_panel_;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public IOActionListener getIOActionListener()
  {
    return this;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode()
  {
    if (mapper_ != null)
    {
      return mapper_.getIOMode();
    }
    
    return DMDHandler.NO_IO;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void read()
  {
    System.out.println("Read");
    if (mapper_ != null)
    {
      try
      {
        main_panel_.dataToMapper();
        mapper_.readMetaData(createJobList());
        main_panel_.dataFromMapper();
      }
      catch (IOException exception)
      {
        exception.printStackTrace(System.err);
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void update()
  {
    System.out.println("Update");
    if (mapper_ != null)
    {
      try
      {
        main_panel_.dataToMapper();
        mapper_.updateMetaData(createJobList());
        main_panel_.dataFromMapper();
      }
      catch (IOException exception)
      {
        exception.printStackTrace(System.err);
      }
    }
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void write()
  {
    System.out.println("Write");
    if (mapper_ != null)
    {
      try
      {
        main_panel_.dataToMapper();
        mapper_.writeMetaData(createJobList());
        main_panel_.dataFromMapper();
      }
      catch (IOException exception)
      {
        exception.printStackTrace(System.err);
      }
    }
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected DMDJobList createJobList()
  {
    DMDJobList job_list = new DMDJobList();
    
    if (option_panel_.useAllFields())
    {
      job_list.add(new DMDJobListItem("/"));
    }
    else
    {
      for (int count = 0; count < DinomedaMapper.FIELD_COUNT; ++count)
      {
        if (option_panel_.useField(count))
        {
          job_list.add(
            new DMDJobListItem("/" + DinomedaMapper.FIELD_NAMES[count].toLowerCase()));
        }
      }
    }

    return job_list;
  }

  protected DinomedaApplication application_ = null;
  protected DinomedaMapper mapper_ = null;
  protected DinomedaMainPanel main_panel_ = null;
  protected DinomedaIOOptions option_panel_ = null;
}
