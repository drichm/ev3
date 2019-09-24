package com.github.drichm.ev3.server.servlet;

import java.nio.file.Paths;
import java.util.Arrays;

import com.github.drichm.ev3.lib.EV3Repository;
import com.github.drichm.ev3.lib.hardware.AttributeInstance;
import com.github.drichm.ev3.lib.hardware.DeviceNodeInstance;
import com.github.drichm.ev3.lib.hardware.SysFS;
import com.github.drichm.ev3.server.Defaults;
import com.github.drichm.ev3.server.api.IHttp;


/** Serve EV3 status as JSON */
public class Repository extends UrlContextServer
{
  //===========================================================================

  static public final EV3Repository REPOSITORY = 
   new EV3Repository( Defaults.ON_WINDOWS ? new SysFS( Paths.get( "/temp/sys/class" ) ) : new SysFS() );

  
  //===========================================================================

  public class JsonReply
  {
    public final JsonEntry[] nodes;
    
    public long millis;

    public JsonReply( DeviceNodeInstance[] nodes )
    {
      this.nodes  = Arrays.stream( nodes  ).map( JsonEntry::new ).toArray( JsonEntry[]::new );
    }
  }

  public class JsonEntry
  {
    public final DeviceNodeInstance  node;
    public final AttributeInstance[] attr;
    
    public JsonEntry( DeviceNodeInstance node )
    {
      this.node = node;
      this.attr = repo.sysfs.attributes( node ).toArray( AttributeInstance[]::new );
    }
  }
  

  
  //===========================================================================
  
  public final EV3Repository repo;
  
  public Repository( String urlContext )
  {
    super( urlContext );

    this.repo = REPOSITORY;
  }

  
  //===========================================================================

  @Override public void serve( IHttp http, String path )
  {
    long millis = System.currentTimeMillis();

    repo.scan();
    
    JsonReply json = new JsonReply( repo.active() );

    json.millis = System.currentTimeMillis() - millis;
    
    http.json( json );
  }

}
