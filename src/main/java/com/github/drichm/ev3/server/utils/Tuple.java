package com.github.drichm.ev3.server.utils;

/**
 * Simple two value tuple
 */
public class Tuple<V1, V2>
{
  //===========================================================================
  
  static public <V1, V2> Tuple<V1, V2> of( V1 v1, V2 v2 )  { return new Tuple<>( v1, v2 ); }


  //===========================================================================

  public final V1 v1;
  public final V2 v2;

  public Tuple(V1 v1, V2 v2)
  {
    this.v1 = v1;
    this.v2 = v2;
  }

  
  //===========================================================================

  /** Method access for this::v1 style access */
  public V1 v1()  { return v1; }

  /** Method access for this::v2 style access */
  public V2 v2()  { return v2; }
  
  
  /** Return a new tuple with v1 and v2 swapped */
  public Tuple<V2, V1> flip()  { return of( v2, v1 ); }


  //===========================================================================
  
  @Override public boolean equals( Object o )
  {
    if (this == o)
      return true;

    if ( !(o instanceof Tuple) )
      return false;

    Tuple<?,?> tuple = (Tuple<?,?>) o;

    if ( v1 != null ? !v1.equals(tuple.v1) : tuple.v1 != null )
      return false;

    if ( v2 != null ? !v2.equals(tuple.v2) : tuple.v2 != null )
      return false;

    return true;
  }

  @Override public int hashCode()
  {
    int result = v1 != null ? v1.hashCode() : 0;
    result = 31 * result + (v2 != null ? v2.hashCode() : 0);
    return result;
  }

  @Override public String toString()
  {
    return "Tuple [v1=" + v1 + ", v2=" + v2 + "]";
  }

}// end of class Tuple

