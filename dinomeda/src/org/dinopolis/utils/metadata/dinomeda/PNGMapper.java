///////////////////////////////////////////////////////////////////////////////
//
// $Id: PNGMapper.java,v 1.1 2003/02/27 21:56:13 krake Exp $
//
// Copyright: Mattias Welponer <mattias@welponer.net>, year
//
///////////////////////////////////////////////////////////////////////////////
//                                                                          
//   This program is free software{} you can redistribute it and/or modify  
//   it under the terms of the GNU Lesser General Public License as        
//   published by the Free Software Foundation{} either version 2 of the    
//   License, or (at your option) any later version.                       
//                                                                         
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.utils.metadata.dinomeda;

// Java imports
import java.util.Date;
import java.util.Locale;
import java.lang.ArrayIndexOutOfBoundsException;
import java.net.URL;
import java.io.IOException;

// external packages

// local packages
import org.dinopolis.utils.metadata.dinomeda.DinomedaMapper;
import org.dinopolis.utils.metadata.DMDStore;
import org.dinopolis.utils.metadata.*;

/**
 * @author Author <email> (mandatory)
 * @version major.minor.patch (mandatory)
 *
 * Class or interface description (mandatory)
 */

public class PNGMapper extends DinomedaMapper
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
    
		
		
		public String[] getTitle()
    { 
        DMDTextNode node = (DMDTextNode)store_.getElement("/title");
        if (node != null)
				  return new String[]{node .get()};
				else
				  return new String[0];
    }
    
    public int getMaxTitleCount()
    {
        return maxTitleCount;
    }
    
    public String[] getCreator()
    {
        DMDTextNode node = (DMDTextNode)store_.getElement("/author");
        if (node != null)
				  return new String[]{node .get()};
				else
				  return new String[0];		
    }
    
    public int getMaxCreatorCount()
    {
        return maxCreatorCount;
    }
    
     
    public Date[] getDate()
    {
//        Date[] date = {makeDate(((DMDTextNode)store_.getElement("/creation time")).get())};
        return new Date[]{};
    }
  
    public int getMaxDateCount()
    {
        return maxDateCount;
    }
   
    
    public String[] getDescription()
    {
        DMDTextNode node = (DMDTextNode)store_.getElement("/comment");
        if (node != null)
				  return new String[]{node .get()};
				else
				  return new String[0];								
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
        return new String[]{};
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
        DMDTextNode node = (DMDTextNode)store_.getElement("/copyright");
        if (node != null)
				  return new String[]{node .get()};
				else
				  return new String[0];					
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
        store_.setElement(new DMDTextNode("/","title",title[0]));
   
    }
    
    public void setCreator(String[] creator) throws ArrayIndexOutOfBoundsException
    {
        if (creator.length > maxCreatorCount) throw new ArrayIndexOutOfBoundsException();
        store_.setElement(new DMDTextNode("/","author",creator[0]));
       
    }
    
    public void setDate(Date[] date) throws ArrayIndexOutOfBoundsException
    {
		  throw new ArrayIndexOutOfBoundsException(); 
//        if (date.length > maxDateCount) throw new ArrayIndexOutOfBoundsException();
//        store_.setElement(new DMDTextNode("/","creation time",year(date[0])));
    }
    
    public void setDescription(String[] description) throws ArrayIndexOutOfBoundsException
    {
        if (description.length > maxDescriptionCount) throw new ArrayIndexOutOfBoundsException();
        store_.setElement(new DMDTextNode("/","comment",description[0]));
        
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
        throw new ArrayIndexOutOfBoundsException();
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
        if (rights.length > maxRightsCount) throw new ArrayIndexOutOfBoundsException();
        store_.setElement(new DMDTextNode("/","rights", rights[0]));
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
    
		
		
		
	public String getMIMEType()
	{
	  return store_.getMIMEType();
	}
	

    private int maxTitleCount = 1;
    private int maxCreatorCount = 1;
    private int maxDateCount = 0;
    private int maxDescriptionCount = 1;
    private int maxPublisherCount = 0;
    private int maxContributorCount = 0;
    private int maxTypeCount = 0;
    private int maxLanguageCount = 0;
    private int maxCollectionCount = 0;
    private int maxRightsCount = 1;
    private int maxIdentifierCount = 0;
    private int maxSourceCount = 0;
    private int maxRelationCount = 0;	
	
    private DMDJobList createJobList(String item)
    {
        DMDJobList joblist = new DMDJobList();
        boolean all = false;
        if (item.equals("/") || item.equals("/*")) all=true;
        
        if (all || item.equals("/title")) joblist.add(new DMDJobListItem("/title"));
        if (all || item.equals("/creator")) joblist.add(new DMDJobListItem("/author"));
        if (all || item.equals("/date")) joblist.add(new DMDJobListItem("/creation time"));
        if (all || item.equals("/description")) joblist.add(new DMDJobListItem("/comment"));
        if (all || item.equals("/right")) joblist.add(new DMDJobListItem("/copyright"));
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
            if (job.equals("/date")) storejoblist.add(new DMDJobListItem("/creation time"));
            if (job.equals("/description")) storejoblist.add(new DMDJobListItem("/comment"));
            if (job.equals("/right")) storejoblist.add(new DMDJobListItem("/copyright"));
         }
        
        return storejoblist;    
    }
	
}
