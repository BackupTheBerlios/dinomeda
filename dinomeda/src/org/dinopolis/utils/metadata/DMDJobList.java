///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDJobList.java,v 1.1 2003/02/27 21:56:11 krake Exp $
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


package org.dinopolis.utils.metadata;

// Java imports

import java.util.*;

// external packages

// local packages

/**
 * @author Martin Oswald <ossi1@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DMDJobList
{
    public DMDJobList()
    {
        list_ = new ArrayList();
    }
    
    public boolean add(DMDJobListItem item)
    {
        if (!contains(item))
        {
            return list_.add(item);
        }
        return false;
     }
    
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

    public DMDJobListItem get(int index)
    {
        return (DMDJobListItem)list_.get(index);
    }
    
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
    
    public DMDJobListItem set(int index, DMDJobListItem item)
    {
        return (DMDJobListItem)list_.set(index, item);
    }

    public DMDJobListItem remove(int index)
    {
        return (DMDJobListItem)list_.remove(index);
    }
    
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
    
    public void clear()
    {
        list_.clear();
    }
      
    public int size()
    {
        return list_.size();
    }
    
    
    public void itemModified(int index)
    {
        get(index).itemModified();
    }
    
    public void itemModified(String jobname)
    {
        get(jobname).itemModified();
    }
    
    public void itemRead(int index)
    {
        get(index).itemRead();
    }

    public void itemRead(String jobname)
    {
        get(jobname).itemRead();
    }
    
    public void itemWrote(int index)
    {
        get(index).itemWrote();
    }

    public void itemWrote(String jobname)
    {
        get(jobname).itemWrote();
    }
    
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
