///////////////////////////////////////////////////////////////////////////////
//
// $Id: DinomedaMapper.java,v 1.3 2003/03/05 12:56:52 osma Exp $
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
   * @return returns the item array if the specified field exists or null,    * if the parameter was not one of the defined constants.
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
  
  //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda title field.
   * @return returns the title array 
   */  abstract public String[] getTitle();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a title field.
   * @return the maximum number of entries the title field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxTitleCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda creator field.
   * @return returns the creator array 
   */
  abstract public String[] getCreator();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a creator field.
   * @return the maximum number of entries the creator field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxCreatorCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda date field.
   * @return returns the date array 
   */
  abstract public Date[] getDate();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a date field.
   * @return the maximum number of entries the date field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxDateCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda description field.
   * @return returns the description array 
   */
  abstract public String[] getDescription();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a description field.
   * @return the maximum number of entries the tdescription field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxDescriptionCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda publisher field.
   * @return returns the publisher array 
   */
  abstract public String[] getPublisher();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a publisher field.
   * @return the maximum number of entries the publisher field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxPublisherCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda contributor field.
   * @return returns the contributor array 
   */
  abstract public String[] getContributor();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a contributor field.
   * @return the maximum number of entries the contributor field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxContributorCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda type field.
   * @return returns the type array 
   */
  abstract public String[] getType();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a type field.
   * @return the maximum number of entries the type field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxTypeCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda language field.
   * @return returns the language array 
   */
  abstract public Locale[] getLanguage();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a language field.
   * @return the maximum number of entries the language field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxLanguageCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda collection field.
   * @return returns the collection array 
   */
  abstract public String[] getCollection();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a collection field.
   * @return the maximum number of entries the collection field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxCollectionCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda rights field.
   * @return returns the rigths array 
   */
  abstract public String[] getRights();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a rights field.
   * @return the maximum number of entries the rights field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxRightsCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda identifier field.
   * @return returns the identifier array 
   */
  abstract public URL[] getIdentifier();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a identifier field.
   * @return the maximum number of entries the identifier field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxIdentifierCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda source field.
   * @return returns the source array 
   */
  abstract public URL[] getSource();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a source field.
   * @return the maximum number of entries the source field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxSourceCount();
    //---------------------------------------------------------------
  /**
   * Gets item array of the Dinomeda relation field.
   * @return returns the relation array 
   */
  abstract public URL[] getRelation();
  //---------------------------------------------------------------
  /**
   * Gets the maximum valid number of occurences of a relation field.
   * @return the maximum number of entries the relation field can hold in
   *         the format the mapper is mapping to.
   */  abstract public int getMaxRelationCount();
  
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
  
  //---------------------------------------------------------------
  /**
   * Sets the Dinomeda title.
   *
   * @param title array of titles
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the title array is   * larger than the maximum amount of titles the mapper is able to map.
   */  abstract public void setTitle(String[] title) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda creator.
   *
   * @param creator array of creators
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the creator array is   * larger than the maximum amount of creators the mapper is able to map.
   */
  abstract public void setCreator(String[] creator) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda date.
   *
   * @param date array of dates
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the date array is   * larger than the maximum amount of dates the mapper is able to map.
   */
  abstract public void setDate(Date[] date) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda title.
   *
   * @param description array of descriptions
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the description array is   * larger than the maximum amount of descriptions the mapper is able to map.
   */
  abstract public void setDescription(String[] description) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda publisher.
   *
   * @param publisher array of publishers
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the publisher array is   * larger than the maximum amount of publishers the mapper is able to map.
   */
  abstract public void setPublisher(String[] publisher) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda contributor.
   *
   * @param contributor array of contributors
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the contributor array is   * larger than the maximum amount of contributors the mapper is able to map.
   */
  abstract public void setContributor(String[] contributor) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda type.
   *
   * @param type array of types
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the type array is   * larger than the maximum amount of types the mapper is able to map.
   */
  abstract public void setType(String[] type) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda language.
   *
   * @param language array of language
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the language array is   * larger than the maximum amount of languages the mapper is able to map.
   */
  abstract public void setLanguage(Locale[] language) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda collection.
   *
   * @param collection array of collections
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the collection array is   * larger than the maximum amount of collections the mapper is able to map.
   */
  abstract public void setCollection(String[] collection) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda rights.
   *
   * @param rights array of rights
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the rights array is   * larger than the maximum amount of rights the mapper is able to map.
   */
  abstract public void setRights(String[] rights) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda identifier.
   *
   * @param identifier array of identifiers
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the identifier array is   * larger than the maximum amount of identifiers the mapper is able to map.
   */
  abstract public void setIdentifier(URL[] identifier) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda source.
   *
   * @param source array of sources
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the source array is   * larger than the maximum amount of sources the mapper is able to map.
   */
  abstract public void setSource(URL[] source) throws ArrayIndexOutOfBoundsException;
    //---------------------------------------------------------------
  /**
   * Sets the Dinomeda relation.
   *
   * @param relation array of relations
   *
   * @throws ArrayIndexOutOfBoundsException if the length of the relation array is   * larger than the maximum amount of relations the mapper is able to map.
   */
  abstract public void setRelation(URL[] relation) throws ArrayIndexOutOfBoundsException;
  
  protected DMDStore store_;
}
