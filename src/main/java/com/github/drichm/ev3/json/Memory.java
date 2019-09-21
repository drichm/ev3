package com.github.drichm.ev3.json;

import java.util.List;

/** Java version and Memory status */
public class Memory
{
  public final List<Integer> javaVersion;
  public final long availableProcessors;
  public final long freeMemory;
  public final long maxMemory;
  public final long totalMemory;

  public Memory()
  {
    Runtime r = Runtime.getRuntime();
    
    this.javaVersion = Runtime.version().version();

    this.availableProcessors = r.availableProcessors();
    this.freeMemory          = r.freeMemory();
    this.maxMemory           = r.maxMemory();
    this.totalMemory         = r.totalMemory();
  }
  
}

