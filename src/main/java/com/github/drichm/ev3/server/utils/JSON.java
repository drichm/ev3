package com.github.drichm.ev3.server.utils;

import java.io.Reader;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;

import com.github.drichm.ev3.server.Defaults;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.internal.bind.util.ISO8601Utils;

/**
 * JSON helpers, most delegated to GSON
 *<p>
 * Abstracted here to allow us to switch from GSON to something else if need be.
 * Jackson ( https://github.com/FasterXML/jackson ) is a possibility
 */
public class JSON
{
  //===========================================================================

  static private final DateTypeAdapter DT = new DateTypeAdapter();

  static private final Gson GSON = 
      new GsonBuilder()
        .registerTypeAdapter( java.util.Date.class, DT )
        .registerTypeAdapter( java.sql.Date .class, DT )
        .create()
        ;
  
  static public Gson gson()
  {
    return GSON;
  }

  
  //===========================================================================
  // Parse
  
  /** 
   * Parse object of class from JSON
   *<p>
   * For an array of objects:
   * <pre>
   * XYZ[] xyz = Json.parse( src, XYZ[].class );
   * </pre>
   *<p> 
   * Note: DOES NOT WORK WITH {@code Collection<XYZ>();}
   */
  static public <T> T parse( Reader json, Class<T> clazz )
  {
    return gson().fromJson( json, clazz );
  }

  
  /** 
   * Parse object of class from JSON
   *<p>
   * For an array of objects:
   * <pre>
   * XYZ[] xyz = Json.parse( src, XYZ[].class );
   * </pre>
   *<p> 
   * Note: DOES NOT WORK WITH {@code Collection<XYZ>();}
   */
  static public <T> T parse( String json, Class<T> clazz )
  {
    return gson().fromJson( json, clazz );
  }
  

  //===========================================================================
  // Format

  static public String stringify( boolean x ) { return gson().toJson( x ); }
  static public String stringify( String  x ) { return gson().toJson( x ); }
  static public String stringify( Enum<?> x ) { return gson().toJson( x == null ? null : x.name() ); }
  static public String stringify( Object  x ) { return gson().toJson( x ); }

  
  /** Write object as JSON text into given writer */
  static public void stringifyInto( Object src, Appendable writer )  { gson().toJson( src, writer ); }

  /** 
   * Write object as class type as JSON text into given writer
   *<p>
   * If src is an array of objects then pass as:
   * <pre>
   * XYZ[] src;
   * 
   * Json.stringifyInto( src, XYZ[].class, writer );
   * </pre>
   *<p> 
   * Note: DOES NOT WORK WITH {@code Collection<XYZ>();}
   */
  static public void stringifyInto( Object src, Class<?> clazz, Appendable writer )
  {
    gson().toJson( src, clazz, writer );
  }

  
  //===========================================================================
  
  /** Parse a json string as a Date - returns null if not a date */
  static public Date parseDate( String date )
  {
    try
    {
      return ISO8601Utils.parse( date, new ParsePosition(0) );
    }
    catch (ParseException e) 
    {
      return null;
    }
  }

  /** Format date in JSON format */
  static public String formatDate( Date date )
  {
    return ISO8601Utils.format( date );
  }


  
  //===========================================================================
  // Parsing Maps

  /**
   * Parse an array of maps
   *<p>
   * Limited Java generics means GSON cannot do this, it can only parse collections of a 
   * specific generic type (i.e. not maps)
   * 
   * @see <a href='https://sites.google.com/site/gson/gson-user-guide#TOC-Collections-Examples'> examples </a>
   */
  static public List<Map<String,Object>> parseMapArray( String json )
  {
    List<Map<String,Object>> list = new LinkedList<Map<String,Object>>();

    if ( json == null )
      return list;
    
    json = json.trim();
    
    if ( json.equals( "[]" ) )
      return list;

    if ( json.startsWith( "[" ) )
      json = json.substring( 2, json.length() - 2 );
    // else: not an array, assume is a single map value

    for ( String entry: json.split( Pattern.quote( "},{" ) ) )
      list.add( parseMap( "{" + entry + "}" ) );
    
    return list;
  }

