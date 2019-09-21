package com.github.drichm.ev3.lib.hardware;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Low level file access to EV3DEV sysfs file system interface to EV3 ports
 * 
 * @see https://ev3dev-lang.readthedocs.io/en/latest/
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/
 * @see https://github.com/ev3dev-lang-java/ev3dev-lang-java
 */
public class SysFS
{
  static public final Charset CHARSET = StandardCharsets.UTF_8;

  // ==========================================================================

  /** SysFS root location */
  public final Path root;

  public SysFS()
  {
    this( Paths.get( "/sys/class" ) );
  }

  public SysFS( Path root )
  {
    this.root = root;
  }


  // ==========================================================================

  public Path path( DeviceNodeInstance dn )
  {
    return root.resolve( dn.name() );
  }

  public Path path( DeviceNodeInstance dn, AttributeInstance attr )
  {
    return root.resolve( dn.name() ).resolve( attr.name() );
  }

  
  /** Replace any number at the end of 'name' with given replacement string */
  static public String replaceN( String name, String replacement )
  {
    int n = 0;

    // extract any trailing number's start position
    for ( int i=name.length()-1 ; i > 0 ; i-- )
      if ( Character.isDigit( name.charAt(i) ) )
        n = i;
      else
        break;

    if ( n == 0 )
      return name;
    else
      return name.substring( 0, n+1 ) + replacement;
  }
  
  
  // ==========================================================================
  // Writing

  public Throwable write( DeviceNodeInstance dn, AttributeInstance attr, String text )
  {
    try
    {
      Files.writeString( path( dn, attr ), text == null ? "" : text, CHARSET, StandardOpenOption.CREATE, StandardOpenOption.WRITE );
      return null;
    }
    catch ( Throwable t )
    {
      return t;
    }
  }


  // ==========================================================================
  // Reading

  public Integer readInteger( DeviceNodeInstance dn, AttributeInstance attr )
  {
    try
    {
      return Integer.valueOf( read( dn, attr ) );
    }
    catch ( Throwable t )
    {
      return null;
    }
  }

  public Double readDouble( DeviceNodeInstance dn, AttributeInstance attr )
  {
    try
    {
      return Double.valueOf( read( dn, attr ) );
    }
    catch ( Throwable t )
    {
      return null;
    }
  }

  public String read( DeviceNodeInstance dn, AttributeInstance attr )
  {
    try
    {
      return Files.readString( path( dn, attr ), CHARSET ).trim();
    }
    catch ( Throwable t )
    {
      return null;
    }
  }


  // ==========================================================================
  // Scanning
  
  /** All Device Nodes currently available */
  public Stream<DeviceNodeInstance> nodes( Set<String> scannable )
  {
    return Arrays.stream ( root.toFile().listFiles() )
                 .filter ( File::isDirectory )
                 .filter ( f -> scannable.contains( f.getName() ) )
                 .map    ( f -> Arrays.stream( f.listFiles() )
                                                .filter( File::isDirectory )
                                                .map( s -> new DeviceNodeInstance( f.getName(), s.getName() ) ) 
                         )
                 .flatMap( s -> s );
  }

  /** Attributes currently visible for Device Node */
  public Stream<AttributeInstance> attributes( DeviceNodeInstance dn )
  {
    try
    {
      return Arrays.stream ( path( dn ).toFile().listFiles() )
                   .map    ( f -> f.isDirectory() 
                                   ? Arrays.stream( f.listFiles() ).map( s -> new AttributeInstance( f.getName(), s.getName() ) )
                                   : Stream.of( new AttributeInstance( f.getName() ) ) )
                   .flatMap( s -> s );
    }
    catch ( Throwable t )
    {
      return null;
    }
  }

} // end of class SysFS
