///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceProvider.java,v 1.2 2003/02/28 13:00:53 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
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

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 *
 * Class or interface description (mandatory)
 */

public interface DMDServiceProvider
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDStore getStore(DMDServiceOffer query);

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public DMDMapper getMapper(DMDServiceOffer query);

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public boolean canProvide(DMDServiceOffer query);
}