  static private final Class<?> MAP_CLASS = (new HashMap<String,Object>()).getClass();

  @SuppressWarnings("unchecked")
  static public Map<String,Object> parseMap( String json )
  {
    if ( json == null )
      return new HashMap<String,Object>();
    else
      return (Map<String,Object>) gson().fromJson( json, MAP_CLASS );
  }

  /** Parse String out of map from parseMap() */
  static public String parseMapString( Map<String,Object> map, String key )
  {
    Object x = map.get( key );
    return (x == null) ? null : x.toString();
  }

  /** Parse Double out of map from parseMap() */
  static public Double parseMapDouble( Map<String,Object> map, String key )
  {
    Object x = map.get( key );
    return (x == null) ? null : Double.parseDouble( x.toString() );
  }

  /** Parse Boolean out of map from parseMap() */
  static public Boolean parseMapBoolean( Map<String,Object> map, String key )
  {
    Object x = map.get( key );
    return (x == null) ? null : Boolean.parseBoolean( x.toString() );
  }


  //===========================================================================

  /**
   * GSON DateType adapter to do something reasonable with dates
   */
  static public class DateTypeAdapter implements JsonSerializer<Date>, JsonDeserializer<Date>
  {
    //===========================================================================
    // Configuration
    
    private final DateFormat date8601     = new SimpleDateFormat( "yyyy-MM-dd"              , Defaults.LOCALE );
    private final DateFormat datetime8601 = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss'Z'", Defaults.LOCALE );
    
    /** Sequence of patterns to try when parsing a data/datetime */
    private final DateFormat[] parse = new DateFormat[]
    {
      datetime8601,
      date8601,
    };
    
    
    public DateTypeAdapter()
    {
      this.datetime8601.setTimeZone( TimeZone.getTimeZone("UTC") );
    }
    
    
    //===========================================================================
    // JsonSerializer<Date>

    @Override public JsonElement serialize( Date src, Type typeOfSrc, JsonSerializationContext context ) 
    {
      DateFormat df = Dates.hasTime( src ) ? datetime8601 : date8601;
      
      synchronized (df) 
      {
        return new JsonPrimitive( df.format( src ) );
      }
    }

    
    //===========================================================================
    // JsonDeserializer<Date>

    /**
     * This is why we do this - 'Date' could be java.util.Date, java.sql.Date or java.sql.Timestamp
     * We can test what to do based on typeOfT
     */
    @Override public Date deserialize( JsonElement json, Type typeOfT, JsonDeserializationContext context )
      throws JsonParseException 
    {
      if ( !(json instanceof JsonPrimitive) )
        throw new JsonParseException( "The date should be a string value" );
      
      Date date = parse( json.getAsString() );

      if (typeOfT == Date.class)
        return date;
      else
      if (typeOfT == Timestamp.class)
        return new Timestamp( date.getTime() );
      else
      if (typeOfT == java.sql.Date.class)
        return new java.sql.Date( date.getTime() );
      else
        throw new IllegalArgumentException( getClass() + " cannot deserialize to " + typeOfT );
    }

    /** Parser */
    private Date parse( String date ) throws JsonSyntaxException
    {
      // Newest Jackson can format as Date.getTime() long value
      try
      {
        return new Date( Long.parseLong( date ) );
      }
      catch ( NumberFormatException e )
      { // not a long
      }
      
      // parse in some date format
      ParseException e = null;

      for ( DateFormat df: parse )
        try
        {
          synchronized (df)
          {
            return df.parse( date );
          }
        }
        catch ( ParseException ignored )
        {
          e = ignored;
        }
      
      throw new JsonSyntaxException( date, e );
    }
      

  } // end of class DateTypeAdapter


} // end of class JSON

