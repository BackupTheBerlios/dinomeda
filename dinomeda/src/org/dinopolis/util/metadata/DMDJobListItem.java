///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDJobListItem.java,v 1.2 2003/03/05 09:03:52 osma Exp $
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
 *//**
 * Class for describing jobs and their status.
 */

public class DMDJobListItem
{
   //---------------------------------------------------------------
  /**
   * Constructs a DMDJobListItem with a specified jobname.
   * @param jobname name of the job.
   */   public DMDJobListItem(String jobname)
  {
    jobname_= jobname;
    status_ = 0;
  }
  
   //---------------------------------------------------------------
  /**
   * Returns the status of the job.
   * @return the status of the job.
   */   public int getStatus()
  {
    return status_;
  }
  
  //---------------------------------------------------------------
  /**
   * Changes the status to modified.
   * @return the DMDJobListItem.
   */ 
  public DMDJobListItem itemModified()
  {
    status_ = 2;
    return this;
  }
    //---------------------------------------------------------------
  /**
   * Changes the status to read.
   * @return the DMDJobListItem.
   */ 
  public DMDJobListItem itemRead()
  {
    if (status_ < 1) status_ = 1;
    return this;
  }
    //---------------------------------------------------------------
  /**
   * Changes the status to wrote.
   * @return the DMDJobListItem.
   */ 
  public DMDJobListItem itemWrote()
  {
    if (status_ > 0) status_ = 1;
    return this;
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the jobname.
   * @return the jobname.
   */   public String getJobName()
  {
    return jobname_;
  }
    //---------------------------------------------------------------
  /**
   * Compaires the job to another job.
   * @param item DMDJobListItem
   * @return true if the jobnames are equal.
   */ 
  public boolean equals(DMDJobListItem item)
  {
    return jobname_.equals(item.jobname_);
  }
  
  //---------------------------------------------------------------
  /**
   * Returns the string representation of the DMDJobListItem.
   * @return the string representation of the DMDJobListItem.
   */   public String toString()
  {
    return "'"+ jobname_ + "'" + " Status: " + status_;
  }
  
  private String jobname_;
  private int status_;     
}
