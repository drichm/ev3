package com.github.drichm.ev3.server.utils;

import static java.time.temporal.ChronoField.DAY_OF_MONTH;
import static java.time.temporal.ChronoField.HOUR_OF_DAY;
import static java.time.temporal.ChronoField.MINUTE_OF_HOUR;
import static java.time.temporal.ChronoField.MONTH_OF_YEAR;
import static java.time.temporal.ChronoField.YEAR;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.time.format.SignStyle;
import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.Date;

import com.github.drichm.ev3.server.Defaults;

/**
 * Utilities to help with java.util.Date and java.time.LocalDate
 */
public class Dates
{
  //===========================================================================
  
  /** Fields to set for Midnight */
  static private final int[] MIDNIGHT_FIELDS = { Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND };

  /** Does given date have a time */
  static public boolean hasTime( Date date )
  {
    if ( date == null )
      return false;

    Calendar c = Calendar.getInstance();
    c.setTime( date );

    for ( int fld: MIDNIGHT_FIELDS )
      if ( c.get( fld ) != 0 )
        return true;

    return false;

  } // end of hasTime
  
  
  //===========================================================================

  /** ZoneId that JVM us running in */
  static public ZoneId JVM_ZONEID = ZoneId.systemDefault(); //ZoneOffset.ofTotalSeconds( 0 ); //TimeZone.getDefault().toZoneId();

  
  //===========================================================================
  // LocalDate
  
  /** Extract millis time from date */
  static public long toTime( LocalDate date )
  {
    return date == null ? 0L : Dates.toDate( date ).getTime();
  }
  
  /** Convert a 1970 millisecs value to a LocalDate */
  static public LocalDate toLocalDate( long millis )
  {
    return toLocalDateTime( millis ).toLocalDate();
  }

  /** Convert a Date to a Java 8 LocalDate */
  static public LocalDate toLocalDate( Date date )
  {
    if ( date instanceof java.sql.Date )
      return ((java.sql.Date) date).toLocalDate();
    else
      return date == null ? null : date.toInstant().atZone( JVM_ZONEID ).toLocalDate();
  }

  /** Convert a Java 8 LocalDate to a Java date @ midnight */
  static public Date toDate( LocalDate date )
  {
    return date == null ? null : Date.from( date.atStartOfDay( JVM_ZONEID ).toInstant() );
  }
  


  //===========================================================================
  // LocalDateTime

  /** Extract millis time from date */
  static public long toTime( LocalDateTime date )
  {
    return date == null ? 0L : Dates.toDate( date ).getTime();
  }
  
  /**
   * Convert a Date to a Java 8 LocalDateTime
   *<p>
   * Note: 
   * Passing a {@link java.sql.Date} will throw an exception, since it has no time component.
   * You will need to use {@link java.sql.Timestamp#toLocalDateTime()}
   */
  static public LocalDateTime toLocalDateTime( Date date )
  {
    return date == null ? null : date.toInstant().atZone( JVM_ZONEID ).toLocalDateTime();
  }

  /** Convert a Java 8 LocalDateTime to a Java date with time */
  static public Date toDate( LocalDateTime date )
  {
    return date == null ? null : Date.from( date.atZone( JVM_ZONEID ).toInstant() );
  }

  /** Convert a 1970 millisecs value to a LocalDateTime */
  static public LocalDateTime toLocalDateTime( long millis )
  {
    return LocalDateTime.ofInstant( Instant.ofEpochMilli( millis ), ZoneId.systemDefault() );
  }

  
  //===========================================================================

  /** Generate a human readable timestamp for now */
  static public String datestamp()                   { return datestamp( LocalDate.now() ); }

  /** Generate a human readable timestamp for date */
  static public String datestamp( LocalDate     d )  { return d == null ? null : d.format( DateTimeFormatter.ISO_LOCAL_DATE ); }

  /** Generate a human readable timestamp for date */
  static public String datestamp( Date          d )  { return datestamp( toLocalDate( d ) ); }


  //===========================================================================
  
  static private final DateTimeFormatter HH_MM = new DateTimeFormatterBuilder()
        .appendValue  ( HOUR_OF_DAY     , 2 )
        .appendLiteral( ':' )
        .appendValue  ( MINUTE_OF_HOUR  , 2 )
        .toFormatter  ( Defaults.LOCALE );
      
  
  
  /** Generate a human readable HH:MM time only for now */
  static public String hhmmstamp()                   { return hhmmstamp( LocalDateTime.now() ); }

