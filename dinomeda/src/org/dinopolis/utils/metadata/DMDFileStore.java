///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDFileStore.java,v 1.1 2003/02/27 21:56:11 krake Exp $
//
// Copyright: Author <email>, year
//
///////////////////////////////////////////////////////////////////////////////
//                                                                          
//   This program is free software; you can redistribute it and/or modify  
//   it under the terms of the GNU Lesser General Public License as        
//   published by the Free Software Foundation; either version 2 of the    
//   License, or (at your option) any later version.                       
//                                                                         
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.utils.metadata;

// Java imports
import java.io.File;
import java.io.FileNotFoundException;

// external packages

// local packages

/**
 * @author Author <email> (mandatory)
 * @version major.minor.patch (mandatory)
 *
 * Class or interface description (mandatory)
 */

public interface DMDFileStore extends DMDStore
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public File getFile();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setFile(File file); // throws NotAFileException when isDirectory

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setFileName(String file_name) throws FileNotFoundException;
}
