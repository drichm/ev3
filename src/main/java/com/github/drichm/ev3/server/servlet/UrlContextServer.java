package com.github.drichm.ev3.server.servlet;

import com.github.drichm.ev3.server.api.IHttp;
import com.github.drichm.ev3.server.api.IServer;

/** Abstract mini-server - a server that serves URLs under some root context URL */
abstract public class UrlContextServer implements IServer
{
  //===========================================================================
  
  public final String  urlContext;
  public final String  urlContext2;
  public final boolean needSlash;
  
  /** Map URL context to root directory */
  public UrlContextServer( String urlContext )
  {
    this.needSlash    = urlContext.endsWith( "/" );
    
    if ( needSlash )
      this.urlContext = urlContext.substring( 0, urlContext.length() - 1 ).trim();
    else
      this.urlContext = urlContext.trim();

    this.urlContext2  = this.urlContext + "/";
  }
  

  //===========================================================================
  
  /**
   * Process URL
   * 
   * @param http HTTP interface
   * @param path path relative to this.urlContext, without leading '/'
   */
  abstract protected void serve( IHttp http, String path );
  
  
  //===========================================================================
  
  /**
   * Serve file for given request
   * 
   * @return true if served, false if request does not match this.url
   */
  @Override public boolean serve( IHttp http )
  {
    String path = http.path();
    
    if ( !path.startsWith( urlContext ) )
      return false;
    else
    if ( path.equals( urlContext ) )
    {
      if ( needSlash )
        http.redirect( urlContext2 );
      else
        serve( http, "" );
      
      return true;
    }
    else
    if ( !path.startsWith( urlContext2 ) )
      return false;
    else
    {
      serve( http, path.substring( urlContext2.length() ).trim() );
    
      return true;
    }
  }

}
