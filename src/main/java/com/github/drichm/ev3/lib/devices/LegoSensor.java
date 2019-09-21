package com.github.drichm.ev3.lib.devices;

import com.github.drichm.ev3.lib.device.Attribute;
import com.github.drichm.ev3.lib.device.DeviceNode;

/**
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensors.html#the-lego-sensor-subsytem
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#lego-ev3-ultrasonic-sensor 
 */
public class LegoSensor extends Device
{
  protected LegoSensor( Attribute... attributes )
  {
    super( new DeviceNode( "lego-sensor", "sensor{N}" ), attributes );
  }
}
