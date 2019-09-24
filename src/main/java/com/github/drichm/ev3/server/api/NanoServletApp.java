package com.github.drichm.ev3.server.api;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import com.github.drichm.ev3.server.Defaults;
import com.github.drichm.ev3.server.Index;
import com.github.drichm.ev3.server.utils.Mime;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.NanoHTTPD.Response.Status;
//import fi.iki.elonen.NanoWSD;
//import fi.iki.elonen.NanoWSD.WebSocketFrame.CloseCode;

// @see https://github.com/NanoHttpd/nanohttpd
public class NanoServletApp extends NanoHTTPD
{
  //==========================================================================
	
  static protected class Setup
  {
    public final int     port;
    public final boolean secure;
    
    public Setup( int port, boolean secure )
    {
      this.port   = port;
      this.secure = secure;
    }
    
    public void inform()
    {
      String http = secure ? "https://" : "http://";
      String host = Defaults.ON_WINDOWS ? "localhost" : "ev3dev";
      String port = (secure && this.port == 443) || (!secure && this.port == 80) ? "" : ":" + this.port;

      System.out.println("Running @ " + http + host + port );
    }
  }
  
  static protected final Setup HTTP  = new Setup( 8080, false );
  static protected final Setup HTTPS = new Setup( 8443, true  );
  
  static protected final Setup USE = HTTP;
  

  //==========================================================================
	
  static private final IServlet servlet = new Index(); 

  public NanoServletApp() throws IOException {
    super( USE.port );
    
    if ( USE.secure )
      makeSecure(  NanoHTTPD.makeSSLSocketFactory( "/keystore.jks", "password".toCharArray()), null );

    USE.inform();
    
    start( NanoHTTPD.SOCKET_READ_TIMEOUT, false );
    
  }

  static public void main(String[] args) {
    try {
        new NanoServletApp();
    } catch (IOException ioe) {
        System.err.println("Couldn't start server:\n" + ioe);
    }
  }

  
  //==========================================================================

  @Override
  public Response serve( IHTTPSession session ) 
  {
  	try 
  	{ 
  	  Http http = new Http( session );
  	  servlet.serve( http );
  	  return http.response();
    }
  	catch (IOException e)
  	{ 
  	  // TODO log these online ?
  	  e.printStackTrace();
  	  return newFixedLengthResponse( e.toString() );
  	}
  }
  
  
  //==========================================================================

  /** HTTP interface implementation */
  public class Http implements IHttp
  {
  	protected final IHTTPSession session;
  	protected       Response     response;
  	
  	public Http( IHTTPSession session )
  	{ 
  	  this.session = session;
  	}
  	
  	@Override public String                    method     ()  { return session.getMethod().name(); }
  	@Override public Map<String, String>       headers    ()  { return session.getHeaders(); }
    @Override public String                    path       ()  { return session.getUri(); }
    @Override public Map<String, List<String>> parameters ()  { return session.getParameters(); }

    @Override public InputStream               inputStream()  { return session.getInputStream(); }
    @Override public Reader                    reader     ()  { return new InputStreamReader( session.getInputStream(), StandardCharsets.UTF_8 ); }
    
    
    protected void response( Mime mime, String msg    )  { response( Status.OK, mime, msg ); }
    protected void response( Status status, Mime mime )  { response( status, mime, "" ); }

    protected void response( Status status, Mime mime, String msg )
    {
      response = newFixedLengthResponse( status, mime.mimeType, msg );
    }

    @Override public void   status  ( int code, String message )  { response( Status.lookup( code ), Mime.HTML, message ); }
    @Override public void   write   ( String text, Mime mime )    { response( mime, text ); }

    @Override public void   write   ( InputStream in, long length, Mime mime ) 
    { 
      response = newFixedLengthResponse( Status.OK, ((mime != null) ? mime : Mime.UNKNOWN).mimeType, in, length ); 
    }

    @Override public void   redirect( String url )
    {
      response( Status.REDIRECT, Mime.HTML );
      response.addHeader( "Location", url );
    }
    
  	public Response response()
  	{ 
  	  if ( response == null )
  	    status( 404 );

      return response; 
  	}
  }

  
  
  //==========================================================================
  // WebSockets - do not work with Chrome (as-of September 2019)
  
//  @Override
//  protected WebSocket openWebSocket( IHTTPSession handshake )  { return new SocketHandler( handshake ); }
//  
//  	
//  /** Active websocket instance */
//  public class SocketHandler extends WebSocket
//  {
//
//    private final ISocket socket;
//    
//    public SocketHandler( IHTTPSession handshake )
//  	{
//      super( handshake );
//      	
//      this.socket = new Socket( this );
//  	}
//
//    @Override
//    protected void onOpen()
//    { try { servlet.onConnect( socket ); } catch (IOException e) { throw new RuntimeException(e); } }
//
//    @Override
//    protected void onMessage( WebSocketFrame message  )
//    { try { servlet.onMessage( socket, message.getTextPayload() ); } catch (IOException e) { throw new RuntimeException(e); } }
//
//    @Override
//    protected void onPong   ( WebSocketFrame message  )
//    {  }
//
//    @Override
//    protected void onException( IOException e )
//    { try { servlet.onError ( socket, e ); } catch (IOException x) { throw new RuntimeException(x); } }
//
//    @Override
//    protected void onClose  ( CloseCode code, String reason, boolean initiatedByRemote )
//    { try { servlet.onClose( code.getValue(), reason ); } catch (IOException e) { throw new RuntimeException(e); } }
//
//  }


  // ==========================================================================
  
  /** Abstract Socket implementation for NanoWSD */
//  static public class Socket implements ISocket
//  {
//  	private final SocketHandler handler;
//
//  	public Socket( SocketHandler handler )
//  	{
//  	  this.handler = handler;
//  	}
//  	
//    public void json( Object x ) throws IOException
//    {
//      handler.send( JSON.stringify( x ) );
//    }
//
//    public void send( String text ) throws IOException
//  	{
//  	  handler.send( text );
//  	}
//  }


}
