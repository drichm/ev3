package com.github.drichm.ev3.server.servlet;

import com.github.drichm.ev3.json.Memory;
import com.github.drichm.ev3.server.api.IHttp;


/** Serve EV3 status as JSON */
public class Status extends UrlContextServer
{
  //===========================================================================

  static public class JsonReply
  {
    public final Memory               memory;
    
    public long millis;

    public JsonReply()
    {
      this.memory = new Memory();
    }
  }

  

  //===========================================================================
  
  public Status( String urlContext )
  {
    super( urlContext );
  }

  
  @Override public void serve( IHttp http, String path )
  {
    long millis = System.currentTimeMillis();

    JsonReply json = new JsonReply();

    json.millis = System.currentTimeMillis() - millis;
    
    http.json( json );
  }

}
