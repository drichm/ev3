package com.github.drichm.ev3.lib.hardware;

import com.github.drichm.ev3.lib.device.DeviceNode;
import com.github.drichm.ev3.lib.device.Named;

/** Actual Device on port N */
public class DeviceNodeInstance extends DeviceNode
{
  public final String nameN;
  
  public DeviceNodeInstance( String topname, String subname )
  {
    super( topname, subname );
    
    this.nameN = SysFS.replaceN( subname, Named.N );
  }
  
  
}