package com.github.drichm.ev3.lib.hardware;

import com.github.drichm.ev3.lib.device.Attribute;
import com.github.drichm.ev3.lib.device.Mode;
import com.github.drichm.ev3.lib.device.Type;

/** Device Node location */
public class AttributeInstance extends Attribute
{
  public AttributeInstance( String topname )
  {
    this( topname, null );
  }

  public AttributeInstance( String topname, String subname )
  {
    super( topname, subname, (Type) null, (Mode) null );
  }
}