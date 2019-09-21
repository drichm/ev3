package com.github.drichm.ev3.server.servlet;

import java.io.IOException;


/** WebSocket interface */
public interface ISocket
{
	public void send( String text ) throws IOException;

}
