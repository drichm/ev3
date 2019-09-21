package com.github.drichm.ev3.lib.device;

/**
 * One possible attribute on a device
 */
public class Attribute extends Named
{
  /** Top-level name - never null */
  public final String   topname;

  /** Sub (lower) level name, null if none */
  public final String   subname;
  
  /** Value type, null if unknown */
  public final Type     type;
  
  /** Read/Write mode - never null */
  public final Mode     mode;

  /** Possible values, empty if not limited. Binary types should pass a Base64 encoding of bytes */
  public final String[] values;

  public Attribute( String name, String... values )
  {
    this( name, Type.s, values.length == 1 ? Mode.ro : Mode.rw, values );
  }

  public Attribute( String name, Mode mode, String... values )
  {
    this( name, Type.s, mode, values );
  }

  public Attribute( String name, Type type, Mode mode, String... values )
  {
    super( name );

    this.type    = type;
    this.mode    = mode == null ? Mode.ro : mode;
    this.values  = values;
    
    if ( name.contains( "/" ) )
    {
      String[] s = name.split( "/" );

      topname = s[0];
      subname = s[1];
    }
    else
    {
      topname = name;
      subname = name;
    }
  }

  public Attribute( String topname, String subname, Type type, Mode mode, String... values )
  {
    super( subname == null ? topname : topname + "/" + subname );

    this.topname = topname;
    this.subname = subname;
    this.type    = type;
    this.mode    = mode == null ? Mode.ro : mode;
    this.values  = values;
  }

}
