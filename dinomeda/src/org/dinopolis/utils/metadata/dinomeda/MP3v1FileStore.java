///////////////////////////////////////////////////////////////////////////////
//
// $Id: MP3v1FileStore.java,v 1.3 2003/03/02 19:42:20 krake Exp $
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


package org.dinopolis.utils.metadata.dinomeda;

// Java imports
import java.io.RandomAccessFile;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// external packages

// local packages
import org.dinopolis.utils.metadata.*;

/**
 * @author Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.5.0
 *
 * Class or interface description (mandatory)
 */

public class MP3v1FileStore implements DMDFileStore
{
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public MP3v1FileStore()
  {
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public MP3v1FileStore(String filename) throws FileNotFoundException 
  {
    setFileName(filename);
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
   * Method description
   */
  public int read() throws IOException 
  {
    int count = 0;
    RandomAccessFile file = new RandomAccessFile(file_, "r");
    file.seek(file.length() - getTagPosition("/tag"));   
    file.read(getTag("/tag"));
  
    if (!isValidID3v1())
    {
      System.err.println("Warning: No MP3IDv1 tags found! Use write() to create tags.");
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
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int read(String item) throws IOException 
  {
    return read(createJobList(item)); 
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int read(DMDJobList joblist)  throws IOException 
  {
    joblist_.add(joblist);
    
    int count = 0;
    RandomAccessFile file = new RandomAccessFile(file_, "r");
    file.seek(file.length() - getTagPosition("/tag"));   
    file.read(getTag("/tag"));
  
    if (!isValidID3v1())
    {
      System.err.println("Warning: No MP3IDv1 tags found! Use write() to create tags.");
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
  
   
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update() throws IOException
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

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update(String item) throws IOException
  {
    return update(createJobList(item)); 
  }


  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int update(DMDJobList joblist) throws IOException
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

  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write() throws IOException 
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
  

  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write(String item) throws IOException 
  {
    return write(createJobList(item));
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int write(DMDJobList joblist) throws IOException 
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
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String getMIMEType()
  {
    return "audio/x-mp3";
  }

   
  //---------------------------------------------------------------
  /**
  * Method description
  */
  public DMDNode getElement(String path)
  {
    if (joblist_.contains(path))
      {
        if (path.equals("/title")  ) return new DMDTextNode("/","title",(new String(getTag(path)).trim()));
        if (path.equals("/artist") ) return new DMDTextNode("/","artist",(new String(getTag(path)).trim()));
        if (path.equals("/album")  ) return new DMDTextNode("/","album",(new String(getTag(path)).trim()));
        if (path.equals("/year")   ) return new DMDTextNode("/","year",(new String(getTag(path)).trim()));
        if (path.equals("/comment")) return new DMDTextNode("/","comment",(new String(getTag(path)).trim()));
        if (path.equals("/track")  ) return new DMDNumberNode("/","track",new Integer(getTag(path)[1]));
        if (path.equals("/genre")  ) return new DMDNumberNode("/","genre",new Integer(getTag(path)[0]));
      }
      
    return new DMDNode();
  }
  
  //---------------------------------------------------------------
  /**
  * Method description
  */
  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    
    if (node.getNodeType() == 1)
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
    
    if (node.getNodeType() == 2)
    {
      setTag(item, ((DMDNumberNode)node).get());
      joblist_.add(new DMDJobListItem(item));
      joblist_.itemModified(item);
    }
    
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
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

  //---------------------------------------------------------------
  /**
  * Method description
  */
  public void set(DMDNodeIterator iterator)
  {
    while (iterator.hasNext())
    {
      setElement(iterator.nextNode());
    }
  }
 
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public File getFile()
  {
    return file_;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setFile(File file) 
  {
    file_ = file;
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public void setFileName(String filename) throws FileNotFoundException 
  {
    file_ = new File(filename);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
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
