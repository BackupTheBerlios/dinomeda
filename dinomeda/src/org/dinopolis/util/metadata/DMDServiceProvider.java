///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceProvider.java,v 1.3 2003/03/10 17:54:30 krake Exp $
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


package org.dinopolis.util.metadata;

// Java imports

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.2.0
 */
 
/**
 * The service provider is a factory interface for
 * creating DMDStores and DMDMappers.
 * It can be queried if it will be able to provide the components
 * of a DMDServiceOffer.
 */

public interface DMDServiceProvider
{
  //---------------------------------------------------------------
  /**
   * Creates a store for having the characteristics specified in the
   * given query.
   *
   * @param query an offer or query object specifiying the capabilities
   *        the returned store should have
   *
   * @return a store matching the specified query or null, if he provider
   *         cannot sprovide such a store.
   */
  public DMDStore getStore(DMDServiceOffer query);

  //---------------------------------------------------------------
  /**
   * Creates a mapper for having the characteristics specified in the
   * given query.
   *
   * @param query an offer or query object specifiying the capabilities
   *        the returned mapper should have
   *
   * @return a mapper matching the specified query or null, if he provider
   *         cannot sprovide such a mapper.
   */
  public DMDMapper getMapper(DMDServiceOffer query);

  //---------------------------------------------------------------
  /**
   * Queries the provider, if it is able to provide the components
   * specified in the given query.
   * If the query containes store capabilities (io method, io mode)
   * and this method returns true, getStore with the same query should not return
   * null.
   * Same is valid for queries containing mapper capabilities (name mapping)
   * and the behaviour of getMapper.
   *
   * @param query specifiying either a mapper, a store or both.
   *
   * @return true if the provider is able to provide everything the
   *         query asked for.
   */
  public boolean canProvide(DMDServiceOffer query);
}
