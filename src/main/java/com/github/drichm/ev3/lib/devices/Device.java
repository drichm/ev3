package com.github.drichm.ev3.lib.devices;

import com.github.drichm.ev3.lib.device.Attribute;
import com.github.drichm.ev3.lib.device.DeviceNode;

/**
 * Abstract class for a logical device
 * 
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/
 */
public class Device
{
  public final DeviceNode  node;
  public final Attribute[] attributes;
  
  public Device( DeviceNode node, Attribute... attributes )
  {
    this.node       = node;
    this.attributes = attributes;
  }

}
