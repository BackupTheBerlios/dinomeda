///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDJobListItem.java,v 1.1 2003/03/02 19:59:08 krake Exp $
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

// external packages

// local packages

/**
 * @author Martin Oswald <ossi1@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class DMDJobListItem
{
    public DMDJobListItem(String jobname)
    {
        jobname_= jobname;
        status_ = 0;
    }
    
    public int getStatus()
    {
        return status_;
    }
    
    
    public DMDJobListItem itemModified()
    {
        status_ = 2;
        return this;
    }
    
    public DMDJobListItem itemRead()
    {
        if (status_ < 1) status_ = 1;
        return this;
    }
    
    public DMDJobListItem itemWrote()
    {
        if (status_ > 0) status_ = 1;
        return this;
    }
    
    public String getJobName()
    {
        return jobname_;
    }
    
    public boolean equals(DMDJobListItem item)
    {
        return jobname_.equals(item.jobname_);
    }
    
    public String toString()
    {
        return "'"+ jobname_ + "'" + " Status: " + status_;
    }
    
    private String jobname_;
    private int status_;       
}
