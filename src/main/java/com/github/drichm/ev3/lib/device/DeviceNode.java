package com.github.drichm.ev3.lib.device;

/** Device Node location */
public class DeviceNode extends Named
{
  public final String type;
  public final String subtype;

  public DeviceNode( String type, String subtype )
  {
    this( type, subtype, type + "/" + subtype );
  }
 
  public DeviceNode( String type, String subtype, String subpath )
  {
    super( subpath );

    this.type    = type;
    this.subtype = subtype;
  }
  
  public String subpath()  { return name(); }
  

  @Override public int hashCode()  { return name().hashCode(); }
  
  @Override public boolean equals( Object obj )
  {
    if ( obj instanceof String )
      // for Map lookup
      return name().equalsIgnoreCase( (String) obj );
    else
    if ( obj instanceof DeviceNode )
      return name().equals( ((DeviceNode) obj).name() );
    else
      return false;
  }

}