///////////////////////////////////////////////////////////////////////////////
//
// $Id: PDFv13Mapper.java,v 1.1 2003/02/27 21:56:13 krake Exp $
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


package org.dinopolis.utils.metadata.dinomeda;

// Java imports
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Date;
import java.util.Locale;
import java.lang.ArrayIndexOutOfBoundsException;
import java.net.URL;

// external packages

// local packages

import org.dinopolis.utils.metadata.*;

/**
 * @author Martin Oswald <ossi1@sbox.tugraz.at> (mandatory)
 * @version 0.2.1 (mandatory)
 *
 * Class or interface description (mandatory)
 */

public class PDFv13Mapper extends DinomedaMapper
{
  
  public int read(String item) throws IOException
  { 
    return store_.read(createJobList(item));
  }
  
  public int read(DMDJobList joblist) throws IOException
  {
    return store_.read(mapJobList(joblist));
  }
  
  public int read() throws IOException
  {
    return store_.read();
  }
  
  public int write() throws IOException
  {  
    return store_.write();
  }
  
  public int write(String item) throws IOException
  {
    return write(createJobList(item));
  }
  
  public int write(DMDJobList joblist) throws IOException
  {
    return store_.write(mapJobList(joblist));
  }
  
  public int update() throws IOException
  {
    return store_.update();
  }
  
  public int update(String item) throws IOException
  {
    return update(createJobList(item));
  }
  
  public int update(DMDJobList joblist) throws IOException
  {
    return store_.update(mapJobList(joblist));
  }
  
  public String getMIMEType()
  {
    return store_.getMIMEType();
  }
  
  private DMDJobList createJobList(String item)
  {
    DMDJobList joblist = new DMDJobList();
    boolean all = false;
    if (item.equals("/") || item.equals("/*")) all=true;
    
    if (all || item.equals("/title")) joblist.add(new DMDJobListItem("/title"));
    if (all || item.equals("/creator")) joblist.add(new DMDJobListItem("/author"));
    if (all || item.equals("/date"))
    {
      joblist.add(new DMDJobListItem("/creationdate"));
      joblist.add(new DMDJobListItem("/moddate"));
    }
    if (all || item.equals("/description")) joblist.add(new DMDJobListItem("/keywords"));
    if (all || item.equals("/type")) joblist.add(new DMDJobListItem("/subject"));
    return joblist;
  }
  
  private DMDJobList mapJobList(DMDJobList joblist)
  {
  
    DMDJobList storejoblist = new DMDJobList();
    
    for (int i=0; i<joblist.size();i++)
    {
      String job = joblist.get(i).getJobName();
      
      if (job.equals("/title")) storejoblist.add(new DMDJobListItem("/title"));
      if (job.equals("/creator")) storejoblist.add(new DMDJobListItem("/author"));
      if (job.equals("/date"))
      {
        storejoblist.add(new DMDJobListItem("/creationDate"));
        storejoblist.add(new DMDJobListItem("/moddate"));
      }
      if (job.equals("/description")) storejoblist.add(new DMDJobListItem("/keywords"));
      if (job.equals("/type")) storejoblist.add(new DMDJobListItem("/subect"));
     }
    
    return storejoblist;  
  }
  
