package com.github.drichm.ev3.lib.devices;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.github.drichm.ev3.lib.device.Attribute;
import com.github.drichm.ev3.lib.device.DeviceNode;

/**
 * Abstract class for a logical device
 * 
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/
 */
public class Device
{
  public final DeviceNode            node;
  public final Map<String,Attribute> attributes = new HashMap<>();
  
  public Device( DeviceNode node )
  {
    this.node = node;
  }

  protected void add( Attribute... add )
  {
    if ( add != null )
      Arrays.stream( add ).forEach( a -> attributes.put( a.name(), a ) );
  }

}
