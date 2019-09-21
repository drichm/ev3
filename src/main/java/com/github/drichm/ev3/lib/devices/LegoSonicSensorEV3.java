package com.github.drichm.ev3.lib.devices;

import com.github.drichm.ev3.lib.device.Attribute;
import com.github.drichm.ev3.lib.device.Mode;
import com.github.drichm.ev3.lib.device.Type;

/**
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensors.html#the-lego-sensor-subsytem
 * @see http://docs.ev3dev.org/projects/lego-linux-drivers/en/ev3dev-jessie/sensor_data.html#lego-ev3-ultrasonic-sensor 
 */
public class LegoSonicSensorEV3 extends LegoSensor
{
  public LegoSonicSensorEV3()
  {
    super( new Attribute( "driver_name", "lego-ev3-us" )
         , new Attribute( "mode"       , "US-DIST-CM", "US-DIST-IN", "US-LISTEN", "US-SI-CM", "US-SI-IN", "US-DC-CM", "US-DC-IN" )
         , new Attribute( "modes"      , Type.sl, Mode.ro )
         , new Attribute( "units"      , Type.s , Mode.ro )
         , new Attribute( "value{N}"   , Type.s , Mode.ro )
         );
  }
}