  /** Generate a human readable HH:MM time only for now */
  static public String hhmmstamp( LocalDateTime d )  { return d == null ? null : d.format( HH_MM ); }

  /** Generate a human readable HH:MM time only for now */
  static public String hhmmstamp( Date          d )  { return hhmmstamp( toLocalDateTime( d ) ); }

  
  //===========================================================================
  
  /** ISO date format: YYYY-MM-DD */
  static public final DateTimeFormatter ISO_LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE;

  /** ISO time format without nanoseconds: HH:MM:SS */
  static public final DateTimeFormatter ISO_LOCAL_TIME = new DateTimeFormatterBuilder()
        .appendValue  ( ChronoField.HOUR_OF_DAY     , 2 )
        .appendLiteral( ':' )
        .appendValue  ( ChronoField.MINUTE_OF_HOUR  , 2 )
        .optionalStart()
        .appendLiteral( ':' )
        .appendValue  ( ChronoField.SECOND_OF_MINUTE, 2 )
        .toFormatter  ( Defaults.LOCALE );

  /** Generate a human readable timestamp for now */
  static public String timestamp()                   { return timestamp( LocalDateTime.now() ); }

  /** Generate a human readable timestamp for date */
  static public String timestamp( LocalDateTime d )  { return d == null ? null : d.format( ISO_LOCAL_DATE ) + " " + d.format( ISO_LOCAL_TIME ); }

  /** Generate a human readable timestamp for date */
  static public String timestamp( Date          d )  { return timestamp( toLocalDateTime( d ) ); }
  
  
  //===========================================================================

  /** Basic timestamp format */
  static private final DateTimeFormatter SHORT = new DateTimeFormatterBuilder()
        .appendValue  ( ChronoField.DAY_OF_MONTH  , 2 )
        .appendLiteral( '.' )
        .appendValue  ( ChronoField.MONTH_OF_YEAR , 2 )
        .appendLiteral( ' ' )
        .appendValue  ( ChronoField.HOUR_OF_DAY   , 2 )
        .appendLiteral( '.' )
        .appendValue  ( ChronoField.MINUTE_OF_HOUR, 2 )
        .toFormatter  ( Defaults.LOCALE );
  
  /** Generate a very short readable timestamp (used in footers on pages) */
  static public String timestampShort()                   { return timestampShort( LocalDateTime.now() ); }

  /** Generate a very short readable timestamp (used in footers on pages) */
  static public String timestampShort( Date          d )  { return timestampShort( toLocalDateTime( d ) ); }

  /** Generate a very short readable timestamp (used in footers on pages) */
  static public String timestampShort( LocalDateTime d )  { return d == null ? null : d.format( SHORT ); }


  //===========================================================================
  
  static public final DateTimeFormatter YYYYMMDD = new DateTimeFormatterBuilder()
        .appendValue( YEAR, 4, 10, SignStyle.EXCEEDS_PAD )
        .appendValue( MONTH_OF_YEAR, 2 )
        .appendValue( DAY_OF_MONTH , 2 )
        .toFormatter( Defaults.LOCALE  );
  
  static public String yyyymmdd      ( Date         d )  { return yyyymmdd( toLocalDate( d ) ); }
  static public String yyyymmdd      ( LocalDate    d )  { return d == null ? null : d.format( YYYYMMDD ); }
  
  
  //===========================================================================
  // Parsing

  /** Parse text using {@link #ISO_LOCAL_DATE} - return null if parsing fails */
  static public LocalDate toLocalDate( CharSequence text )
  {
    return toLocalDate( ISO_LOCAL_DATE, text );
  }

  /** Parse text using formatter - return null if parsing fails */
  static public LocalDate toLocalDate( DateTimeFormatter f, CharSequence text )
  {
    try
    {
      return LocalDate.from( f.parse( text ) );
    }
    catch ( DateTimeParseException e )
    {
      return null;
    }
  }


  /** Parse text using {@link DateTimeFormatter#ISO_LOCAL_DATE_TIME} - return null if parsing fails */
  static public LocalDateTime toLocalDateTime( CharSequence text )
  {
    return toLocalDateTime( DateTimeFormatter.ISO_LOCAL_DATE_TIME, text );
  }

  /** Parse text using formatter - return null if parsing fails */
  static public LocalDateTime toLocalDateTime( DateTimeFormatter f, CharSequence text )
  {
    try
    {
      return LocalDateTime.from( f.parse( text ) );
    }
    catch ( DateTimeParseException e )
    {
      return null;
    }
  }

} // end of class Dates
