///////////////////////////////////////////////////////////////////////////////
//
// $Id: MIME.java,v 1.2 2003/03/18 17:04:17 krake Exp $
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

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.1
 */

/**
 * Class providing methods to determine a file's MIME type.
 */

public class MIME
{
  /**
   * Tries to determine a file's MIME type by comparing
   * the filename's extension with some known file extensions.
   */
  public static String getMIMETypeForFile(File file)
  {
    String filename = file.getName();
    int index = filename.lastIndexOf('.');
    if (index == -1)
    {
      return null;
    }

    String extension = filename.substring(index+1, filename.length());
    extension = extension.toLowerCase();

    String mime_type = null;
    if (extension.equals("mp3"))
    {
      mime_type = "audio/x-mp3";
    }
    else if (extension.startsWith("htm"))
    {
      mime_type = "text/html";
    }
    else if (extension.startsWith("pdf"))
    {
      mime_type = "application/pdf";
    }
    else if (extension.startsWith("png"))
    {
      mime_type = "image/png";
    }
    else if (extension.equals("jpg") || extension.equals("jpeg"))
    {
      mime_type = "image/jpeg";
    }

    return mime_type;
  }
}
