package com.github.drichm.ev3.server.api;

import java.io.IOException;

public interface IServer
{
  /*
   * @return true if served, false if HTTP request is not recognised or served by this server
   */
  public boolean serve( IHttp http ) throws IOException;

}
