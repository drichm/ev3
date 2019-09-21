package com.github.drichm.ev3.server;

import java.io.IOException;

import com.github.drichm.ev3.server.servlet.IHttp;
import com.github.drichm.ev3.server.servlet.IServlet;
import com.github.drichm.ev3.server.servlet.ISocket;


/** Entry point for all HTTP requests */
public class Index  implements IServlet
{
  
  // ==========================================================================

  /** Debug Web-app */
  static public final WebServer   DEBUG      = new WebServer ( "/debug/", Defaults.DEBUG_APP );

  /** EV3 repository */
  static public final Repository  REPOSITORY = new Repository( "/repo" );

  /** Java status */
  static public final Status      STATUS     = new Status    ( "/status" );

  
  //===========================================================================

  
  @Override
  public boolean serve( IHttp http )
  {
    return DEBUG     .serve( http )
        || REPOSITORY.serve( http )
        || STATUS    .serve( http )
        ;
  }
  
  
  
  public void onConnect( ISocket socket ) throws IOException
  {
    socket.send( "Hello" );
  }
  
  @Override
  public void onMessage( ISocket socket, String message ) throws IOException
  {
    socket.send( message + " ... back to you" );
  }

}
