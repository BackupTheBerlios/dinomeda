///////////////////////////////////////////////////////////////////////////////
//
// $Id: HTMLFileStore.java,v 1.9 2003/05/06 16:46:27 krake Exp $
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


package org.dinopolis.util.metadata.dinomeda;

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
import java.util.Iterator;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// external packages


// local packages
import org.dinopolis.util.metadata.DMDFileStore;
import org.dinopolis.util.metadata.DMDTextNode;
import org.dinopolis.util.metadata.DMDNodeIterator;
import org.dinopolis.util.metadata.DMDNode;
import org.dinopolis.util.metadata.DMDJobList;
import org.dinopolis.util.metadata.DMDJobListItem;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.2.0
 */

/**
 * The HTMLFileStore
 */

public class HTMLFileStore implements DMDFileStore
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

    if (file_.canWrite())
    {
      mode |= WRITE;
      mode |= UPDATE;
    }
    return mode;
  }

  public int readMetaData(DMDJobList joblist) throws IOException
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Matcher matcher;

    // remove all DMDNodes
    deleteDMDNodes(joblist, metalist_);

    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);

    String s = reader.readLine();
    matcher = regex_stop_.matcher(s);
    while(s != null && !matcher.matches())
    {
      // search line for the title element
      matcher = regex_title_.matcher(s);
      if (matcher.matches())
      {
        String title = matcher.group(1);
        if (all || joblist.contains("/title"))
        {
          setElement(new DMDTextNode("/", "title", title));
          joblist_.add(new DMDJobListItem("/title").itemRead());
         count++;
        }
      }
      // search line for a meta element
      matcher = regex_meta_.matcher(s);
      if (matcher.matches())
      {
        String name = matcher.group(1).toLowerCase();
        String content = matcher.group(2);
        if(all || joblist.contains("/" + name ))
        {
          setElement(new DMDTextNode("/", name, content));
          joblist_.add(new DMDJobListItem("/" + name).itemRead());
          count++;
        }
      }
      s = reader.readLine();
      matcher = regex_stop_.matcher(s);
    }

    reader.close();
    fr.close();
    
    return count;
  }

  public int readMetaData(String item) throws IOException 
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return readMetaData(joblist); 
  }  
  
  public int readMetaData()  throws IOException 
  {
    return readMetaData(joblist_);
  }    
  
  public int writeMetaData(DMDJobList joblist) throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);
    Matcher matcher;

    // extern and intern joblist elements
    joblist.add(joblist_);

    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);

    StringBuffer buffer = new StringBuffer();
    
    String s = reader.readLine();
    // replace all existing metatags
    matcher = regex_stop_.matcher(s);
    while(s != null && !matcher.matches())
    {
      matcher = regex_title_.matcher(s);
      if (matcher.matches())
      {
        // search line for the title element
        if (all || joblist.contains("/title")) 
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/title", metalist_, tmp);
          if(node != null)
          {
            buffer.append(matcher.replaceFirst("<title>" + node.get() + "</title>"));
            buffer.append("\n"); 
          }
          count++;
        }
      }
      // search line for a meta element
      else
      {
        matcher = regex_meta_.matcher(s);
        if (matcher.matches())
        {
          String name = matcher.group(1).toLowerCase();
          if(all || joblist.contains("/" + name ))
          {
            DMDTextNode node = (DMDTextNode)firstDMDNode("/" + name, metalist_, tmp);
            if(node != null) {
              buffer.append(matcher.replaceFirst("<meta name=\"" + name + "\" content=\"" + node.get() + "\">"));
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
      }
      s = reader.readLine();
      matcher = regex_stop_.matcher(s);
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
    for (int i=0; i<tmp.size(); i++)
    {
      metalist_.add(tmp.get(i));
    }
            
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

  public int writeMetaData(String item) throws IOException 
  {    
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return writeMetaData(joblist);
  }

  public int writeMetaData() throws IOException 
  {    
    return writeMetaData(new DMDJobList());
  }

  public int updateMetaData(DMDJobList joblist) throws IOException
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);
    Matcher matcher;

    // extern and intern joblist elements
    joblist.add(joblist_);

    FileReader fr = new FileReader(file_);
    BufferedReader reader = new BufferedReader(fr);

    StringBuffer buffer = new StringBuffer();

    String s = reader.readLine();
    matcher = regex_stop_.matcher(s);
    while(s != null && !matcher.matches())
    {
      // search line for the title element
      matcher = regex_title_.matcher(s);
      if (matcher.matches())
      {
        if (all || joblist.contains("/title"))
        {
          DMDTextNode node = (DMDTextNode)firstDMDNode("/title", metalist_, tmp);
          if(node != null)
          {
            s = matcher.replaceFirst("<title>" + node.get() + "</title>");
          }
          count++;
        }
      }
      // search line for a meta element
      else
      {
        matcher = regex_meta_.matcher(s);
        if (matcher.matches())
        {
          String name = matcher.group(1).toLowerCase();
          if(all || joblist.contains("/" + name ))
          {
            DMDTextNode node = (DMDTextNode)firstDMDNode("/" + name, metalist_, tmp);
            if(node != null)
            {
              s = matcher.replaceFirst("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");
            }
            count++;
          }
        }
      }
      buffer.append(s);
      buffer.append("\n");
      s = reader.readLine();
      matcher = regex_stop_.matcher(s);
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
    for (int i=0; i<tmp.size(); i++)
    {
      metalist_.add(tmp.get(i));
    }
        
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

  public int updateMetaData(String item) throws IOException
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return updateMetaData(joblist);
  }

  public int updateMetaData() throws IOException
  {
    return updateMetaData(new DMDJobList());
  }

  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    joblist_.add(new DMDJobListItem(item).itemModified());
    
    if (node.isNull())
    {
      // remove all exiting DMDNodes
      deleteDMDNodes(item, metalist_); 
    }
    else
    {
      // remove all exiting DMDNodes
      deleteDMDNodes(item, metalist_);
      metalist_.add(node);
    }
  }
  
  public void set(DMDNodeIterator iterator)
  {
    while (iterator.hasNext())
    {
      DMDNode node = iterator.nextNode();
      setElement(node);
    }
  }

  public DMDNode getElement(String path)
  {
    for (int i=0; i<metalist_.size(); i++)
    {
      Object o = metalist_.get(i);
      System.err.println("getElement: "+ o);
      DMDNode node = (DMDNode)(metalist_.get(i));
      if(path.equals(node.getPath() + node.getName()))
      {
         return node;
      }
    }
    return new DMDNode();
  }

  public DMDNodeIterator get(String item) 
  {
    Vector list = new Vector();
  
    boolean all = item.equals("/") || item.equals("/*");
    
    for (int i=0; i<metalist_.size(); i++)
    {
      DMDNode node = (DMDNode)(metalist_.get(i));
      if(all || item.equals(node.getPath() + node.getName()))
      {
        list.add(node);
      }
    }
    
    return new DMDNodeIterator(list.iterator());
  }

  public Iterator iterator()
  {
    return get("/");
  }

  public String toString()
  {
    String out = new String();
    for (int i=0; i<metalist_.size(); i++)
    {
      out += metalist_.get(i) + "\n";
    }
    return out;	
  }

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

  protected Pattern regex_title_ = Pattern.compile(".*<title>(.*)</title>");
  protected Pattern regex_meta_ = Pattern.compile(".*<meta.*name=\"(.*?)\".*content=\"(.*?)\">");
  protected Pattern regex_start_ = Pattern.compile(".*<head>");
  protected Pattern regex_stop_ = Pattern.compile(".*</head>");

  protected File file_ = null;
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector metalist_ = new Vector(5, 5);

}


