package com.github.drichm.ev3.server;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.github.drichm.ev3.server.servlet.IHttp;
import com.github.drichm.ev3.server.utils.Mime;

/**
 * Map URLs to files and serve the file
 */
public class WebServer extends UrlContextServer
{
  //===========================================================================
  
  public final Path   root;
  
  /** Map URL context to root directory */
  public WebServer( String urlContext, Path root )
  {
    super( urlContext );
    
    this.root = root;
  }
  
  
  //===========================================================================

  /** Serve file for given request */
  @Override public void serve( IHttp http, String path )
  {
    serve( http, (path.isEmpty() ? root : root.resolve( path )).toFile(), null );
  }
  
  
  /** Serve file, or index.html if file is a directory */
  public void serve( IHttp http, File file, Mime mime )
  {
    //String etag = Integer.toHexString((file.getAbsolutePath() + file.lastModified() + "" + file.length()).hashCode());
    
    if ( file == null )
    {
      http.status( 404 );
      return;
    }
    
    if ( file.isDirectory() )
    {
      file = new File( file, "index.html" );
      mime = Mime.HTML;
    }

    if ( !file.exists() )
      http.status( 404, file.getAbsolutePath() );
    else
      try
      {
        //System.out.println( file.getAbsolutePath() );
        
        http.write( Files.newInputStream( file.toPath() ), file.length(), mime != null ? mime : Mime.from( file, Mime.UNKNOWN ) );
      }
      catch ( IOException e )
      {
        throw new RuntimeException( file.getAbsolutePath(), e  ); 
      }
  }

} // end of class WebServer

