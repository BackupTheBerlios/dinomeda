///////////////////////////////////////////////////////////////////////////////
//
// $Id: PNGFileStore.java,v 1.4 2003/05/06 16:46:27 krake Exp $
//
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.util.metadata.dinomeda;

// Java imports
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Vector;

// external packages
import com.sixlegs.image.png.PngImage;
import com.sixlegs.image.png.TextChunk;

// local packages
import org.dinopolis.util.metadata.DMDFileStore;
import org.dinopolis.util.metadata.DMDTextNode;
import org.dinopolis.util.metadata.DMDNodeIterator;
import org.dinopolis.util.metadata.DMDNode;
import org.dinopolis.util.metadata.DMDJobList;
import org.dinopolis.util.metadata.DMDJobListItem;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 */

/**
 * The PNGFileStore 
 */

public class PNGFileStore implements DMDFileStore
{
  
  public File getFile()
  {
    return file_;
  }

  //---------------------------------------------------------------
  
  public void setFile(File file) 
  {
    file_ = file;
  }

  //---------------------------------------------------------------
  
  public void setFileName(String filename) throws FileNotFoundException 
  {
    file_ = new File(filename);
  }

  public int getIOMode()
  {
    if (file_ == null || !file_.exists())
    {
      return NO_IO;
    }
    
    int mode = NO_IO;
    if (file_.canRead())
    {
      mode |= READ;
    }
    
    return mode;
  }
  
  //---------------------------------------------------------------
  
  public int readMetaData(DMDJobList joblist)  throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");

    deleteDMDNodes(joblist, dmdlist_);
    
    PngImage png = new PngImage(file_.getPath());
    
    // read all data from PNG file
    png.getEverything();

    if(png.hasErrors()) 
    {
      throw new IOException();
    }

    for (Enumeration e = png.getTextChunks(); e.hasMoreElements();) 
    {
      TextChunk chunk = (TextChunk)e.nextElement();
      String key = chunk.getKeyword().toLowerCase();	

      if(all || joblist.contains("/" + key)) {
        dmdlist_.add(new DMDTextNode("/", key, chunk.getText()));
        joblist_.add(new DMDJobListItem("/" + key).itemRead());
        count++;
      }
    }
    return count;
  }  

  public int readMetaData(String item) throws IOException 
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return readMetaData(joblist); 
  }  
  
  public int readMetaData() throws IOException 
  {
    return readMetaData(joblist_);
  }

  public int writeMetaData(String item) throws IOException 
  {
    throw new IOException();
  }
  
  public int writeMetaData(DMDJobList joblist) throws IOException 
  {
    throw new IOException();
  }
  
  public int writeMetaData() throws IOException 
  { 
    throw new IOException(); 
  }

  //---------------------------------------------------------------  
 
  public int updateMetaData(String item) throws IOException
  {
    throw new IOException();
  }
  
  //---------------------------------------------------------------  
  
  public int updateMetaData(DMDJobList joblist) throws IOException
  {
    throw new IOException();
  }

  //---------------------------------------------------------------  
  
  public int updateMetaData() throws IOException
  {
    throw new IOException();
  }


  //---------------------------------------------------------------
  
  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    joblist_.add(new DMDJobListItem(item).itemModified());	
    // remove all exiting DMDNodes    
    deleteDMDNodes(item, dmdlist_);
    dmdlist_.add(node);    
  }
  
  //---------------------------------------------------------------
  
  public void set(DMDNodeIterator iterator)
  {
    while (iterator.hasNext())
    {
      DMDNode node = iterator.nextNode();
      String item = node.getPath() + node.getName();
      joblist_.add(new DMDJobListItem(item).itemModified());	
      // remove all exiting DMDNodes            
      deleteDMDNodes(item, dmdlist_);
      dmdlist_.add(node);    
    }
  }

  //---------------------------------------------------------------
  
  public DMDNode getElement(String path)
  {
    for (int i=0; i<dmdlist_.size(); i++)
    {
      DMDNode node = (DMDNode)dmdlist_.get(i);
      if(path.equals(node.getPath() + node.getName()))
      {
        return node;
      }
    }
    return new DMDNode();
  }

  
  //---------------------------------------------------------------

  public Iterator iterator()
  {
    return get("/");
  }

  //---------------------------------------------------------------

  public DMDNodeIterator get(String item) 
  {
    Vector list = new Vector();
  
    boolean all = item.equals("/") || item.equals("/*");
    
    for (int i=0; i<dmdlist_.size(); i++)
    {
      DMDNode node = (DMDNode)dmdlist_.get(i);
      if(all || item.equals(node.getPath() + node.getPath()))
      {
        list.add(node);
      }
    }
    
    return new DMDNodeIterator(list.iterator());
  }
  
  //---------------------------------------------------------------
  
  public String toString()
  {
    String out = new String();
    for (int i=0; i<dmdlist_.size(); i++)
    {
      out += dmdlist_.get(i) + "\n";
    }
    return out;	
  }
  
  //---------------------------------------------------------------
  
  public String getMIMEType()
  {
    return "image/png";
  }
 

  
  //---------------------------------------------------------------
  /**
   * Delete DMDNodes from an Vector
   */
  private void deleteDMDNodes(String item, Vector list) 
  {
    if(item.equals("/") || item.equals("/*"))
    {
      list.clear();
    }
    else
    {
      int i = 0;
      while (i < list.size())
      {
        DMDNode node = (DMDNode)list.get(i);
        if(item.equals(node.getPath() + node.getName()))
          list.remove(i);
        else
          i++;
      }
    }
  }
  
  
  //---------------------------------------------------------------
  /**
   * Delete DMDNodes from an Vector
   */
  private void deleteDMDNodes(DMDJobList joblist, Vector list) 
  {
    if(joblist.contains("/") || joblist.contains("/*"))
    {
      list.clear();
    }
    else
    {
      int i = 0;
      while (i < list.size())
      {
        DMDNode node = (DMDNode)list.get(i);
        if(joblist.contains(node.getPath() + node.getName()))
          list.remove(i);
        else
          i++;
      }
    }
  }
    
  
  protected File file_;  
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector dmdlist_ = new Vector(5, 5);

}
