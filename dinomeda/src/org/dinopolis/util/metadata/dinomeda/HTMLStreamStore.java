///////////////////////////////////////////////////////////////////////////////
//
// $Id: HTMLStreamStore.java,v 1.3 2003/03/16 17:23:32 mwelp Exp $
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

// external packages
import com.stevesoft.pat.Regex;

// local packages
import org.dinopolis.util.metadata.DMDStreamStore;
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
  
  public int read(DMDJobList joblist) throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");

    // remove all DMDNodes
    deleteDMDNodes(joblist, metalist_);
    
    InputStreamReader sr = new InputStreamReader(input_);
    BufferedReader reader = new BufferedReader(sr);

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
    sr.close();
    
    return count;
  }

  public int read(String item) throws IOException 
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return read(joblist); 
  }  
  
  public int read()  throws IOException 
  {
    return read(joblist_);
  }    
  
  public int write(DMDJobList joblist) throws IOException 
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);

    // extern and intern joblist elements
    joblist.add(joblist_);
    
    InputStreamReader sr = new InputStreamReader(input_);
    BufferedReader reader = new BufferedReader(sr);
    OutputStreamWriter sw = new OutputStreamWriter(output_);
    PrintWriter writer = new PrintWriter(sw);
    
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
            writer.println(regex_title_.replaceFirst(s));        
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
            writer.println(regex_title_.replaceFirst(s));
          }
          count++;
        }
      }
      else
      // found no metatag
      {
        writer.println(s);
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

  public int write(String item) throws IOException 
  {    
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return write(joblist);
  }

  public int write() throws IOException 
  {    
    return write(new DMDJobList());
  }

  public int update(DMDJobList joblist) throws IOException
  {
    int count = 0;
    boolean all = joblist.contains("/") || joblist.contains("/*");
    Vector tmp = new Vector(5, 5);
    
    // extern and intern joblist elements
    joblist.add(joblist_);
    
    InputStreamReader sr = new InputStreamReader(input_);    
    BufferedReader reader = new BufferedReader(sr);
    OutputStreamWriter sw = new OutputStreamWriter(output_);
    PrintWriter writer = new PrintWriter(sw);    

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
      writer.println(s);
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

  public int update(String item) throws IOException
  {
    DMDJobList joblist = new DMDJobList();
    joblist.add(new DMDJobListItem(item));
    return update(joblist);
  }

  public int update() throws IOException
  {
    return update(new DMDJobList());
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

  
  protected Regex regex_title_ = new Regex("<title>(.*)</title>");		
  protected Regex regex_meta_ = new Regex("<meta.*name=\"(.*?)\".*content=\"(.*?)\">");								
  protected Regex regex_start_ = new Regex("<head>");
  protected Regex regex_stop_ = new Regex("</head>");

  
  protected InputStream input_ = null;  
  protected OutputStream output_ = null;  
  protected DMDJobList joblist_ = new DMDJobList();
  protected Vector metalist_ = new Vector(5, 5);
  
}


