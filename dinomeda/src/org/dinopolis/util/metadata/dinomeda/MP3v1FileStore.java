///////////////////////////////////////////////////////////////////////////////
//
// $Id: MP3v1FileStore.java,v 1.5 2003/04/24 09:08:49 osma Exp $
//
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2002
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
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// external packages

// local packages
import org.dinopolis.util.metadata.*;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at> * @author Martin Oswald <ossi1@sbox.tugraz.at>
 * @version 0.5.0
 *//**
 * Store working with MP3 files
 */

public class MP3v1FileStore implements DMDFileStore
{
  
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
  
  
  public int readMetaData() throws IOException 
  {
    int count = 0;
    RandomAccessFile file = new RandomAccessFile(file_, "r");
    file.seek(file.length() - getTagPosition("/tag"));   
    file.read(getTag("/tag"));
  
    if (!isValidID3v1())
    {
      System.err.println("Warning: No MP3IDv1 tags found! Use writeMetaData() to create tags.");
      return 0;
    }
  
    for (int i=0; i<joblist_.size();i++)
    {
      String job = joblist_.get(i).getJobName();
      
      if ((joblist_.get(i)).getStatus()!=1)
      {       
        System.err.println("MP3v1FileStore -> read " + job);
    
        file.seek(file.length() - getTagPosition(job));   
        file.read(getTag(job));
      
        (joblist_.get(i)).itemRead();
        count++;
        
      }
      else
      {
        System.err.println("MP3v1FileStore -> "+ job + " already read");
      }
    }
  
    file.close();
    return count;
  }
  
  
  public int readMetaData(String item) throws IOException 
  {
    return readMetaData(createJobList(item)); 
  }  
  
  
  public int readMetaData(DMDJobList joblist)  throws IOException 
  {
    joblist_.add(joblist);
    
    int count = 0;
    RandomAccessFile file = new RandomAccessFile(file_, "r");
    file.seek(file.length() - getTagPosition("/tag"));   
    file.read(getTag("/tag"));
  
    if (!isValidID3v1())
    {
      System.err.println("Warning: No MP3IDv1 tags found! Use writeMetaData() to create tags.");
      return 0;
    }
  
    for (int i=0; i<joblist.size();i++)
    {
      String job = joblist.get(i).getJobName();
      
      if ((joblist_.get(job)).getStatus()!=1)
      {       
        System.err.println("MP3v1FileStore -> read " + job);
    
        file.seek(file.length() - getTagPosition(job));   
        file.read(getTag(job));
      
        (joblist_.get(job)).itemRead();
        count++;
        
      }
      else
      {
        System.err.println("MP3v1FileStore -> "+ job + " already read");
      }
    }
  
    file.close();
    return count;
  }  
  
   
  
  public int updateMetaData() throws IOException
  {
    int count = 0;

    RandomAccessFile file = new RandomAccessFile(file_, "rw");

    /* look for ID3Tag */
    file.seek(file.length() - getTagPosition("/tag"));
    file.read(getTag("/tag"));

    if (isValidID3v1())
    {
      for (int i = 1; i < FIELDS.length; i++)
      {

        if (joblist_.contains(FIELDS[i]) && joblist_.get(FIELDS[i]).getStatus()==2)
        {
          System.err.println("MP3v1FileStore -> update " + FIELDS[i] + " '" + getElement(FIELDS[i]) + "'");
          file.write(getTag(FIELDS[i]));
          (joblist_.get(FIELDS[i])).itemWrote();
          count++;
        }
        else
        {
          file.skipBytes(getTag(FIELDS[i]).length);
        }
      }
    }
    file.close();
    return count;
  }

  
  public int updateMetaData(String item) throws IOException
  {
    return updateMetaData(createJobList(item)); 
  }


  
  public int updateMetaData(DMDJobList joblist) throws IOException
  {
    int count=0;
    RandomAccessFile file = new RandomAccessFile(file_, "rw");

    /* look for ID3Tag */
    file.seek(file.length() - getTagPosition("/tag"));
    file.read(getTag("/tag"));

    if (isValidID3v1())
    {
      for (int i = 1; i < FIELDS.length; i++)
      {
        if (joblist.contains(FIELDS[i])&& joblist_.contains(FIELDS[i]) && joblist_.get(FIELDS[i]).getStatus()==2)
        {
          System.err.println("MP3v1FileStore -> update " + FIELDS[i] + " '" + getElement(FIELDS[i]) + "'");
          file.write(getTag(FIELDS[i]));
          (joblist_.get(FIELDS[i])).itemWrote();
          count++;
        }
        else
        {
          file.skipBytes(getTag(FIELDS[i]).length);
        }
      }
    }
    file.close();
    return count;
  }

  
  