  public String[] getTitle()
  { 
    DMDNode node = store_.getElement("/title");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{((DMDTextNode)node).get()};
    }
  }
  
  public int getMaxTitleCount()
  {
    return maxTitleCount;
  }
  
  public String[] getCreator()
  {
    
    DMDNode node = store_.getElement("/author");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{((DMDTextNode)node).get()};
    }
  }
  
  public int getMaxCreatorCount()
  {
    return maxCreatorCount;
  }
  
   
  public Date[] getDate()
  {
    DMDNode node1 = store_.getElement("/creationdate");
    DMDNode node2 = store_.getElement("/moddate");
    
    if (node1.isNull() && node2.isNull()) 
    {
      if(!node1.isNull()) return new Date[]{makeDate(((DMDTextNode)node1).get())};
      if(!node2.isNull()) return new Date[]{makeDate(((DMDTextNode)node2).get()),
                                            makeDate(((DMDTextNode)node2).get())};
      return new Date[0];
    }
    else
    {
      return new Date[]{makeDate(((DMDTextNode)node1).get()),
                        makeDate(((DMDTextNode)node2).get())};
    }
  }
  
  
  public int getMaxDateCount()
  {
    return maxDateCount;
  }
   
  
  public String[] getDescription()
  {
    DMDNode node = store_.getElement("/keywords");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{((DMDTextNode)node).get()};
    }
  }
  
  public int getMaxDescriptionCount()
  {
    return maxDescriptionCount;
  }
  
  
  public String[] getPublisher() 
  {
    return new String[]{};
  }
  
  public int getMaxPublisherCount()
  {
    return maxPublisherCount;
  }
  
  public String[] getContributor() 
  {
    return new String[]{};
  }
  
  public int getMaxContributorCount()
  {
    return maxContributorCount;
  }
  
  
  public String[] getType()
  {
    DMDNode node = store_.getElement("/subject");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{((DMDTextNode)node).get()};
    }
  }
  
  public int getMaxTypeCount()
  {
    return maxTypeCount;
  }
  
   
  public Locale[] getLanguage()
  {
    return new Locale[]{};
  }
  
  public int getMaxLanguageCount()
  {
    return maxLanguageCount;
  }
  
   
  public String[] getCollection() 
  {
    return new String[]{};
  }
  
  public int getMaxCollectionCount()
  {
    return maxCollectionCount;
  }
  
  
  public String[] getRights()
  {
    return new String[]{};
  }
  
  public int getMaxRightsCount()
  {
    return maxRightsCount;
  }
  
  
  public URL[] getIdentifier()
  {
    return new URL[]{};
  }
  
  public int getMaxIdentifierCount()
  {
    return maxIdentifierCount;
  }
  
  
  public URL[] getSource()
  {
    return new URL[]{};
  }
  
  public int getMaxSourceCount()
  {
    return maxSourceCount;
  }
  
  public URL[] getRelation() 
  {
    return new URL[]{};
  }
  
  public int getMaxRelationCount()
  {
    return maxRelationCount;
  }
  
  public void setTitle(String[] title) throws ArrayIndexOutOfBoundsException
  {
    if (title.length > maxTitleCount) throw new ArrayIndexOutOfBoundsException();
    if (title.length == 0)
    {
      store_.setElement(new DMDTextNode("/","title",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","title",title[0]));  
    }
   
  }
  
  public void setCreator(String[] creator) throws ArrayIndexOutOfBoundsException
  {
    if (creator.length > maxCreatorCount) throw new ArrayIndexOutOfBoundsException();
    if (creator.length == 0)
    {
      store_.setElement(new DMDTextNode("/","author",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","author",creator[0]));  
    }
     
  }
  
  public void setDate(Date[] date) throws ArrayIndexOutOfBoundsException
  {
    if (date.length > maxDateCount) throw new ArrayIndexOutOfBoundsException();
    if (date.length == 0)
    {
      store_.setElement(new DMDTextNode("/","creationdate",null));      store_.setElement(new DMDTextNode("/","moddate",null));  
    }
    else
    {
      if (date.length == 1) 
      {
        store_.setElement(new DMDTextNode("/","creationdate",pdfDate(date[0])));
        store_.setElement(new DMDTextNode("/","moddate",pdfDate(date[0])));
      }
      if (date.length == 2)
      {
        store_.setElement(new DMDTextNode("/","creationdate",pdfDate(date[0])));
        store_.setElement(new DMDTextNode("/","moddate",pdfDate(date[1])));
      }
    }
  }
  
  public void setDescription(String[] description) throws ArrayIndexOutOfBoundsException
  {
    if (description.length > maxDescriptionCount) throw new ArrayIndexOutOfBoundsException(); 
    if (description.length == 0)
    {
      store_.setElement(new DMDTextNode("/","keywords",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","keywords",description[0]));  
    }
  }
  
  public void setPublisher(String[] publisher) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException(); 
  }
  
  public void setContributor(String[] contributor) throws ArrayIndexOutOfBoundsException
  {
     throw new ArrayIndexOutOfBoundsException();
  }
  
  public void setType(String[] type) throws ArrayIndexOutOfBoundsException
  {
    if (type.length > maxTypeCount) throw new ArrayIndexOutOfBoundsException();
    if (type.length == 0)
    {
      store_.setElement(new DMDTextNode("/","subject",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","subject",type[0]));  
    }
   
  }
  
  public void setLanguage(Locale[] language) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException();  
  }
  
  public void setCollection(String[] collection) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException();  
  } 
  
  public void setRights(String[] rights) throws ArrayIndexOutOfBoundsException
  {
     throw new ArrayIndexOutOfBoundsException(); 
  }
  
  public void setIdentifier(URL[] identifier) throws ArrayIndexOutOfBoundsException
  {
     throw new ArrayIndexOutOfBoundsException(); 
  }
  
  public void setSource(URL[] source) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException();   
  }
  
  public void setRelation(URL[] relation) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException();   
  }
  
  private Date makeDate(String pdfdate)
  {
    GregorianCalendar date = new GregorianCalendar();  
    
    if (pdfdate.startsWith("D:"))
    {
      date.set(date.YEAR,(new Integer(pdfdate.substring(2,6))).intValue());
      date.set(date.MONTH,(new Integer(pdfdate.substring(6,8))).intValue()-1);
      date.set(date.DAY_OF_MONTH,(new Integer(pdfdate.substring(8,10))).intValue());
      date.set(date.HOUR_OF_DAY,(new Integer(pdfdate.substring(10,12))).intValue());
      date.set(date.MINUTE,(new Integer(pdfdate.substring(12,14))).intValue());
      date.set(date.SECOND,(new Integer(pdfdate.substring(14,16))).intValue());
    
    }
    
    return date.getTime();
    
  }
  
  private String pdfDate(Date pdate)
  {
    
    GregorianCalendar date = new GregorianCalendar();
    date.setTime(pdate);
    int year = date.get(date.YEAR);
    int month = date.get(date.MONTH)+1;
    int day = date.get(date.DAY_OF_MONTH);
    int hours = date.get(date.HOUR_OF_DAY);
    int minutes = date.get(date.MINUTE);
    int seconds = date.get(date.SECOND);
    
    String yyyy;
    String mm;
    String dd;
    String hh;
    String mi;
    String ss;
    
    yyyy = "" + year;
    
    if (month<10) mm = "0" + month;
    else mm = "" + month;
    
    if (day<10) dd = "0" + day;
    else dd = "" + day;
    
    if (hours<10) hh = "0" + hours;
    else hh = "" + hours;
    
    if (minutes<10) mi = "0" + minutes;
    else mi = "" + minutes;
    
    if (seconds<10) ss = "0" + seconds;
    else ss = "" + seconds;
    
    return "D:" + yyyy + mm + dd + hh + mi + ss;
  }
   
  private int maxTitleCount = 1;
  private int maxCreatorCount = 1;
  private int maxDateCount = 2;
  private int maxDescriptionCount = 1;
  private int maxPublisherCount = 0;
  private int maxContributorCount = 0;
  private int maxTypeCount = 1;
  private int maxLanguageCount = 0;
  private int maxCollectionCount = 0;
  private int maxRightsCount = 0;
  private int maxIdentifierCount = 0;
  private int maxSourceCount = 0;
  private int maxRelationCount = 0;
  
}
