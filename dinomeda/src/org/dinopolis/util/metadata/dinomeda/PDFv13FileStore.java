///////////////////////////////////////////////////////////////////////////////
//
// $Id: PDFv13FileStore.java,v 1.5 2003/04/24 09:08:49 osma Exp $
//
// Copyright: Martin Oswald <ossi1@sbox.tugraz.at>, 2003
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
 * @author Martin Oswald <oss1@sbox.tugraz.at>
 * @version 0.2.1
 */

/**
 * Store working with metadata in PDF files
 */

public class PDFv13FileStore implements DMDFileStore
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
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
    
    for (int i=0; i<joblist_.size();i++)
    {
      String job = joblist_.get(i).getJobName();
      
      if ((joblist_.get(i)).getStatus()!=1)
      {       
        System.err.println("PDFv13FileStore -> read " + job);
    
        getTag(job);
      
        (joblist_.get(i)).itemRead();
        count++;
        
      }
      else
      {
        System.err.println("PDFv13FileStore -> "+ job + " already read");
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
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
    
    for (int i=0; i<joblist.size();i++)
    {
      String job = joblist.get(i).getJobName();
      
      if ((joblist_.get(job)).getStatus()!=1)
      {       
        System.err.println("PDFv13FileStore -> read " + job);
    
        getTag(job);
      
        (joblist_.get(job)).itemRead();
        count++;
        
      }
      else
      {
        System.err.println("PDFv13FileStore -> "+ job + " already read");
      }
    }
    file.close();
    return count;  
  }  
  
  public int updateMetaData() throws IOException
  {
    int count = 0;

    RandomAccessFile file = new RandomAccessFile(file_, "rw");
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
    
    for (int i = 0; i < FIELDS.length; i++)
    {
        
      if (joblist_.contains(FIELDS[i]) && joblist_.get(FIELDS[i]).getStatus()==2)
      {
        System.err.println("PDFv13FileStore -> update " + FIELDS[i] + " '" + getElement(FIELDS[i]) + "'");
        (joblist_.get(FIELDS[i])).itemWrote();   
        count++;
      }
      else
      {
        getTag(FIELDS[i]);
      }
    }
    
    writeIntoPDFFile(file);
    
    file.close();
    
    return count;
  }

  public int updateMetaData(String item) throws IOException
  {
    return updateMetaData(createJobList(item)); 
  }

  public int updateMetaData(DMDJobList joblist) throws IOException
  {
    int count = 0;

    RandomAccessFile file = new RandomAccessFile(file_, "rw");
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
    
    for (int i = 0; i < FIELDS.length; i++)
    {
        
      if (joblist.contains(FIELDS[i]) && joblist_.contains(FIELDS[i]) && joblist_.get(FIELDS[i]).getStatus()==2)
      {
        System.err.println("PDFv13FileStore -> update " + FIELDS[i] + " '" + getElement(FIELDS[i]) + "'");
        (joblist_.get(FIELDS[i])).itemWrote();   
        count++;
      }
      else
      {
        getTag(FIELDS[i]);
      }
    }
    
    writeIntoPDFFile(file);
    
    file.close();
    
    return count;
  }

  public int writeMetaData() throws IOException 
  {  
    
    int count=0;
    RandomAccessFile file = new RandomAccessFile(file_, "rw");
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
    
    if (pdfInfoDict_.indexOf("/Dinomeda (yes)")==-1)
    {
      pdfInfoDictPosition_ = pdfCRTPosition_; 
    }
    
    file.seek(pdfInfoDictPosition_);
    
    String pdfCRTItem = new String("0000000000" + pdfInfoDictPosition_);
    pdfCRT_.set(pdfInfo_+1, pdfCRTItem.substring(pdfCRTItem.length()-10,pdfCRTItem.length()) + " 00000 n ");
    
    for (int i = 0; i < FIELDS.length; i++)
    {
      if (joblist_.contains(FIELDS[i]) && (joblist_.get(FIELDS[i])).getStatus()>0)
      {
        (joblist_.get(FIELDS[i])).itemWrote();
        System.err.println("PDFv13FileStore -> write " + FIELDS[i] + " '" + getElement(FIELDS[i])+ "'");
        
      }
      else
      {
        setElement(new DMDTextNode("/",FIELDS[i].substring(1), ""));
        joblist_.remove(FIELDS[i]);
        System.err.println("PDFv13FileStore -> delete " + FIELDS[i]);
      }
      count++;
    }
    
    writeIntoPDFFile(file);
    
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
    
    readTrailer(file);
    readCRT(file);
    readInfoDict(file);
     
    for (int i = 0; i < FIELDS.length; i++)
    {
      if (joblist.contains(FIELDS[i]))
      {
        
        (joblist_.get(FIELDS[i])).itemWrote();
        System.err.println("PDFv13FileStore -> write " + FIELDS[i] + " '" + getElement(FIELDS[i])+"'");
        count++;
      }
      else
      {
        if (joblist_.contains(FIELDS[i]))
        {
          setElement(new DMDTextNode("/",FIELDS[i].substring(1), ""));
          joblist_.remove(FIELDS[i]);
          System.err.println("PDFv13FileStore -> delete " + FIELDS[i]);
          count++;
        }
         else
        {
          getTag(FIELDS[i]);
        }
      }
      
      
    }
    
    writeIntoPDFFile(file);
    
    file.close();
    return count;
  }
  
  public String getMIMEType()
  {
    return "application/pdf";
  }
  
  public DMDNode getElement(String path)
  {
    DMDNode node = new DMDNode();
    
    if (joblist_.contains(path))
      {
        if (path.equals("/title")        ) node = new DMDTextNode("/","title",title_);
        if (path.equals("/subject")      ) node = new DMDTextNode("/","subject",subject_);
        if (path.equals("/author")       ) node = new DMDTextNode("/","author",author_);
        if (path.equals("/keywords")     ) node = new DMDTextNode("/","keywords",keywords_);
        if (path.equals("/creator")      ) node = new DMDTextNode("/","creator",creator_);
        if (path.equals("/producer")     ) node = new DMDTextNode("/","producer",producer_);
        if (path.equals("/creationdate") ) node = new DMDTextNode("/","creationdate",creationdate_);
        if (path.equals("/moddate")      ) node = new DMDTextNode("/","moddate",moddate_);
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
    
    return new DMDNode();
  }
  
  public void setElement(DMDNode node)
  {
    String item = node.getPath() + node.getName();
    String value;
    if (node.isNull())
    {
      value = "";
    }
    else
    {
      value = ((DMDTextNode)node).get();
    }
    
    if (item.equals("/author")) author_ = value;
    if (item.equals("/creationdate")) creationdate_ = value;
    if (item.equals("/moddate")) moddate_ = value;
    if (item.equals("/creator"))  creator_ = value;
    if (item.equals("/producer"))  producer_ = value;
    if (item.equals("/title"))  title_ = value;
    if (item.equals("/subject")) subject_ = value;
    if (item.equals("/keywords")) keywords_ = value;
    
    joblist_.add(new DMDJobListItem(item));
    joblist_.itemModified(item);
  }
  
  public DMDNodeIterator get(String item) 
  {
    ArrayList nodelist = new ArrayList();
    boolean all = item.equals("/") || item.equals("/*");
      
    if(all || item.equals("/title")     ) nodelist.add(getElement("/title"));
    if(all || item.equals("/subject")   ) nodelist.add(getElement("/subject"));
    if(all || item.equals("/author")    ) nodelist.add(getElement("/author"));
    if(all || item.equals("/keywords")  ) nodelist.add(getElement("/keywords"));
    if(all || item.equals("/creator")   ) nodelist.add(getElement("/creator"));
    if(all || item.equals("/producer")  ) nodelist.add(getElement("/producer"));
    if(all || item.equals("/creationdate")) nodelist.add(getElement("/creationdate"));
    if(all || item.equals("/moddate")   ) nodelist.add(getElement("/moddate"));
     
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
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void readTrailer(RandomAccessFile file) throws IOException
  {
    String str = "";
    int index;
    byte buffer[] = new byte[1];
    long position = file.length()-1;
    
    
    //read startxref
    while (!str.startsWith("startxref"))
    {
      file.seek(position--);
      file.read(buffer);
      str = new String(buffer) + str;
    }
    
   
    pdfCRTPosition_ = toNumber(str.substring(10));
    
    str = "";
     
     //read Trailer
    while (!str.startsWith("trailer"))
    {
      file.seek(position--);
      file.read(buffer);
      str = new String(buffer) + str;
    }
     
    pdfTrailer_ = str;
 
    if ((index=pdfTrailer_.indexOf("/Info"))!=-1)
    {
      pdfInfo_ = (int)toNumber(pdfTrailer_.substring(index+6));
    }
    else
    {
      throw new IOException();
    }
  
    if ((index=pdfTrailer_.indexOf("/Size"))!=-1)
    {
      pdfSize_ = (int)toNumber(pdfTrailer_.substring(index+6));
    }
    else
    {
      throw new IOException();
    }
     
    if ((index=pdfTrailer_.indexOf("/Root"))!=-1)
    {
      pdfRoot_ = (int)toNumber(pdfTrailer_.substring(index+6));
    }
    else
    {
      throw new IOException();
    }
 
    if ((index=pdfTrailer_.indexOf("/Prev"))!=-1)
    {
      pdfPrev_ = (int)toNumber(pdfTrailer_.substring(index+6));
    }
    else
    {
      //System.err.println("Info: Trailer contains no refernce to a previous cross-refernce table!");
    }
    
    if ((index=pdfTrailer_.indexOf("/Encrypt"))!=-1)
    {
      pdfEncrypt_ = (int)toNumber(pdfTrailer_.substring(index+9));
    }
    else
    {
      //System.err.println("Info: PDF-file is not encrypted!");
    }
    
    if ((index=pdfTrailer_.indexOf("/ID"))!=-1)
    {
      pdfID_ = pdfTrailer_.substring(pdfTrailer_.indexOf("[",index)+1,
                       pdfTrailer_.indexOf("]",index));
    }
    else
    {
      //System.err.println("Info: No ID");
    }
   
    //System.err.println(pdfTrailer_);
  
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void readInfoDict(RandomAccessFile file) throws IOException
  {
    pdfInfoDictPosition_ = toNumber((String)pdfCRT_.get(pdfInfo_+1));
    
    file.seek(pdfInfoDictPosition_);
    
    while (!pdfInfoDict_.endsWith("endobj\n"))
    { 
      pdfInfoDict_ += file.readLine() + "\n";   
    }
    
    //System.err.println(pdfInfoDict_);
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void getTag(String item)
  {
    int index;
    
    if (item.equals("/author") && (index=pdfInfoDict_.indexOf("/Author"))!=-1)
    {
      author_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/creationdate") && (index=pdfInfoDict_.indexOf("/CreationDate"))!=-1)
    {
      creationdate_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/moddate") && (index=pdfInfoDict_.indexOf("/ModDate"))!=-1)
    {
      moddate_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/creator") && (index=pdfInfoDict_.indexOf("/Creator"))!=-1)
    {
      creator_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/producer") && (index=pdfInfoDict_.indexOf("/Producer"))!=-1)
    {
      producer_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/title") && (index=pdfInfoDict_.indexOf("/Title"))!=-1)
    {
      title_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/subject") && (index=pdfInfoDict_.indexOf("/Subject"))!=-1)
    {
      subject_ = readTag(pdfInfoDict_,index);
    }
    
    if (item.equals("/keywords") && (index=pdfInfoDict_.indexOf("/Keywords"))!=-1)
    {
      keywords_ = readTag(pdfInfoDict_,index);
    }
    
 
  }  
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private String readTag(String str,int index) 
  {
    return str.substring(str.indexOf("(",index)+1,str.indexOf(")",index));
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void writeIntoPDFFile(RandomAccessFile file) throws IOException
  {
    
    if (pdfInfoDict_.indexOf("/Dinomeda (yes)")==-1)
    {
      pdfInfoDictPosition_ = pdfCRTPosition_; 
    }
    
    file.seek(pdfInfoDictPosition_);
     
    String pdfCRTItem = new String("0000000000" + pdfInfoDictPosition_);
    pdfCRT_.set(pdfInfo_+1, pdfCRTItem.substring(pdfCRTItem.length()-10,pdfCRTItem.length()) + " 00000 n ");
    
    file.write(createInfoDict());
    pdfCRTPosition_ = file.getFilePointer();
    file.write(createCRT());
    file.write(createTrailer());
    file.setLength(file.getFilePointer());
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private boolean isNumber(char chr)
  {
    if (chr=='0') return true;
    if (chr=='1') return true;
    if (chr=='2') return true;
    if (chr=='3') return true;
    if (chr=='4') return true;
    if (chr=='5') return true;
    if (chr=='6') return true;
    if (chr=='7') return true;
    if (chr=='8') return true;
    if (chr=='9') return true;
                
    return false;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private long toNumber(String str)
  {
    
    char chr[] = str.toCharArray();
    char buffer[] = new char[chr.length];
    int index = 0;
    
    //buffer[0]='0';
  
    while (index < chr.length && isNumber(chr[index]))
    {
      buffer[index] = chr[index];
      index++;
    }
     
    //System.err.println("number: " +new String(buffer));
   
    return (new Integer(new String(buffer).trim())).longValue();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void readCRT(RandomAccessFile file) throws IOException
  {
    ArrayList pdfCRT = new ArrayList();
    
    file.seek(pdfCRTPosition_);
    if (file.readLine().startsWith("xref"))
    {
      for (int i=0;i<=pdfSize_;i++)
      {
        pdfCRT.add(file.readLine());
      }
      
      pdfCRT_ = pdfCRT;
    }   
    else
    {
      throw new IOException();
    }
 
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
    
    if (all || item.equals("/title")     ) joblist.add(new DMDJobListItem("/title"));
    if (all || item.equals("/subject")   ) joblist.add(new DMDJobListItem("/subject"));
    if (all || item.equals("/author")    ) joblist.add(new DMDJobListItem("/author"));
    if (all || item.equals("/keywords")  ) joblist.add(new DMDJobListItem("/keywords"));
    if (all || item.equals("/creator")   ) joblist.add(new DMDJobListItem("/creator"));
    if (all || item.equals("/producer")  ) joblist.add(new DMDJobListItem("/producer"));
    if (all || item.equals("/creationDate")) joblist.add(new DMDJobListItem("/creationdate"));
    if (all || item.equals("/modDate")   ) joblist.add(new DMDJobListItem("/moddate"));
    return joblist;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private byte[] createInfoDict() 
  {
    String output = pdfInfo_ + " 0 obj" + "\n" +
            "<<" +"\n" +
            "/Creator (" + creator_ + ")\n" +
            "/CreationDate (" + creationdate_ + ")\n" +
            "/ModDate (" + moddate_ + ")\n" +
            "/Title (" + title_ + ")\n" +
            "/Producer (" + producer_ + ")\n" +
            "/Subject (" + subject_ + ")\n" +
            "/Author (" + author_ + ")\n" +
            "/Keywords (" + keywords_ + ")\n" +
            "/Dinomeda (yes)\n" +
            ">>" + "\n" +
            "endobj" + "\n";
    
    return output.getBytes();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private byte[] createCRT()
  {
    String output = "xref\n";
    
    for (int i=0;i<pdfCRT_.size();i++)
    {
      output += (String)pdfCRT_.get(i) + "\n";
    }
    return output.getBytes();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private byte[] createTrailer()
  {
    String output = "trailer\n" +
            "<<\n" +
            "/Size " + pdfSize_ + "\n" +
            "/Root " + pdfRoot_ + " 0 R\n";
    
    if (pdfInfo_ != -1) output += "/Info " + pdfInfo_ + " 0 R\n";
    if (pdfPrev_ != -1) output += "/Prev " + pdfPrev_ + "\n";
    if (pdfEncrypt_ != -1) output += "/Encrypt " + pdfEncrypt_ + " 0 R\n";
    if (pdfID_ != null) output += "/ID [" + pdfID_ + "]\n";
    
    output += ">>\n" +
          "startxref\n" +
          pdfCRTPosition_ + "\n" +
          "%%EOF\n";
    
    return output.getBytes();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public String toString()
  {
  
    return "Title:        " + title_         + "\n" +
           "Subject:      " + subject_       + "\n" +
           "Author:       " + author_        + "\n" +
           "Keywords:     " + keywords_      + "\n" +
           "Creator:      " + creator_       + "\n" +
           "Producer:     " + producer_      + "\n" +
           "CreationDate: " + creationdate_  + "\n" +
           "ModDate:      " + moddate_       + "\n" + joblist_;
  }
 
   
  private String author_ = ""; 
  private String creationdate_ = ""; 
  private String moddate_ = "";
  private String creator_ = "";
  private String producer_ = "";
  private String title_ = "";
  private String subject_ = "";
  private String keywords_ = "";
  
  private long pdfCRTPosition_; // Location of cross-reference table
  private long pdfInfoDictPosition_; // Location of info dictionary
  
  private int pdfSize_ = -1; // /Size
  private int pdfInfo_ = -1; // /Info
  private int pdfRoot_ = -1; // /Root
  private int pdfPrev_ = -1; // /Prev
  private String pdfID_ = null; // /ID
  private int pdfEncrypt_ = -1; // /Encrypt
  
  private String pdfVersion_;
  
  private ArrayList pdfCRT_ = new ArrayList(); //cross-refernce table
  private String pdfInfoDict_ = ""; //info dictionary
  private String pdfTrailer_ = ""; // trailer
  
  private static String[] FIELDS = {"/title","/subject","/author","/keywords",
                                    "/creator","/producer","/creationdate","/moddate"};
  
  private DMDJobList joblist_ = new DMDJobList();
  private File file_;
  
}