  public int writeMetaData() throws IOException 
  {  
    int count=0;
    RandomAccessFile file = new RandomAccessFile(file_, "rw");
    
    /* look for ID3Tag */
    file.seek(file.length() - getTagPosition("/tag"));
    file.read(getTag("/tag"));
    
    if (!isValidID3v1()) 
    {
      /* append ID3Tag to file */
      file.seek(file.length());
      file.write(new String("TAG").getBytes());
    }
  
    // write all ID3 fields
    for (int i = 1; i < FIELDS.length; i++)
    {
      if (joblist_.contains(FIELDS[i]) && (joblist_.get(FIELDS[i])).getStatus()>0)
      {
        file.write(getTag(FIELDS[i]));
        (joblist_.get(FIELDS[i])).itemWrote();
        System.err.println("MP3v1FileStore -> write " + FIELDS[i] + " '" + getElement(FIELDS[i])+ "'");
        
      }
      else
      {
        setTag(FIELDS[i], "");
        file.write(getTag(FIELDS[i]));
        System.err.println("MP3v1FileStore -> delete " + FIELDS[i]);
      }
      count++;
      
      
    }
    file.close();
    return count;
  }
  
  public int writeMetaData(String item) throws IOException 
  {
    return writeMetaData(createJobList(item));
  }
  
  
  public int writeMetaData(DMDJobList joblist) throws IOException 
  {
    joblist_.add(joblist);
    int count=0;
    
    RandomAccessFile file = new RandomAccessFile(file_, "rw");
    
    /* look for ID3Tag */
    file.seek(file.length() - getTagPosition("/tag"));
    file.read(getTag("/tag"));
    
    if (!isValidID3v1()) 
    {
      /* append ID3Tag to file */
      file.seek(file.length());    
    }
  
    // write all ID3 fields
    for (int i = 1; i < FIELDS.length; i++)
    {
      if (joblist.contains(FIELDS[i]))
      {
        file.write(getTag(FIELDS[i]));
        (joblist_.get(FIELDS[i])).itemWrote();
        System.err.println("MP3v1FileStore -> write " + FIELDS[i] + " '" + getElement(FIELDS[i])+"'");
        count++;
      }
      else
      {
        if (joblist_.contains(FIELDS[i]))
        {
          setTag(FIELDS[i], "");
          file.write(getTag(FIELDS[i]));
          System.err.println("MP3v1FileStore -> delete " + FIELDS[i]);
          count++;
        }
         else
        {
          file.skipBytes(getTag(FIELDS[i]).length);
        }
      }
      
      
    }
    file.close();
    return count;
  }
  
  
  public String getMIMEType()
  {
    return "audio/x-mp3";
  }

   
  public DMDNode getElement(String path)
  {
    DMDNode node = new DMDNode();
    
    if (joblist_.contains(path))
      {
        if (path.equals("/title")  ) node = new DMDTextNode("/","title",(new String(getTag(path)).trim()));
        if (path.equals("/artist") ) node = new DMDTextNode("/","artist",(new String(getTag(path)).trim()));
        if (path.equals("/album")  ) node = new DMDTextNode("/","album",(new String(getTag(path)).trim()));
        if (path.equals("/year")   ) node = new DMDTextNode("/","year",(new String(getTag(path)).trim()));
        if (path.equals("/comment")) node = new DMDTextNode("/","comment",(new String(getTag(path)).trim()));
        if (path.equals("/track")  ) node = new DMDNumberNode("/","track",new Integer(getTag(path)[1]));
        if (path.equals("/genre")  ) node = new DMDNumberNode("/","genre",new Integer(getTag(path)[0]));
      }
     
    if (node.getNodeType()==node.TEXT_NODE)
    {
      if (node.isNull() || ((DMDTextNode)node).get().trim().length()==0)
      {
        return new DMDNode();
      }
      else
      {
        return node;
      }
    }
    
    if (node.getNodeType()==node.NUMBER_NODE)
    {
      if (node.isNull() || ((DMDNumberNode)node).get().intValue()==-1)
      {
        return new DMDNode();
      }
      else
      {
        return node;
      }
    }
    
    return new DMDNode();
  }
  
  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    
    if (node.getNodeType() == node.TEXT_NODE)
    {
      String text;
      if (node.isNull())
      {
        text = "";
      }
      else
      {
        text = ((DMDTextNode)node).get();
      }
      setTag(item, text);
      joblist_.add(new DMDJobListItem(item));
      joblist_.itemModified(item);
    }
    
