///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDJobList.java,v 1.2 2003/03/05 09:03:52 osma Exp $
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


package org.dinopolis.util.metadata;

// Java imports

import java.util.*;

// external packages

// local packages

/**
 * @author Martin Oswald <ossi1@sbox.tugraz.at>
 * @version 0.1.0
 */

/**
 * List of DMDJobListItems
 */

public class DMDJobList
{
  
  //---------------------------------------------------------------
  /**
   * Constructs an empty DMDJobList
   */
  public DMDJobList()
  {
    list_ = new ArrayList();
  }
  
  //---------------------------------------------------------------
  /**
   * Adds a DMDJobListItem
   * @param item DMDJoblistItem
   * @return false if the DMDJobList already contains the same DMDJobListItem 
   */
  public boolean add(DMDJobListItem item)
  {
    if (!contains(item))
    {
      return list_.add(item);
    }
    return false;
   }
  
  //---------------------------------------------------------------
  /**
   * Adds another DMDJobList
   * @param item DMDJoblist
   * @return the amount of DMDJobListItems added 
   */
  public int add(DMDJobList joblist)
  {
    int added=0;
    
    for (int i=0;i<joblist.size();i++)
    {
      if (add(joblist.get(i)))
      {
        added++;
      }
    }
    return added;
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the DMDJobListItem on the spectified position
   * @param index position of the DMDJobListItem in the DMDJobList
   * @return the DMDJobListItem 
   */
  public DMDJobListItem get(int index)
  {
    return (DMDJobListItem)list_.get(index);
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the DMDJobListItem with the spectified jobname
   * @param jobname name of the DMDJobListItem in the DMDJobList
   * @return the DMDJobListItem 
   */
  public DMDJobListItem get(String jobname)
  {
    int i=0;
  
    while (i<size())
    {
      if (get(i).getJobName().equals(jobname))
      {
        return get(i);        
      }
      i++;
    }
    return null;
  }
  
  //---------------------------------------------------------------
  /**
   * Sets a DMDJobListItem on the spectified position
   * @param jobname name of the DMDJobListItem in the DMDJobList
   * @param the DMDJobListItem 
   */
  public DMDJobListItem set(int index, DMDJobListItem item)
  {
    return (DMDJobListItem)list_.set(index, item);
  }

  public DMDJobListItem remove(int index)
  {
    return (DMDJobListItem)list_.remove(index);
  }
  
  //---------------------------------------------------------------
  /**
   * Removes a DMDJobListItem with the spectified jobname
   * @param jobname name of the DMDJobListItem in the DMDJobList
   * @return the removed DMDJobListItem
   */
  public DMDJobListItem remove(String jobname)
  {
    int i=0;
  
    while (i<size())
    {
      if (get(i).getJobName().equals(jobname))
      {
        return (DMDJobListItem)list_.remove(i);        
      }
      i++;
    }
    return null;
  }
  
  //---------------------------------------------------------------
  /**
   * Removes all DMDJobListItem which are not modified
   * @return amount of the removed DMDJobListItems
   */
  public int removeItemsNotModified()
  {
    int index = 0;
    int removed = 0;
    
    while (index<size())
    {
      if (get(index).getStatus()<2)
      {
        list_.remove(index);
        index--;
        removed++;
      }
      index++;
    }
    return removed;
  }
  
  //---------------------------------------------------------------
  /**
   * Removes all DMDJobListItem which are not read
   * @return amount of the removed DMDJobListItems
   */
  public int removeItemsNotRead()
  {
    int index = 0;
    int removed = 0;
    
    while (index<size())
    {
      if (get(index).getStatus()<1)
      {
        list_.remove(index);
        index--;
        removed++;
      }
      index++;
    }
    return removed;
  }
  
  //---------------------------------------------------------------
  /**
   * Removes all DMDJobListItem which are not read
   * @return amount of the removed DMDJobListItems
   */
  public boolean contains(DMDJobListItem item)
  {
    for (int i=0;i<list_.size();i++)
    {
      if (item.equals(get(i)))
      {
        return true;
      }
      
    }
    return false;
  }
  
   //---------------------------------------------------------------
  /**
   * Returns true if the DMDJobList contains a DMDJobListItem with the 
   * specified name
   * @return ture if the DMDJobList contains a DMDJobListItem with the 
   * specified name
   */
  public boolean contains(String jobname)
  {
    for (int i=0;i<list_.size();i++)
    {
      if (jobname.equals(get(i).getJobName()))
      {
        return true;
      }
      
    }
    return false;
  }
  
  //---------------------------------------------------------------
  /**
   * Clears the DMDJobList
   */
  public void clear()
  {
    list_.clear();
  }
    
  //---------------------------------------------------------------
  /**
   * Returns the size of the DMDJobList
   * @return the size of the DMDJobList
   */
  public int size()
  {
    return list_.size();
  }
  
  
  //---------------------------------------------------------------
  /**
   * Sets the DMDJobListItem on the specified position to status itemModified
   * @param index position of the DMDJobListItem 
   */
  public void itemModified(int index)
  {
    get(index).itemModified();
  }
  
  //---------------------------------------------------------------
  /**
   * Sets the DMDJobListItem with the specified jobname to status itemModified
   * @param jobname name of the DMDJobListItem 
   */
  public void itemModified(String jobname)
  {
    get(jobname).itemModified();
  }
  
  //---------------------------------------------------------------
  /**
   * Sets the DMDJobListItem on the specified position to status itemRead
   * @param index position of the DMDJobListItem 
   */
  public void itemRead(int index)
  {
    get(index).itemRead();
  }

  //---------------------------------------------------------------
  /**
   * Sets the DMDJobListItem with the specified jobname to status itemRead
   * @param jobname name of the DMDJobListItem 
   */
  public void itemRead(String jobname)
  {
    get(jobname).itemRead();
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the status of the DMDJobListItem on the specified position
   * @param index position of the DMDJobListItem in the DMDJobList 
   */
  public void itemWrote(int index)
  {
    get(index).itemWrote();
  }

  //---------------------------------------------------------------
  /**
   * Sets the DMDJobListItem with the specified jobname to status itemWrote
   * @param jobname name of the DMDJobListItem 
   */
  public void itemWrote(String jobname)
  {
    get(jobname).itemWrote();
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the string representation of the DMDJoblist 
   * @return the string representation of the DMDJoblist 
   */
  public String toString()
  {
    String output ="";
    
    for (int i=0;i<size();i++)
    {
      output = output + i + " -> " + get(i) + "\n";
    }
         
    return output;
  }
   
  private ArrayList list_;
  
}
