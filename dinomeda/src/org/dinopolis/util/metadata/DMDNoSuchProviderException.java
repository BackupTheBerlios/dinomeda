///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDNoSuchProviderException.java,v 1.1 2003/03/02 19:59:08 krake Exp $
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

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Exception indicating that an operation was requested for an unknown
 * DMDServiceProvider.
 */
public class DMDNoSuchProviderException extends Exception
{
  //---------------------------------------------------------------
  /**
   * Constructs an empty exception.
   */
  public DMDNoSuchProviderException()
  {
    super();
  }

  //---------------------------------------------------------------
  /**
   * Constructs an exception for the provider with the given class name.
   *
   * @param class_name the fully qualified class name of the service provider.
   */
  public DMDNoSuchProviderException(String class_name)
  {
    super();
    class_name_ = class_name;
  }

  //---------------------------------------------------------------
  /**
   * Constructs an exception for the provider with the given class name and
   * includes an error message.
   *
   * @param class_name the fully qualified class name of teh service provider.
   * @param message an error message describing the cause for this exception.
   * @see java.lang.Exception#Exception(java.lang.String)
   */
  public DMDNoSuchProviderException(String class_name, String message)
  {
    super(message);
    class_name_ = class_name;
  }


  //---------------------------------------------------------------
  /**
   * Returns the fully qualified class name of the provider.
   *
   * @return the fully qualified class name or null if no class name was set.
   */
  public String getClassName()
  {
    return class_name_;
  }

  //---------------------------------------------------------------
  /**
   * The class name of the service provider which could not be found.
   */
  protected String class_name_ = null;
}
