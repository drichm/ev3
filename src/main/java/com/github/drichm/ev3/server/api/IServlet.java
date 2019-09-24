package com.github.drichm.ev3.server.api;

import java.io.IOException;

public interface IServlet extends IServer
{
	
	default public void onConnect( ISocket socket )                  throws IOException  {};
	default public void onError  ( ISocket socket, Throwable t)      throws IOException  {};
	default public void onMessage( ISocket socket, String message )  throws IOException  {};
	default public void onClose  ( int statusCode, String reason )   throws IOException  {};

}
