///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNodeIterator.java,v 1.1 2003/03/02 19:59:08 krake Exp $
//
// Copyright: Author <email>, year
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
 * @author Author <email> (mandatory)
 * @version major.minor.patch (mandatory)
 *
 * Class or interface description (mandatory)
 */

public class DMDNodeIterator implements java.util.Iterator
{
    public DMDNodeIterator(Iterator iterator)
    {
        iterator_ = iterator;
    }
    
    public DMDNode nextNode()
    {
        return (DMDNode)iterator_.next();
    }
 
    public void remove()
    {
        iterator_.remove();
    }
    
    public boolean hasNext()
    {
        return iterator_.hasNext();
    }
    
    public Object next()
    {
        return iterator_.next();
    }
    
    protected Iterator iterator_; 
}
