package com.github.drichm.ev3.server;

import java.nio.file.Paths;

import com.github.drichm.ev3.lib.EV3Repository;
import com.github.drichm.ev3.lib.hardware.DeviceNodeInstance;
import com.github.drichm.ev3.lib.hardware.SysFS;
import com.github.drichm.ev3.server.servlet.IHttp;


/** Serve EV3 status as JSON */
public class Repository extends UrlContextServer
{
  //===========================================================================

  static public final EV3Repository REPOSITORY = 
   new EV3Repository( Defaults.ON_WINDOWS ? new SysFS( Paths.get( "/temp/sys/class" ) ) : new SysFS() );

  
  //===========================================================================

  static public class JsonReply
  {
    public final DeviceNodeInstance[] nodes;
    
    public long millis;

    public JsonReply( DeviceNodeInstance[] nodes )
    {
      this.nodes  = nodes;
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