    if (node.getNodeType() == node.NUMBER_NODE)
    {
      Number number;      if (node.isNull())
      {        number = new Integer(-1);

      }
      else
      {
        number = ((DMDNumberNode)node).get();
      }      setTag(item, number);

      joblist_.add(new DMDJobListItem(item));
      joblist_.itemModified(item);
    }
    
  }
  
  public DMDNodeIterator get(String item) 
  {
    ArrayList nodelist = new ArrayList();
    boolean all = item.equals("/") || item.equals("/*");
      
    if(all || item.equals("/title"))   nodelist.add(getElement("/title"));
    if(all || item.equals("/artist"))  nodelist.add(getElement("/artist"));
    if(all || item.equals("/album"))   nodelist.add(getElement("/album"));
    if(all || item.equals("/comment")) nodelist.add(getElement("/comment"));
    if(all || item.equals("/year"))  nodelist.add(getElement("/year"));
    if(all || item.equals("/track"))   nodelist.add(getElement("/track"));
    if(all || item.equals("/genre"))   nodelist.add(getElement("/genre"));
     
    return new DMDNodeIterator(nodelist.iterator());
  }

  public void set(DMDNodeIterator iterator)
  {
    while (iterator.hasNext())
    {
      setElement(iterator.nextNode());
    }
  }
 
  public File getFile()
  {
    return file_;
  }

  public void setFile(File file) 
  {
    file_ = file;
  }

  public void setFileName(String filename) throws FileNotFoundException 
  {
    file_ = new File(filename);
  }
  
  /**
   * Creates a joblist for the store from a string item.
   * @param item name or path of the medafields ("/" means all fields)
   * @return a DMDJobList for the store
   */
  private DMDJobList createJobList(String item)
  {
    boolean all = false;
    DMDJobList joblist = new DMDJobList();
    if (item.equals("/*") || item.equals("/")) all = true;
    
    if (all || item.equals("/title")  ) joblist.add(new DMDJobListItem("/title"));
    if (all || item.equals("/artist") ) joblist.add(new DMDJobListItem("/artist"));
    if (all || item.equals("/album")  ) joblist.add(new DMDJobListItem("/album"));
    if (all || item.equals("/year")   ) joblist.add(new DMDJobListItem("/year"));
    if (all || item.equals("/comment")) joblist.add(new DMDJobListItem("/comment"));
    if (all || item.equals("/track")  ) joblist.add(new DMDJobListItem("/track"));
    if (all || item.equals("/genre")  ) joblist.add(new DMDJobListItem("/genre"));
    return joblist;
  }
  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean isValidID3v1()
  {
    return TAG.equals(new String(tag_));
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean setTag(String item, byte[] value) 
  {
    if (item.equals("/tag")) return copyByteArray(value, tag_);
    if (item.equals("/title")) return copyByteArray(value, songname_);
    if (item.equals("/artist")) return copyByteArray(value, artist_);
    if (item.equals("/album")) return copyByteArray(value, album_);
    if (item.equals("/year")) return copyByteArray(value, year_);
    if (item.equals("/comment")) return copyByteArray(value, comment_);
    if (item.equals("/track"))
    {
      track_[0] = (byte)0;
      if (value.length == 1) track_[1] = value[0];
      else track_[1] = (byte)0;
      return true;
    }
    if (item.equals("/genre")) return copyByteArray(value, genre_);
    return false;
  }
 
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean setTag(String item, Number value) 
  {
    //if (value.intValue()==-1) value = new Integer(0);
    byte[] bytevalue = new byte[1];
    bytevalue[0] = (value == null ? 0 : value.byteValue());
    return setTag(item, bytevalue);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean setTag(String item, String value) 
  {
    return setTag(item, value.getBytes());
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private byte[] getTag(String item)
  {
    if (item.equals("/tag")) return tag_;
    if (item.equals("/title")) return songname_;
    if (item.equals("/artist")) return artist_;
    if (item.equals("/album")) return  album_;
    if (item.equals("/year")) return year_;
    if (item.equals("/comment")) return comment_;
    if (item.equals("/track")) return track_;
    if (item.equals("/genre")) return genre_;
    return null;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private int getTagPosition(String item)
  {
    if (item.equals("/tag")) return 128;
    if (item.equals("/title")) return 125;
    if (item.equals("/artist")) return 95;
    if (item.equals("/album")) return  65;
    if (item.equals("/year")) return 35;
    if (item.equals("/comment")) return 31;
    if (item.equals("/track")) return 3;
    if (item.equals("/genre")) return 1;
    return 0;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean copyByteArray(byte[] src, byte[] dst)
  {
    if (dst.length < src.length)
    {
      System.err.println("Warning: '" + new String(src) + "' has more than " + dst.length + " letters!");
    }
    
    java.util.Arrays.fill(dst, (byte) 0);
    int index = 0;
    
    while (index<src.length && index<dst.length)
    {
      dst[index] = src[index];
      index++;
    }
    return true;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String toString()
  {
  
    return "Title:   " + getElement("/title")   + "\n" +
         "Artist:  " + getElement("/artist")  + "\n" +
         "Album:   " + getElement("/album")   + "\n" +
         "Year:  " + getElement("/year")  + "\n" +
         "Comment: " + getElement("/comment") + "\n" +
         "Track:   " + getElement("/track")   + "\n" +
         "Genre:   " + getElement("/genre")   + "\n" + joblist_;
  }
  
  private byte[] tag_ = new byte[3];
  private byte[] songname_ = new byte[30];
  private byte[] artist_ = new byte[30];
  private byte[] album_ = new byte[30];
  private byte[] year_ = new byte[4];
  private byte[] comment_ = new byte[28];
  private byte[] track_ = new byte[2];
  private byte[] genre_ = new byte[1];
  
  private static final String TAG = "TAG";
  private static final String[] FIELDS = {"/tag", "/title", "/artist", "/album", 
                      "/year", "/comment", "/track", "/genre"};
  
  
  private DMDJobList joblist_ = new DMDJobList();
  private File file_;   
}
