///////////////////////////////////////////////////////////////////////////////
//
// $Id: HTMLFileStore.java,v 1.1 2003/02/27 21:56:14 krake Exp $
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
import java.util.Vector;
import java.lang.StringBuffer;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;

// external packages
import com.stevesoft.pat.Regex;

// local packages
import org.dinopolis.utils.metadata.DMDFileStore;
import org.dinopolis.utils.metadata.DMDTextNode;
import org.dinopolis.utils.metadata.DMDNodeIterator;
import org.dinopolis.utils.metadata.DMDNode;
import org.dinopolis.utils.metadata.DMDJobList;
import org.dinopolis.utils.metadata.DMDJobListItem;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.1.0
 *
 * The HTMLFileStore
 */

public class HTMLFileStore implements DMDFileStore
{
    
  //---------------------------------------------------------------
  /**
   * Constructs a HTMLFileStore object
   */
  public HTMLFileStore()
  {
  }

  //---------------------------------------------------------------
  /**
   * Constructs a HTMLFileStore object from a local HTML file.
   */   
  public HTMLFileStore(String filename) throws FileNotFoundException 
  {
    setFileName(filename);
  }
  
  //---------------------------------------------------------------
  /**
   * Get file
   */
  public File getFile()
  {
     return file_;
  }

  //---------------------------------------------------------------
  /**
   * Set file
   */
  public void setFile(File file) 
  {
    file_ = file;
  }

  //---------------------------------------------------------------
  /**
   * set filename
   */
  public void setFileName(String filename) throws FileNotFoundException 
  {
    file_ = new File(filename);
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
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
    if (file_.canWrite())
    {
      mode |= WRITE;
      mode |= UPDATE;
    }
    
    return mode;
  }
  
  //---------------------------------------------------------------  
  /**
   * Read metadata form the HTML file.
   */
  public int read(DMDJobList joblist) throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");

