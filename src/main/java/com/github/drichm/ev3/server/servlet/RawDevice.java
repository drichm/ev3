package com.github.drichm.ev3.server.servlet;

import com.github.drichm.ev3.lib.EV3Repository;
import com.github.drichm.ev3.server.api.IHttp;

/** Access device directly */
public class RawDevice extends UrlContextServer
{
  //===========================================================================

  static public class JsonReply
  {
    public long millis;
    
    public String error;
    
    public byte[] b;
    public String s;

    public JsonReply( byte[] b, String s )  { this.b = b; this.s = s; }
    public JsonReply( Throwable t  )            { this.error = t.toString(); }
  }

  

  //===========================================================================
  
  public final EV3Repository repo;
  
  public RawDevice( String urlContext, EV3Repository repo )
  {
    super( urlContext );
    
    this.repo = repo;
  }

  
  @Override public void serve( IHttp http, String path )
  {
    long millis = System.currentTimeMillis();
    
    JsonReply json;
    
    try
    {
      byte[] b = repo.sysfs.readBytes( path );
      String s;
      
      try
      {
        s = repo.sysfs.readString( path );
      }
      catch ( Throwable t )
      {
        s = null;
      }
      
      json = new JsonReply( b, s );
    }
    catch ( Throwable t )
    {
      json = new JsonReply( t );
    }

    json.millis = System.currentTimeMillis() - millis;
    
    http.json( json );
  }

}
