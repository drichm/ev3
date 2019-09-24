package com.github.drichm.ev3.server;

import java.io.IOException;

import com.github.drichm.ev3.server.api.IHttp;
import com.github.drichm.ev3.server.api.IServer;
import com.github.drichm.ev3.server.api.IServlet;
import com.github.drichm.ev3.server.api.ISocket;
import com.github.drichm.ev3.server.servlet.RawDevice;
import com.github.drichm.ev3.server.servlet.Repository;
import com.github.drichm.ev3.server.servlet.Status;
import com.github.drichm.ev3.server.servlet.WebServer;


/** Entry point for all HTTP requests */
public class Index  implements IServlet
{
  
  // ==========================================================================

  /** Debug Web-app */
  static public final WebServer   DEBUG      = new WebServer ( "/debug/", Defaults.DEBUG_APP );

  /** EV3 repository */
  static public final Repository  REPOSITORY = new Repository( "/repo" );

  /** Raw Device access */
  static public final RawDevice   DEVICE     = new RawDevice ( "/device", REPOSITORY.repo );

  /** Java status */
  static public final Status      STATUS     = new Status    ( "/status" );
  
  static public final IServer     ROOT  = new IServer()
  {
    @Override public boolean serve( IHttp http ) throws IOException
    {
      if ( http.path().isBlank() || http.path().equals( "/" ) )
      {
        http.redirect( DEBUG.urlContext2 );
        return true;
      }
      else
        return false;
    }
  };

  
  //===========================================================================

  
  @Override
  public boolean serve( IHttp http ) throws IOException
  {
    return DEBUG     .serve( http )
        || REPOSITORY.serve( http )
        || DEVICE    .serve( http )
        || STATUS    .serve( http )
        
        // final test
        || ROOT      .serve( http )
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
