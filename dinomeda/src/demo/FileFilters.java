///////////////////////////////////////////////////////////////////////////////
//
// $Id: FileFilters.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import javax.swing.filechooser.FileFilter;

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class FileFilters
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  static FileFilter createAllSupportedFilesFilter(DinomedaApplication application)
  {
    return new AllSupportedFilter(application.getSupportedMIMETypes());
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  static FileFilter[] createMIMEFileFilters(DinomedaApplication application)
  {
    String[] types = application.getSupportedMIMETypes();
    FileFilter[] filters = new FileFilter[types.length];
    
    for (int count = 0; count < filters.length; ++count)
    {
      filters[count] = new MIMEFilter(types[count]);
    }
    
    return filters;
  }
}

class AllSupportedFilter extends FileFilter
{
  public AllSupportedFilter(String[] mime_types)
  {
    mime_types_ = mime_types;
  }

  public boolean accept(File file)
  {
    if (file.isDirectory())
    {
      return true;
    }
    
    String type = MIME.getMIMETypeForFile(file);
    if (type == null)
    {
      return false;
    }

    for (int count = 0; count < mime_types_.length; ++count)
    {
      if (mime_types_[count].equals(type))
      {
        return true;
      }
    }
    return false;
  }

  public String getDescription()
  {
    return "All supported MIME types";
  }

  protected String[] mime_types_;
}

class MIMEFilter extends FileFilter
{
  public MIMEFilter(String mime_type)
  {
    mime_type_ = mime_type;
  }

  public boolean accept(File file)
  {
    if (file.isDirectory())
    {
      return true;
    }
    
    String type = MIME.getMIMETypeForFile(file);
    if (type == null)
    {
      return false;
    }
    return mime_type_.equals(type);
  }

  public String getDescription()
  {
    return mime_type_;
  }

  protected String mime_type_;
}