    // remove all DMDNodes
    deleteDMDNodes(joblist, metalist_);
    
    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);

    String s = reader.readLine();
    while(s != null && !regex_stop_.search(s)) {
      // search line for the title element
      if (regex_title_.search(s))
      {
        String title = regex_title_.stringMatched(1);
        if (all || joblist.contains("/title")) 
        {
          setElement(new DMDTextNode("/", "title", title));
          joblist_.add(new DMDJobListItem("/title").itemRead());
         count++;
        }
      }
      // search line for a meta element
      if (regex_meta_.search(s))
      {
        String name = regex_meta_.stringMatched(1).toLowerCase();
        String content = regex_meta_.stringMatched(2);
        if(all || joblist.contains("/" + name )) 
        {
          setElement(new DMDTextNode("/", name, content));
          joblist_.add(new DMDJobListItem("/" + name).itemRead());							
          count++;			  
        }
      }
      s = reader.readLine();
    }

    reader.close();
    fr.close();
    
    return count;
  }

  //---------------------------------------------------------------  
  /**
   * Read metadata form the HTML file.
   */
  public int read(String item) throws IOException 
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return read(joblist); 
  }  
  
  //---------------------------------------------------------------  
  /**
   * Read metadata form the HTML file.
   */
  public int read()  throws IOException 
  {
    return read(joblist_);
  }    
  
  //---------------------------------------------------------------  
  /**
   * Write metadata into the HTML file.
   */ 
  public int write(DMDJobList joblist) throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);

    // extern and intern joblist elements
    joblist.add(joblist_);
    
    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);
    
    StringBuffer buffer = new StringBuffer();
    
    String s = reader.readLine();
    // replace all existing metatags 
    while(s != null && !regex_stop_.search(s)) {
      if (regex_title_.search(s))
      {
        // search line for the title element
        if (all || joblist.contains("/title")) 
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/title", metalist_, tmp);
          if(node != null)
          {
            regex_title_.setReplaceRule("<title>" + node.get() + "</title>");
            buffer.append(regex_title_.replaceFirst(s)); 
            buffer.append("\n"); 
          }
          count++;
        }
      }  
      // search line for a meta element
      else if (regex_meta_.search(s))
      {
        String name = regex_meta_.stringMatched(1).toLowerCase();
        if(all || joblist.contains("/" + name )) 
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/" + name, metalist_, tmp);
          if(node != null) {
            regex_title_.setReplaceRule("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");
            buffer.append(regex_title_.replaceFirst(s));
            buffer.append("\n"); 
          }
          count++;
        }
      }
      else
      // found no metatag
      {
        buffer.append(s);
        buffer.append("\n"); 
      }
      s = reader.readLine();
    }
    
    // append metatags to the header
    DMDTextNode node = (DMDTextNode)firstDMDNode("/", metalist_, tmp);
    while(node != null)
    {
      String name = node.getName();
      if(name.equals("title"))
      {
        if (all || joblist.contains("/title")) 
        {
          buffer.append("<title>"+ node.get() +"</title>");
          buffer.append("\n");
          count++;
        }
      }
      else
      {
        if (all || joblist.contains("/" + name)) 
        {
          buffer.append("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");
          buffer.append("\n");
          count++;
        }
      }
      node = (DMDTextNode)firstDMDNode("/", metalist_, tmp);
    }

    // restore metadatalist
    metalist_.add(tmp);
        
    // read complete file
    while(s != null)
    {
      buffer.append(s);
      buffer.append("\n");
      s = reader.readLine();
    }
    
    reader.close();
    fr.close();
    
    // write complete file
    FileWriter fw = new FileWriter(file_);
    BufferedWriter writer = new BufferedWriter(fw);
    writer.write(buffer.toString());
    
    writer.close();
    fw.close();
        
    return count;
  }

  //---------------------------------------------------------------  
  /**
   * Write metadata in the HTML file.
   */ 
  public int write(String item) throws IOException 
  {    
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return write(joblist);
  }

  //---------------------------------------------------------------    
  /**
   * Write metadata in the HTML file.
   */ 
  public int write() throws IOException 
  {    
    return write(new DMDJobList());
  }

  //---------------------------------------------------------------    
  /**
   * Update metadata in the HTML file.
   */ 
  public int update(DMDJobList joblist) throws IOException
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);
    
    // extern and intern joblist elements
    joblist.add(joblist_);
    
    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);
    
    StringBuffer buffer = new StringBuffer();

    String s = reader.readLine();
    while(s != null && !regex_stop_.search(s)) {
      // search line for the title element
      if (regex_title_.search(s))
      {
        if (all || joblist.contains("/title")) 
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/title", metalist_, tmp);
          if(node != null)
          {
            regex_title_.setReplaceRule("<title>" + node.get() + "</title>");
            s = regex_title_.replaceFirst(s);
          }
          count++;
        }
      }  
      // search line for a meta element
      else if (regex_meta_.search(s))
      {
        String name = regex_meta_.stringMatched(1).toLowerCase();
        if(all || joblist.contains("/" + name )) 
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/" + name, metalist_, tmp);
          if(node != null)
          {
            regex_title_.setReplaceRule("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");
            s = regex_title_.replaceFirst(s);
          }
          count++;			  
        }
      }
      buffer.append(s);
      buffer.append("\n"); 
      s = reader.readLine();
    }

    DMDTextNode node = (DMDTextNode)firstDMDNode("/", metalist_, tmp);
    while(node != null)
    {
      String name = node.getName();
      if(name.equals("title"))
      {
        if (all || joblist.contains("/title")) 
        {
          buffer.append("<title>"+ node.get() +"</title>");
          buffer.append("\n");
          count++;
        }
      }
      else
      {
        if (all || joblist.contains("/" + name)) 
        {
          buffer.append("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");
          buffer.append("\n");
          count++;
        }
      }
      node = (DMDTextNode)firstDMDNode("/", metalist_, tmp);
    }

    // restore metadatalist
    metalist_.add(tmp);
        
    // read complete file
    while(s != null)
    {
      buffer.append(s);
      buffer.append("\n");
      s = reader.readLine();
    }
    
    reader.close();
    fr.close();

    // write complete file    
    FileWriter fw = new FileWriter(file_);
    BufferedWriter writer = new BufferedWriter(fw);
    writer.write(buffer.toString());
    
    writer.close();
    fw.close();
        
    return count;
  }

  //---------------------------------------------------------------    
  /**
   * Update metadata in the HTML file.
   */ 
  public int update(String item) throws IOException
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return update(joblist);
  }

  //---------------------------------------------------------------    
  /**
   * Update metadata in the HTML file.
   */ 
  public int update() throws IOException
  {
    return update(new DMDJobList());
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
    deleteDMDNodes(item, metalist_);
    metalist_.add(node);    
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
      deleteDMDNodes(item, metalist_);
      metalist_.add(node);    
    }
  }

  //---------------------------------------------------------------
  /**
   * Get first element
   */
  public DMDNode getElement(String path)
  {
    for (int i=0; i<metalist_.size(); i++)
    {
      DMDNode node = (DMDNode)metalist_.get(i);
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
    
    for (int i=0; i<metalist_.size(); i++)
    {
      DMDNode node = (DMDNode)metalist_.get(i);
      if(all || item.equals(node.getPath() + node.getName()))
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
    for (int i=0; i<metalist_.size(); i++)
    {
      out += metalist_.get(i) + "\n";
    }
    return out;	
  }

  //---------------------------------------------------------------
  /**
   * Get MIME type
   */
  public String getMIMEType()
  {
    return "text/html";
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
  

  //---------------------------------------------------------------
  /**
   * Returns the first DMDNode and moves it from list Vector to the tmp Vector
   */
  private DMDNode firstDMDNode(String item, Vector list,  Vector tmp) 
  {
    boolean all = item.equals("/") || item.equals("/*");
    int i = 0;
    while (i < list.size())
    {
      DMDNode node = (DMDNode)list.get(i);
      if(all || item.equals(node.getPath() + node.getName()))
      {
        tmp.add(node);
        list.remove(i);
        return node;
      }
      i++;
    }
    return null;
  }

  
  protected Regex regex_title_ = new Regex("<title>(.*)</title>");		
  protected Regex regex_meta_ = new Regex("<meta.*name=\"(.*?)\".*content=\"(.*?)\">");								
  protected Regex regex_start_ = new Regex("<head>");
  protected Regex regex_stop_ = new Regex("</head>");
 
  protected File file_ = null;  
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector metalist_ = new Vector(5, 5);
  
}


