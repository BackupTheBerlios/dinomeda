///////////////////////////////////////////////////////////////////////////////
//
// $Id: PNGStreamStore.java,v 1.1 2003/02/27 21:56:14 krake Exp $
//
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU Lesser General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.utils.metadata.dinomeda;

// Java imports
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Vector;

// external packages
import com.sixlegs.image.png.PngImage;
import com.sixlegs.image.png.TextChunk;

// local packages
import org.dinopolis.utils.metadata.DMDStreamStore;
import org.dinopolis.utils.metadata.DMDTextNode;
import org.dinopolis.utils.metadata.DMDNodeIterator;
import org.dinopolis.utils.metadata.DMDNode;
import org.dinopolis.utils.metadata.DMDJobList;
import org.dinopolis.utils.metadata.DMDJobListItem;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The PNGStreamStore 
 */

public class PNGStreamStore implements DMDStreamStore
{
    
  //---------------------------------------------------------------
  /**
   * Constructs a PNGStreamStore object
   */
  public PNGStreamStore()
  {
  }

  //---------------------------------------------------------------
  /**
   * Get input stream
   */
  public InputStream getInputStream()
  {
    return input_;
  }

  //---------------------------------------------------------------
  /**
   * Set input stream
   */
  public void setInputStream(InputStream stream)
  {
    input_ = stream;
  }

  //---------------------------------------------------------------
  /**
   * Get output stream
   */
  public OutputStream getOutputStream()
  {
    return output_;
  }

  //---------------------------------------------------------------
  /**
   * Set output stream
   */
  public void setOutputStream(OutputStream stream)
  {
    output_ = stream;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode()
  {
    int mode = NO_IO;
    
    if (input_ != null)
    {
      mode |= READ;
    }
    
    return mode;
  }
    
  //---------------------------------------------------------------
  /**
   * Read metadata form the PNG stream.
   */
  public int read(DMDJobList joblist)  throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");

    deleteDMDNodes(joblist, dmdlist_);
    
    PngImage png = new PngImage(input_);
    
    // read all data from PNG stream
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

  //---------------------------------------------------------------    
  /**
   * Read metadata form the PNG stream.
   */
  public int read(String item) throws IOException 
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return read(joblist); 
  }  
  
  //---------------------------------------------------------------  
  /**
   * Read metadata form the PNG stream.
   */
  public int read() throws IOException 
  {
    return read(joblist_);
  }

  
  //---------------------------------------------------------------  
  /**
   * Write metadata to PNG stream.
   * not implemented
   */
  public int write(String item) throws IOException 
  {
    throw new IOException();
  }
  
  //---------------------------------------------------------------  
  /**
   * Write metadata to PNG stream.
   * not implemented
   */  
  public int write(DMDJobList joblist) throws IOException 
  {
    throw new IOException();
  }
  
  //---------------------------------------------------------------  
  /**
   * Write metadata to PNG stream.
   * not implemented
   */
  public int write() throws IOException 
  { 
    throw new IOException(); 
  }

  
  //---------------------------------------------------------------  
  /**
   * Update metadata in PNG stream.
   * not implemented
   */
  public int update(String item) throws IOException
  {
    throw new IOException();
  }
  
  //---------------------------------------------------------------  
  /**
   * Update metadata in PNG stream.
   * not implemented
   */
  public int update(DMDJobList joblist) throws IOException
  {
    throw new IOException();
  }

  //---------------------------------------------------------------  
  /**
   * Update metadata in PNG stream.
   * not implemented
   */
  public int update() throws IOException
  {
    throw new IOException();
  }


  //---------------------------------------------------------------
  /**
  * Set element
  */
  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    joblist_.add(new DMDJobListItem(item).itemModified());
    // remove all exiting DMDNodes    
    deleteDMDNodes(item, dmdlist_);
    dmdlist_.add(node);    
  }
  
  //---------------------------------------------------------------
  /**
  * Set elements
  */
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
  /**
  * Get first element
	* ok!
  */
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
    return null;
  }

  
  //---------------------------------------------------------------
  /**
   * Get elements
   */
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
  /**
   * toString
   */
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
  /**
   * Get MIME type
   */
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
  
  
    
  
  protected InputStream input_ = null;  
  protected OutputStream output_ = null;  
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector dmdlist_ = new Vector(5, 5);

}
