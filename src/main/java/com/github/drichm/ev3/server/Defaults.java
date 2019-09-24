package com.github.drichm.ev3.server;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Global Defaults
 */
public class Defaults
{
  //===========================================================================

  /** Debugging on Windows */
  static public boolean ON_WINDOWS = "Windows_NT".equalsIgnoreCase( System.getenv( "OS" ) );
  
  /** Disk location of ev3 debug web-app */
  static public Path    DEBUG_APP  = ON_WINDOWS 
                                      ? Paths.get("").toAbsolutePath().resolve( "src/main/webapp" )
                                      : Paths.get("").toAbsolutePath().resolve( "debug" );
  

  //===========================================================================
  // Locale
  
  /** Default locale assumed */
  static public final Locale LOCALE = Locale.ENGLISH;

  /** Return the given Locale if not null, otherwise return the default */
  static public Locale defaultLocale( Locale locale ) 
  { 
    return (locale != null) ? locale : LOCALE; 
  }


  //===========================================================================
  // Encoding

  /** Character encoding assumed by all classes for HTTP text */
  static public final String  ENCODING_HTTP = "UTF-8";
  
  /** Charset for ENCODING_HTTP */
  static public final Charset CHARSET_HTTP = Charset.forName( ENCODING_HTTP );


  /** Character encoding assumed by all classes for files */
  static public final String  ENCODING     = "ISO-8859-1";
  
  /** Charset for file ENCODING */
  static public final Charset CHARSET      = Charset.forName( ENCODING );

} // end of Defaults