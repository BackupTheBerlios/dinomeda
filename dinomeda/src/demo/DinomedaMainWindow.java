///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaMainWindow.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.awt.Container;
import java.awt.ItemSelectable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import java.io.IOException;
import java.util.Properties;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileFilter;

// external packages

// local packages
import org.dinopolis.utils.metadata.DMDMapper;

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DinomedaMainWindow extends JFrame implements ActionListener, ChangeListener
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DinomedaMainWindow(DinomedaApplication app)
  {
    super("Dinomeda Demoapplication");
    application_ = app;
    
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    Container content_pane = getContentPane();
    content_pane.setLayout(new BorderLayout());
        
    status_panel_ = new StatusPanel();
    content_pane.add(status_panel_, BorderLayout.SOUTH);
    
    file_panel_ = new JTextField();
    file_panel_.setEditable(false);
    content_pane.add(file_panel_, BorderLayout.NORTH);
    file_panel_.setText("No file open");

    io_panel_ = new IOPanel();
    content_pane.add(io_panel_, BorderLayout.EAST);
    
    createMenu();
    
    setSize(640, 480);
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void configureApplication()
  {
    status_panel_.setStatusText("Loading application configuration...");
    try
    {
      application_.loadConfig();
    }
    catch (IOException io)
    {
      status_panel_.clearStatusText();
      JOptionPane.showMessageDialog(this, io.getMessage(), 
        "Loading application configuration", JOptionPane.WARNING_MESSAGE);
    }
  
    status_panel_.setStatusText("Configuring trader...");
    try
    {
      application_.configureTrader();
    }
    catch (IOException io)
    {
      status_panel_.clearStatusText();
      JOptionPane.showMessageDialog(this, io.getMessage(), 
        "Configuring trader", JOptionPane.WARNING_MESSAGE);
    }
    
    status_panel_.clearStatusText();
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void actionPerformed(ActionEvent event)
  {
    Object item = event.getSource();
    if (item == file_open_)
    {
      openFile();
    }
    else if (item == file_quit_)
    {
      quit();
    }
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void stateChanged(ChangeEvent event)
  {
    Object item = event.getSource();
    if (item == file_open_ && file_open_.isArmed())
    {
      status_panel_.setStatusText("Opens a new file");
    }
    else if (item == file_quit_ && file_quit_.isArmed())
    {
      status_panel_.setStatusText("Quits the application");
    }
    else
    {
      status_panel_.clearStatusText();
    }
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void createMenu()
  {
    JMenuBar menubar = new JMenuBar();
    setJMenuBar(menubar);
    
    JMenu file_menu = new JMenu("File");
    menubar.add(file_menu);
    file_open_ = new JMenuItem("Open...", KeyEvent.VK_O);
    file_open_.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
    file_open_.addChangeListener(this);
    file_open_.addActionListener(this);
    file_menu.add(file_open_);
      
    file_menu.addSeparator();
    
    file_quit_ = new JMenuItem("Quit", KeyEvent.VK_Q);
    file_quit_.setAccelerator(
      KeyStroke.getKeyStroke(KeyEvent.VK_Q, ActionEvent.CTRL_MASK));
    file_quit_.addChangeListener(this);
    file_quit_.addActionListener(this);
    file_menu.add(file_quit_);
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void openFile()
  {
    status_panel_.setStatusText("Opening file...");
    if (file_chooser_ == null)
    {
      file_chooser_ = new JFileChooser();
      
      FileFilter all_files = FileFilters.createAllSupportedFilesFilter(application_);
      file_chooser_.addChoosableFileFilter(all_files);
      
      FileFilter[] filters = FileFilters.createMIMEFileFilters(application_);
      for (int count = 0; count < filters.length; ++count)
      {
        file_chooser_.addChoosableFileFilter(filters[count]);
      }
      file_chooser_.setFileFilter(all_files);
      
      file_option_panel_ = new AccessOptionPanel(application_);
      file_chooser_.setAccessory(file_option_panel_);
      file_chooser_.addPropertyChangeListener(file_option_panel_);      
    }
    
    if (file_chooser_.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
    {
      if (gui_module_ != null)
      {
          JComponent main_panel = gui_module_.getMainPanel();
          remove(main_panel);
                  
          io_panel_.setIOOptions(null);
          io_panel_.removeIOActionListener(gui_module_.getIOActionListener());
      }
      
      status_panel_.setStatusText("Creating GUI...");
      String mapping = file_option_panel_.getSelectedNameMapping();
      gui_module_ = GUIModuleFactory.createModule(mapping);
      
      if (gui_module_ != null)
      {
        gui_module_.setApplication(application_);
        if (gui_module_.setFile(file_chooser_.getSelectedFile()))
        {
          file_panel_.setText("File: " + file_chooser_.getSelectedFile());
        
          JComponent main_panel = gui_module_.getMainPanel();
          getContentPane().add(main_panel, BorderLayout.CENTER);
          
          JComponent option_panel = gui_module_.getIOOptionPanel();
          io_panel_.setIOOptions(option_panel);
          io_panel_.addIOActionListener(gui_module_.getIOActionListener());
          io_panel_.setIOMode(gui_module_.getIOMode());
          pack();
        }
        else
        {
          file_panel_.setText("No file open");
          gui_module_ = null;
        }
      }
      else
      {
        repaint();
      }
    }
    status_panel_.clearStatusText();
  }
    
  //---------------------------------------------------------------
  /**
   * Method description
   */
  protected void quit()
  {
    dispose();
    System.exit(0);
  }
    
  protected DinomedaApplication application_ = null;
  protected StatusPanel status_panel_ = null;
  protected JTextField file_panel_ = null;
  protected IOPanel io_panel_ = null;
  protected GUIModule gui_module_ = null;

  protected JMenuItem file_open_ = null;
  protected JMenuItem file_quit_ = null;
  
  protected JFileChooser file_chooser_ = null;  
  protected AccessOptionPanel file_option_panel_ = null;
}
