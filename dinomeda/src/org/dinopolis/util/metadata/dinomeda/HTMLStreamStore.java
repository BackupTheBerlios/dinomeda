///////////////////////////////////////////////////////////////////////////////
//
// $Id: HTMLStreamStore.java,v 1.6 2003/04/24 09:08:49 osma Exp $
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.BufferedWriter;
import java.io.PrintWriter;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

// external packages

// local packages
import org.dinopolis.util.metadata.DMDStreamStore;
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
 * The HTMLStreamStore
 */

public class HTMLStreamStore implements DMDStreamStore
{
    
  //---------------------------------------------------------------
 
  public InputStream getInputStream()
  {
    return input_;
  }

  //---------------------------------------------------------------
 
  public void setInputStream(InputStream stream)
  {
    input_ = stream;
  }

  //---------------------------------------------------------------
  
  public OutputStream getOutputStream()
  {
    return output_;
  }

  //---------------------------------------------------------------
  
  public void setOutputStream(OutputStream stream)
  {
    output_ = stream;
  }
    
  //---------------------------------------------------------------
  
  
  public int getIOMode()
  {
    int mode = NO_IO;
    
    if (input_ != null)
    {
      mode |= READ;
      
      if (output_ != null)
      {
        mode |= WRITE;
        mode |= UPDATE;
      }  
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
    
    InputStreamReader sr = new InputStreamReader(input_);
    BufferedReader reader = new BufferedReader(sr);

    String s = reader.readLine();
    matcher = regex_stop_.matcher(s);
    while(s != null && !matcher.matches()) {
      // search line for the title element
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
    sr.close();
    
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
    
    InputStreamReader sr = new InputStreamReader(input_);
    BufferedReader reader = new BufferedReader(sr);
    OutputStreamWriter sw = new OutputStreamWriter(output_);
    PrintWriter writer = new PrintWriter(sw);
    
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
            writer.println(matcher.replaceFirst("<title>" + node.get() + "</title>"));
          }
          count++;
        }
      }  
      // search line for a meta element
      else
      {
        matcher = regex_stop_.matcher(s);
        if (matcher.matches())
        {
          String name = matcher.group(1).toLowerCase();
          if(all || joblist.contains("/" + name ))
          {
            DMDTextNode node = (DMDTextNode)firstDMDNode("/" + name, metalist_, tmp);
            if(node != null) {
              writer.println(matcher.replaceFirst("<meta name=\"" + name + "\" content=\"" + node.get() + "\">"));
            }
            count++;
          }
        }
        else
        // found no metatag
        {
          writer.println(s);
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
          writer.println("<title>"+ node.get() +"</title>");
          count++;
        }
      }
      else
      {
        if (all || joblist.contains("/" + name)) 
        {
          writer.println("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");    
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

    // read complete stream
    while(s != null)
    {
      writer.println(s);
      s = reader.readLine();
    }
    
    reader.close();
    sr.close();
  
    writer.close();
    sw.close();
        
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
    
    InputStreamReader sr = new InputStreamReader(input_);    
    BufferedReader reader = new BufferedReader(sr);
    OutputStreamWriter sw = new OutputStreamWriter(output_);
    PrintWriter writer = new PrintWriter(sw);    

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
      writer.println(s);
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
          writer.println("<title>"+ node.get() +"</title>");
          count++;
        }
      }
      else
      {
        if (all || joblist.contains("/" + name)) 
        {
          writer.println("<meta name=\"" + name + "\" content=\"" + node.get() + "\">");   
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
      writer.println(s);
      s = reader.readLine();
    }
    
    reader.close();
    sr.close();
   
    writer.close();
    sw.close();
        
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
    // remove all exiting DMDNodes
    deleteDMDNodes(item, metalist_);
    metalist_.add(node);    
  }
  
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
  
  protected InputStream input_ = null;  
  protected OutputStream output_ = null;  
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector metalist_ = new Vector(5, 5);
  
}


