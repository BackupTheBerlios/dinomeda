///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaMapper.java,v 1.2 2003/03/03 13:56:39 krake Exp $
//
// Copyright: Martin Oswald <ossi1@sbox.tugraz.at>, 2002-2003
// Copyright: Mattias Welponer <maba@sbox.tugraz.at>, 2002-2003
//
///////////////////////////////////////////////////////////////////////////////
//
//   This program is free software; you can redistribute it and/or modify
//   it under the terms of the GNU Lesser General Public License as
//   published by the Free Software Foundation; either version 2 of the
//   License, or (at your option) any later version.
//
///////////////////////////////////////////////////////////////////////////////


package org.dinopolis.util.metadata.dinomeda;

// Java imports
import java.util.Date;
import java.util.Locale;
import java.lang.ArrayIndexOutOfBoundsException;
import java.net.URL;

// external packages

// local packages
import org.dinopolis.util.metadata.DMDMapper;
import org.dinopolis.util.metadata.DMDStore;

/**
 * @author: Martin Oswald <ossi1@sbox.tugraz.at>
 * @author: Mattias Welponer <maba@sbox.tugraz.at>
 * @version 0.2.0
 */
 
/**
 * The interface specifying methods and constants of name mappers
 * which map store names to the Dinomeda naming scheme.
 */

abstract public class DinomedaMapper implements DMDMapper
{
  public static final int TITLE       = 0;
  public static final int CREATOR     = 1;
  public static final int DATE        = 2;
  public static final int DESCRIPTION = 3;
  public static final int PUBLISHER   = 4;
  public static final int CONTRIBUTOR = 5;
  public static final int TYPE        = 6;
  public static final int LANGUAGE    = 7;
  public static final int COLLECTION  = 8;
  public static final int RIGHTS      = 9;
  public static final int IDENTIFIER  = 10;
  public static final int SOURCE      = 11;
  public static final int RELATION    = 12;

  public static final String[] FIELD_NAMES =
    {"Title", "Creator", "Date", "Description", "Publisher",
     "Contributor", "Type", "Language", "Collection", "Rights",
     "Identifier", "Source", "Relation"};

  /**
   * Number of name fields.
   */
  public static final int FIELD_COUNT = 13;

  //---------------------------------------------------------------
  /**
   * Sets a DMDStore instance
   *
   * @param store the store the mapper works with
   */
  public void setStore(DMDStore store)
  {
    store_ = store;
  }

  //---------------------------------------------------------------
  /**
   * Gets the DMDStore instance the mapper is currently working with.
   *
   * @return the mapper's current store
   */
  public DMDStore getStore()
  {
    return store_;
  }

  //---------------------------------------------------------------
  /**
   * Returns the mapper's naming scheme.
   * This is "Dinomeda" for all Dinomeda mappers.
   *
   * @return the string "Dinomeda"
   */
  public String getNameMapping()
  {
      return "Dinomeda";
  }

  //---------------------------------------------------------------
  /**
   * Method description
   */
  public int getIOMode()
  {
    if (store_ != null)
    {
      return store_.getIOMode();
    }
    
    return NO_IO;
  }
  
  //---------------------------------------------------------------
  /**
   * Gets item array of a Dinomeda name field.
   *
   * @param item a value from the constants defined in this class
   *
   * @return returns the item array if the specified field or null, if the
   *         parameter was not one of the defined constants.
   */
  public Object[] get(int item)
  {
    if (item==TITLE) return getTitle();
    if (item==CREATOR) return getCreator();
    if (item==DATE) return getDate();
    if (item==DESCRIPTION) return getDescription();
    if (item==PUBLISHER) return getPublisher();
    if (item==CONTRIBUTOR)  return getContributor();
    if (item==TYPE)  return getType();
    if (item==LANGUAGE)  return getLanguage();
    if (item==COLLECTION)  return getCollection();
    if (item==RIGHTS)  return getRights();
    if (item==IDENTIFIER) return getIdentifier();
    if (item==SOURCE) return getSource();
    if (item==RELATION) return getRelation();
    return null;
  }

  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a specified item.
   *
   * @param item a value from the constants defined in this class
   *
   * @return the maximum number of entries the named element can hold in
   *         the format the mapper is mapping to or 0 if the parameter
   *         is invalid
   */
  public int getMaxCount(int item)
  {
    if (item==TITLE) return getMaxTitleCount();
    if (item==CREATOR) return getMaxCreatorCount();
    if (item==DATE) return getMaxDateCount();
    if (item==DESCRIPTION) return getMaxDescriptionCount();
    if (item==PUBLISHER) return getMaxPublisherCount();
    if (item==CONTRIBUTOR)  return getMaxContributorCount();
    if (item==TYPE)  return getMaxTypeCount();
    if (item==LANGUAGE)  return getMaxLanguageCount();
    if (item==COLLECTION)  return getMaxCollectionCount();
    if (item==RIGHTS)  return getMaxRightsCount();
    if (item==IDENTIFIER) return getMaxIdentifierCount();
    if (item==SOURCE) return getMaxSourceCount();
    if (item==RELATION) return getMaxRelationCount();
    return 0;
  }
  
  abstract public String[] getTitle();
  abstract public int getMaxTitleCount();
  
  abstract public String[] getCreator();
  abstract public int getMaxCreatorCount();
  
  abstract public Date[] getDate();
  abstract public int getMaxDateCount();
  
  abstract public String[] getDescription();
  abstract public int getMaxDescriptionCount();
  
  abstract public String[] getPublisher();
  abstract public int getMaxPublisherCount();
  
  abstract public String[] getContributor();
  abstract public int getMaxContributorCount();
  
  abstract public String[] getType();
  abstract public int getMaxTypeCount();
  
  abstract public Locale[] getLanguage();
  abstract public int getMaxLanguageCount();
  
  abstract public String[] getCollection();
  abstract public int getMaxCollectionCount();
  
  abstract public String[] getRights();
  abstract public int getMaxRightsCount();
  
  abstract public URL[] getIdentifier();
  abstract public int getMaxIdentifierCount();
  
  abstract public URL[] getSource();
  abstract public int getMaxSourceCount();
  
  abstract public URL[] getRelation();
  abstract public int getMaxRelationCount();
  
  public void set(int item, Object[] value) throws ArrayIndexOutOfBoundsException
  {
    if (item==TITLE) setTitle((String[])value);
    if (item==CREATOR) setCreator((String[])value);
    if (item==DATE) setDate((Date[])value);
    if (item==DESCRIPTION) setDescription((String[])value);
    if (item==PUBLISHER) setPublisher((String[])value);
    if (item==CONTRIBUTOR) setContributor((String[])value);
    if (item==TYPE) setType((String[])value);
    if (item==LANGUAGE) setLanguage((Locale[])value);
    if (item==COLLECTION) setCollection((String[])value);
    if (item==RIGHTS) setRights((String[])value);
    if (item==IDENTIFIER) setIdentifier((URL[])value);
    if (item==SOURCE) setSource((URL[])value);
    if (item==RELATION) setRelation((URL[])value);
  }
  
  abstract public void setTitle(String[] title) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setCreator(String[] creator) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setDate(Date[] date) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setDescription(String[] description) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setPublisher(String[] publisher) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setContributor(String[] contributor) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setType(String[] type) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setLanguage(Locale[] language) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setCollection(String[] collection) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setRights(String[] rights) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setIdentifier(URL[] identifier) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setSource(URL[] source) throws ArrayIndexOutOfBoundsException;
  
  abstract public void setRelation(URL[] relation) throws ArrayIndexOutOfBoundsException;
  
  protected DMDStore store_;
}
