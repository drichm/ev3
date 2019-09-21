package com.github.drichm.ev3.lib.device;

/**
 * A named thing
 * <p>
 * Named things are equal if their classes are equal and their name()s are equal 
 */
abstract public class Named implements Comparable<Named>
{
  /** String used in device and attribute names as a placeholder for a numerical port or value index */
  static public final String N = "{N}";
  
  protected final String name;
  protected final String description;
  
  protected Named( String name )
  {
    this( name, null );
  }

  protected Named( String name, String description )
  {
    this.name        = name;
    this.description = description;
  }
  
  /** Non-null name */
  public String name       ()  { return name == null ? "" : name; }
  
  /** Possibly null description */
  public String description()  { return description; }
  
  
  
  @Override public int compareTo( Named o )
  {
    return name().compareToIgnoreCase( o.name() );
  }
  
  
  
  @Override public int hashCode()  { return name().hashCode(); }

  @Override public boolean equals( Object obj )
  {
    if ( !(obj instanceof Named) )
      return false;
    else
    if ( getClass() != obj.getClass() )    // !!! assumes same ClassLoader
      return false;
    else
      return name().equals( ((Named) obj).name() );
  }

}
