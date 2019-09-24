package com.github.drichm.ev3.server.api;

import java.io.InputStream;
import java.io.Reader;
import java.util.List;
import java.util.Map;

import com.github.drichm.ev3.server.utils.JSON;
import com.github.drichm.ev3.server.utils.Mime;

public interface IHttp
{
  //===========================================================================  READ
  
  /** HTTP method of request */
  public String                     method     ();

  /** Headers sent in request */
  public Map<String, String>        headers    ();

  /** URI part between the domain name and the query string */
  public String                     path       ();

  /** URI query string parameters */
  public Map<String, List<String>>  parameters ();
  
  /** POSTed data as InputStream */
  public InputStream                inputStream();
  
  /** POSTed data as Reader */
  public Reader                     reader     ();
  
  /** POSTed data parsed as JSON */
  default public <T> T              readJSON   ( Class<T> clazz )  { return JSON.parse( reader(), clazz ); }

  
  //===========================================================================  WRITE

  /** Write text to client */
  public void                       write( String text, Mime mime );

  /** Convert object to JSON and send to client */
  default public void              json( Object x )                 { write( JSON.stringify( x ), Mime.JSON ); }

  /** Write input stream to client, CALLER MUST CLOSE STREAM - it will be closed for you */
  public void                       write( InputStream in, long length, Mime mime );

  /** Send status code with message */
  public void                       status( int code, String message );

  /** Send status code with no message */
  default public void               status( int code )  { status( code, "" ); }

  /* Send redirect to client */
  public void                       redirect( String url );

}
