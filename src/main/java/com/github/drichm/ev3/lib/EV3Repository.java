package com.github.drichm.ev3.lib;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import com.github.drichm.ev3.lib.hardware.DeviceNodeInstance;
import com.github.drichm.ev3.lib.hardware.SysFS;

/**
 * All devices currently attached to the EV3
 */
public class EV3Repository
{
  // ==========================================================================

  public final SysFS sysfs;
  
  /** subpath:instance map for active node instances */
  private final Map<String,DeviceNodeInstance> active = new ConcurrentHashMap<>();
  
  
  public EV3Repository( SysFS sysfs )
  {
    this.sysfs = sysfs;
  }
  
  
  // ==========================================================================
  
  /** TODO replace with registered Devices */
  static private Set<String> scannable = new HashSet<String>();
  static
  {
    //scannable.add( "lego-port" );
    scannable.add( "lego-sensor" );
    scannable.add( "tacho-motor" );
    scannable.add( "leds" );
  }


  public void scan()
  {
    Map<String,DeviceNodeInstance> now = sysfs.nodes( scannable ).collect( Collectors.toMap( n -> n.name(), n -> n ) );
    
    DeviceNodeInstance[] removed = active.values().stream().filter( v -> !now   .containsKey( v.name() ) ).toArray( DeviceNodeInstance[]::new );
    DeviceNodeInstance[] added   = now   .values().stream().filter( v -> !active.containsKey( v.name() ) ).toArray( DeviceNodeInstance[]::new );
    
    Arrays.stream( removed ).forEach( n -> active.remove( n.name() ) );
    Arrays.stream( added   ).forEach( n -> active.put   ( n.name(), n ) );
    
    // TODO notify monitors: added & removed
  }
  
  
  public DeviceNodeInstance[] active()
  {
    return active.values().stream().toArray( DeviceNodeInstance[]::new );
  }

}