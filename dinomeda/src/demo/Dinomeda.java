///////////////////////////////////////////////////////////////////////////////
//
// $Id: Dinomeda.java,v 1.2 2003/03/02 20:15:50 krake Exp $
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
      if (args[count].equals("--help"))
      {
        printHelp();
        System.exit(0);
      }
      else if (args[count].equals("--swing"))
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

  //---------------------------------------------------------------
  /**
   * Method description
   */
  private void printHelp()
  {
    System.out.println("Dinomeda Demo-Application");
    System.out.println("usage console mode: Dinomeda [options] [files]");
    System.out.println("usage swing mode: Dinomeda --swing [options]");
    System.out.println();

    System.out.println("General options:");
    System.out.println("--cfg file\tuse the config file \"file\"");
    System.out.println();

    System.out.println("Console mode options:");
    System.out.print("--metagrep string\tsearch all metadata of each file for the ");
    System.out.println("given search string");
  }

  protected boolean use_swing_ = false;
  protected String[] program_arguments_;
}
