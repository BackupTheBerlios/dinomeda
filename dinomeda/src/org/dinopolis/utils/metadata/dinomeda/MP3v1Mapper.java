///////////////////////////////////////////////////////////////////////////////
//
// $Id: MP3v1Mapper.java,v 1.3 2003/03/02 19:42:20 krake Exp $
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

public class MP3v1Mapper extends DinomedaMapper
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
    if (all || item.equals("/creator")) joblist.add(new DMDJobListItem("/artist"));
    if (all || item.equals("/date")) joblist.add(new DMDJobListItem("/year"));
    if (all || item.equals("/description")) joblist.add(new DMDJobListItem("/comment"));
    if (all || item.equals("/type")) joblist.add(new DMDJobListItem("/genre"));
    if (all || item.equals("/collection")) joblist.add(new DMDJobListItem("/album"));
    return joblist;
  }
  
  
  private DMDJobList mapJobList(DMDJobList joblist)
  {
  
    DMDJobList storejoblist = new DMDJobList();
    
    for (int i=0; i<joblist.size();i++)
    {
      String job = joblist.get(i).getJobName();
      
      if (job.equals("/title")) storejoblist.add(new DMDJobListItem("/title"));
      if (job.equals("/creator")) storejoblist.add(new DMDJobListItem("/artist"));
      if (job.equals("/date")) storejoblist.add(new DMDJobListItem("/year"));
      if (job.equals("/description")) storejoblist.add(new DMDJobListItem("/comment"));
      if (job.equals("/type")) storejoblist.add(new DMDJobListItem("/genre"));
      if (job.equals("/collection")) storejoblist.add(new DMDJobListItem("/album"));  
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
    DMDNode node = store_.getElement("/artist");
    
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
    DMDNode node = store_.getElement("/year");
    
    if (node.isNull()) 
    {
      return new Date[0];
    }
    else
    {
      return new Date[]{makeDate(((DMDTextNode)node).get())};
    }
  }
  
  public int getMaxDateCount()
  {
    return maxDateCount;
  }
   
  public String[] getDescription()
  {
    DMDNode node = store_.getElement("/comment");
    
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
    DMDNode node = store_.getElement("/genre");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{getGenreName(((DMDNumberNode)node).get())};
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
    DMDNode node = store_.getElement("/album");
    
    if (node.isNull()) 
    {
      return new String[0];
    }
    else
    {
      return new String[]{((DMDTextNode)node).get()};
    }
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
      store_.setElement(new DMDTextNode("/","artist",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","artist",creator[0]));  
    }
  }
  
  public void setDate(Date[] date) throws ArrayIndexOutOfBoundsException
  {
    if (date.length > maxDateCount) throw new ArrayIndexOutOfBoundsException();
    if (date.length == 0)
    {
      store_.setElement(new DMDTextNode("/","year",null));
    }
    else
    {
      store_.setElement(new DMDTextNode("/","year",year(date[0])));
    }
  }
  
  public void setDescription(String[] description) throws ArrayIndexOutOfBoundsException
  {
    if (description.length > maxDescriptionCount) throw new ArrayIndexOutOfBoundsException();
    if (description.length == 0)
    {
      store_.setElement(new DMDTextNode("/","comment",null));  
    }
    else
    {
      store_.setElement(new DMDTextNode("/","comment",description[0]));  
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
      store_.setElement(new DMDNumberNode("/","genre"));
    }
    else
    {
      store_.setElement(new DMDNumberNode("/","genre",getGenreIndex(type[0])));
    }
  }
  
  public void setLanguage(Locale[] language) throws ArrayIndexOutOfBoundsException
  {
    throw new ArrayIndexOutOfBoundsException();  
  }
  
  public void setCollection(String[] collection) throws ArrayIndexOutOfBoundsException
  {
    if (collection.length > maxCollectionCount) throw new ArrayIndexOutOfBoundsException();
    if (collection.length == 0)
    {
      store_.setElement(new DMDTextNode("/","album", null));
    }
    else
    {
      store_.setElement(new DMDTextNode("/","album",collection[0]));  
    }
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
  
  private String getGenreName(Number index)
  {
    return genres_[index.intValue()]; 
  }
  
  private Integer getGenreIndex(String name)
  {
    for (int index=0;index<genres_.length;index++)
    {
      if ((name).equals(genres_[index])) return new Integer(index);
    }
    Integer alternative = findAlternative(name);
    System.err.println("Warning: Not able to map type '" + name + "'!" + " Using type '" + getGenreName(alternative) + "' instead.");
     
    return alternative;
  }
  
  private Integer findAlternative(String name)
  {
    int matches = 0;
    int maxmatches = 0;
    int alternative = 0;
    
    for (int index=0;index<genres_.length;index++)
    {
      matches = compare(name,genres_[index]);
      if (matches>maxmatches)
      {
        alternative = index;
        maxmatches = matches;
      }
    }
       
    return new Integer(alternative);
  }
  
  private int compare(String str1, String str2)
  {
    int matches = 0;
    int start = 0;
    
    char[] chr1 = str1.toCharArray();
    char[] chr2 = str2.toCharArray();
    
    if (chr1.length==chr2.length) matches++;
    
    for(int i=0;i<chr1.length;i++)
    {
      for(int j=start;j<chr2.length;j++)
      {
        if (chr2[j]==chr1[i])
        {
          matches++;
          chr2[j]='~';
          chr1[i]='#';
          start=j;
        }
      }
    }
    return matches;
  }
  
  private Date makeDate(String year)
  {
    GregorianCalendar date = new GregorianCalendar();  
    
    if (!year.startsWith(" "))
    {
      try
      {
        date.set(date.YEAR,(new Integer(year)).intValue());
      }
      catch (NumberFormatException number_excpetion)
      {
      }      
      date.set(date.MONTH,0);
      date.set(date.DAY_OF_MONTH,1);
      date.set(date.HOUR,0);
      date.set(date.MINUTE,0);
      date.set(date.SECOND,0);
      
    }
    return date.getTime();
  }
  
  private String year(Date pdate)
  {
    GregorianCalendar date = new GregorianCalendar();
    date.setTime(pdate);
    
    int year = date.get(date.YEAR);  
    
    return "" + year;
  }
  
  private int maxTitleCount = 1;
  private int maxCreatorCount = 1;
  private int maxDateCount = 1;
  private int maxDescriptionCount = 1;
  private int maxPublisherCount = 0;
  private int maxContributorCount = 0;
  private int maxTypeCount = 1;
  private int maxLanguageCount = 0;
  private int maxCollectionCount = 1;
  private int maxRightsCount = 0;
  private int maxIdentifierCount = 0;
  private int maxSourceCount = 0;
  private int maxRelationCount = 0;
  
  private final static String[] genres_ = {"Blues", "Classic Rock", "Country", 
                                           "Dance", "Disco", "Funk", "Grunge",
                                           "Hip-Hop", "Jazz", "Metal", "New Age",
                                           "Oldies", "Other", "Pop", "R&B", "Rap", 
                                           "Reggae", "Rock", "Techno", "Industrial",
                                           "Alternative", "Ska", "Death Metal", "Pranks", 
                                           "Soundtrack", "Euro-Techno", "Ambient", "Trip-Hop",
                                           "Vocal", "Jazz+Funk", "Fusion", "Trance", 
                                           "Classical", "Instrumental", "Acid", "House", 
                                           "Game", "Sound Clip", "Gospel", "Noise", 
                                           "AlternRock", "Bass", "Soul", "Punk", "Space",
                                           "Meditative", "Instrumental Pop", "Instrumental Rock", 
                                           "Ethnic", "Gothic", "Darkwave", "Techno-Industrial",
                                           "Electronic", "Pop-Folk", "Eurodance", "Dream", 
                                           "Southern Rock", "Comedy", "Cult", "Gangsta", "Top 40", 
                                           "Christian Rap", "Pop/Funk", "Jungle", "Native American",
                                           "Cabaret", "New Wave", "Psychadelic", "Rave", "Showtunes",
                                           "Trailer", "Lo-Fi", "Tribal", "Acid Punk", "Acid Jazz",
                                           "Polka", "Retro", "Musical", "Rock & Roll", "Hard Rock",
                                           "Folk", "Folk-Rock", "National Folk", "Swing", 
                                           "Fast Fusion", "Bebob", "Latin", "Revival", 
                                           "Celtic", "Bluegrass", "Avantgarde", "Gothic Rock", 
                                           "Progressive Rock", "Psychedelic Rock", "Symphonic Rock", 
                                           "Slow Rock", "Big Band", "Chorus", "Easy Listening", 
                                           "Acoustic", "Humour", "Speech", "Chanson", "Opera", 
                                           "Chamber Music", "Sonata", "Symphony", "Booty Brass", 
                                           "Primus", "Porn Groove", "Satire", "Slow Jam", "Club", 
                                           "Tango", "Samba", "Folklore", "Ballad", "Power Ballad", 
                                           "Rhythmic Soul", "Freestyle", "Duet", "Punk Rock", 
                                           "Drum Solo", "A Capela", "Euro-House", "Dance Hall"};
}
