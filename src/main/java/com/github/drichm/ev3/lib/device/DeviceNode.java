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

}