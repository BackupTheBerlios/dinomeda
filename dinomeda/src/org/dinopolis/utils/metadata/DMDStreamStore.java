///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDStreamStore.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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
import java.io.InputStream;
import java.io.OutputStream;

// external packages

// local packages

/**
 * @author Author <email> (mandatory)
 * @version major.minor.patch (mandatory)
 *
 * Class or interface description (mandatory)
 */

public interface DMDStreamStore extends DMDStore
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public InputStream getInputStream();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setInputStream(InputStream stream);

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public OutputStream getOutputStream();

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setOutputStream(OutputStream stream);
}