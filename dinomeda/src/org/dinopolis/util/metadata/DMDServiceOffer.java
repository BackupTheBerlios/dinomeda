///////////////////////////////////////////////////////////////////////////////
//
// $Id: DMDServiceOffer.java,v 1.3 2003/03/03 23:17:33 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2003
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
 * @version 0.2.0
 */

/**
 * The offer class contains all information necessary to descibe a service
 * module:<br>
 * - the class name of a provider
 * - the mime type of the data
 * - the name of a naming scheme
 * - the name of an IO method, e.g. File, Stream
 * - an IO mode flag field (flags defined in DMDHandler)
 * <p>
 * If all of the above data is specified it actually describes two
 * modules. A name mapper and a store.<br>
 * If the name mapping is null, it describes only a store, if the IO method
 * is null and the IO mode is 0, it describes only a mapper.
 * <p>
 * Examples:<br>
 * all examples are in the following format:<br>
 * <code>(classname, mimetype, namemapping, iomethod, iomode)</code><br>
 * iomode is an NO_IO or any ORed combination of READ, UPDATE, WRITE.<br>
 * The combination of all three mode is also available with ALL_IO
 * <p>
 * <code>("mypackage.MyProvider", "text/html", null, "File", READ)</code><br>
 *     describes a file store accessing HTML files in READ mode, available<br>
 *     through provider mypackage.MyProvider<p>
 * <code>("mypackage.MyProvider", "text/html", "Dinomeda", null, NO_IO)</code><br>
 *     describes a name mapper mapping from the Dinomeda naming scheme to<br>
 *     the internal names of an HTML store and vice versa, again available<br>
 *     through th provider mypackage.MyProvider<p>
 * <code>("mypackage.MyProvider", "text/html", "Dinomeda", "File", READ)</code><br>
 *     describes a mapped file store for reading HTML files
 * <p>
 * The class has only GET methods to ensure that the contained data cannot be
 * altered.<p>
 * If you need to have some fields variable, use DMDServiceQuery instead.
 */

public class DMDServiceOffer
{
  //---------------------------------------------------------------
  /**
   * Creates a service offer for the given parameters.
   * Any of the parameters can be null or 0 respectively.<br>
   * Only the IO mode is partially checked (no values < 0 allowed)
   *
   * @param provider_class a fully qualified class name of a DMDServiveProvider
   * @param mime_type a MIME type string
   * @param name_mapping a namemapping scheme name
   * @param io_method a string decribing a store IO method. Usually File or Stream.
   * @param io_mode a flag field (flags from DMDHandler ORed together)
   */
  public DMDServiceOffer(String provider_class, String mime_type,
                         String name_mapping,
                         String io_method, int io_mode)
  {
    provider_class_ = provider_class;
    mime_type_ = mime_type;
    name_mapping_ = name_mapping;
    io_method_ = io_method;
    io_mode_ = (io_mode < 0 ? 0 : io_mode);
  }

  //---------------------------------------------------------------
  /**
   * Gets the provider class property value.
   *
   * @return the classname property of this offer.
   */
  public String getProviderClass()
  {
    return provider_class_;
  }

  //---------------------------------------------------------------
  /**
   * Gets the MIME type property value.
   *
   * @return the MIME type property of this offer.
   */
  public String getMIMEType()
  {
    return mime_type_;
  }

  //---------------------------------------------------------------
  /**
   * Gets the name mapping property value.
   *
   * @return the name mapping property of this offer.
   */
  public String getNameMapping()
  {
    return name_mapping_;
  }


  //---------------------------------------------------------------
  /**
   * Gets the IO method property value.
   *
   * @return IO method property of this offer.
   */
  public String getIOMethod()
  {
    return io_method_;
  }

  //---------------------------------------------------------------
  /**
   * Gets the IO mode property value.
   *
   * @return the IO mode property of this offer.
   */
  public int getIOMode()
  {
    return io_mode_;
  }

  //---------------------------------------------------------------
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();

    buffer.append("DMDServiceOffer{provider=");
    buffer.append(provider_class_);
    buffer.append(",mime=");
    buffer.append(mime_type_);
    buffer.append(",mapping=");
    buffer.append(name_mapping_);
    buffer.append(",method=");
    buffer.append(io_method_);
    buffer.append(",mode=");
    buffer.append(io_mode_);

    return buffer.toString();
  }

  //---------------------------------------------------------------
  /**
   * Checks if the given offer can be matched in all store relevant
   * properties.
   * The relevant properties are:<br>
   * - provider classname<br>
   * - MIME type<br>
   * - IO method<br>
   * - IO mode
   * <p>
   * The offer can be matched if<br>
   * both objects have the same property value<br>
   * or<br>
   * the object given as the parameter has the respective property in<br>
   * default (null) value.<p>
   * For example the classnames are considered matched if <code>this</code><br>
   * and <code>offer</code> have the same value in the classname property<br>
   * or <code>offer</code> has <code>null</code> as its classname.
   *
   * @param offer the offer to match with
   *
   * @return true if this offer matches the given one
   */
  public boolean matchesStore(DMDServiceOffer offer)
  {
    if (offer == null)
    {
      return false;
    }

    boolean result = true;
    result = result &&
             (offer.provider_class_ == null
              || offer.provider_class_.equals(provider_class_));

    result = result &&
             (offer.mime_type_ == null || offer.mime_type_.equals(mime_type_));

    result = result &&
             (offer.io_method_ == null || offer.io_method_.equals(io_method_));

    result = result &&
             ((offer.io_mode_ & io_mode_) == offer.io_mode_);

    return result;
  }

  //---------------------------------------------------------------
  /**
   * Checks if the given offer can be matched in all mapper relevant
   * properties.
   * The relevant properties are:<br>
   * - provider classname<br>
   * - MIME type<br>
   * - Name mapping
   * <p>
   * The offer can be matched if<br>
   * both objects have the same property value<br>
   * or<br>
   * the object given as the parameter has the respective property in<br>
   * default (null) value.<p>
   * For example the classnames are considered matched if <code>this</code><br>
   * and <code>offer</code> have the same value in the classname property<br>
   * or <code>offer</code> has <code>null</code> as its classname.
   *
   * @param offer the offer to match with
   *
   * @return true if this offer matches the given one
   */
  public boolean matchesMapper(DMDServiceOffer offer)
  {
    if (offer == null)
    {
      return false;
    }

    boolean result = true;

    result = result &&
             (offer.provider_class_ == null 
              || offer.provider_class_.equals(provider_class_));
    
    result = result && 
             (offer.mime_type_ == null || offer.mime_type_.equals(mime_type_));
             
    result = result &&
             (offer.name_mapping_ == null || offer.name_mapping_.equals(name_mapping_));
    
    return result;
  }

  /**
   * The fully qualified class name of a DMDServiceProvder implementation.
   */
  protected String provider_class_;

  /**
   * A MIME type string.
   */
  protected String mime_type_;

  /**
   * The name of a metadata naming conention.
   */
  protected String name_mapping_;

  /**
   * A store IO method.
   */
  protected String io_method_;

  /**
   * A flag field containing an ORed combination of IO flags defined in DMDHandler.
   */
  protected int io_mode_;
}
