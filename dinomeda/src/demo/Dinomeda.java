///////////////////////////////////////////////////////////////////////////////
//
// $Id: Dinomeda.java,v 1.1 2003/02/27 21:56:14 krake Exp $
//
// Copyright: Kevin Krammer <voyager@sbox.tugraz.at>, 2002-2003
//
///////////////////////////////////////////////////////////////////////////////
//                                                                          
//   This program is free software; you can redistribute it and/or modify  
//   it under the terms of the GNU General Public License as        
//   published by the Free Software Foundation; either version 2 of the    
//   License, or (at your option) any later version.                       
//                                                                         
///////////////////////////////////////////////////////////////////////////////


package demo;

// Java imports

// external packages

// local packages

/**
 * @author Kevin Krammer <voyager@sbox.tugraz.at>
 * @version 0.1.0
 *
 * Class or interface description (mandatory)
 */

public class Dinomeda
{
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public Dinomeda(String[] args)
  {
    parseArguments(args);
  }

  public DinomedaApplication createApplication()
  {
    DinomedaApplication app = null;
    if (use_swing_)
    {
      app = new DinomedaSwingApplication(program_arguments_);
    }
    else
    {
      app = new DinomedaConsoleApplication(program_arguments_);
    }
    
    return app;
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  public static void main(String[] args)
  {
    Dinomeda starter = new Dinomeda(args);
    
    DinomedaApplication app = starter.createApplication();
    app.run();
  }
  
  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void parseArguments(String[] args)
  {
    int pos = -1;
    for (int count = 0; count < args.length; ++count)
    {
      if (args[count].equals("--swing"))
      {
        use_swing_ = true;
        
        pos = count;
        break;
      }
    }
    
    if (pos > -1)
    {
      int length = args.length - 1;

      program_arguments_ = new String[length];
            
      System.arraycopy(args, 0, program_arguments_, 0, pos);
      
      System.arraycopy(args, pos+1, program_arguments_, pos, (length-pos)); 
    }
    else
    {
      program_arguments_ = args;
    }
  }
  
  protected boolean use_swing_ = false;  
  protected String[] program_arguments_;
}
