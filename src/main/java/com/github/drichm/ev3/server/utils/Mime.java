package com.github.drichm.ev3.server.utils;

import java.io.File;
import java.util.EnumMap;
import java.util.Map;


/**
 * Common MIME types
 */
public enum Mime
{
  //===========================================================================
  
  CSS    ( "text/css"                 , "Cascading Stylesheet"       ),
  CSV    ( "text/csv"                 , "Comma Separated Variables"  ),
  HTML   ( "text/html"                , "HyperText Markup Language"  ),
  TEXT   ( "text/plain"               , "Plain Text"                 , "txt" ),
  DIF    ( "text/plain"               , "Data Interchange Format"    , "txt", null, Mime.TEXT ),
  RTF    ( "text/richtext"            , "Rich Text Format"           ),
  TSV    ( "text/tab-separated-values", "Tab-Separated Values"       ),
  LOG    ( "text/x-log"               , "Log file"                   ),
  
  DTD    ( "application/xml-dtd"      , "Document Type Definition"   ),
  JS     ( "application/javascript"   , "JavaScript"                 ),
  JSON   ( "application/json"         , "JavaScript Object Notation" ),
  SQL    ( "application/x-sql"        , "Structured Query Language"  , "sql" ),
  
  XML    ( "application/xml"          , "eXtensible Markup Language" ),
  MATHML ( "application/mathml+xml"   , "MathML"                     , "xml", null    , Mime.XML ),
  XSD    ( "application/xml"          , "XML Schema Definition"      , null , Mime.XML, null ),
  XSL    ( "application/xslt+xml"     , "XML Stylesheet"             ),

  BMP    ( "image/bmp"                , "Windows Bitmap"                         ),
  GIF    ( "image/gif"                , "Graphics Interchange Format"            ),
  ICO    ( "image/x-icon"             , "Microsoft Windows Icon"                 ),
  JPEG   ( "image/jpeg"               , "Joint Photographic Experts Group Image" , "jpg" ),
  PNG    ( "image/png"                , "Portable Network Graphics"              ),
  SVG    ( "image/svg+xml"            , "Scalable Vector Graphics"               ),
  TIFF   ( "image/tiff"               , "Tag Image File Format"                  , "tif" ),
  
  OTF    ( "font/otf"                 , "OpenType Font"         ),
  TTF    ( "font/ttf"                 , "True Type Font"        ),
  WOFF   ( "font/woff"                , "Web Open Font Format"  ),
  WOFF2  ( "font/woff2"               , "Web Open Font Format"  ),
  
  EOT    ( "application/vnd.ms-fontobject"                                             , "MS Embedded OpenType fonts" ),

  DOC    ( "application/msword"                                                        , "Microsoft Word" ),
  DOCX   ( "application/vnd.openxmlformats-officedocument.wordprocessingml.document"   , "Microsoft Word Document" ),
  DOTX   ( "application/vnd.openxmlformats-officedocument.wordprocessingml.template"   , "Microsoft Word Template" ),
  
  PPT    ( "application/vnd.ms-powerpoint"                                             , "Microsoft Powerpoint" ),
  PPTX   ( "application/vnd.openxmlformats-officedocument.presentationml.presentation" , "Microsoft Powerpoint Presentation" ),
  POTX   ( "application/vnd.openxmlformats-officedocument.presentationml.template"     , "Microsoft Powerpoint Template" ),
  PPSX   ( "application/vnd.openxmlformats-officedocument.presentationml.slideshow"    , "Microsoft Powerpoint Slideshow" ),
  
  XLS    ( "application/vnd.ms-excel"                                                  , "Microsoft Excel" ),
  XLSX   ( "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"         , "Microsoft Excel Sheet" ),
  XLTX   ( "application/vnd.openxmlformats-officedocument.spreadsheetml.template"      , "Microsoft Excel Template" ),
  
  PDF    ( "application/pdf"         , "Adobe Portable Document Format" ),

  ZIP    ( "application/zip"         , "ZIP File/Archive" ),

  // used by IE for an unknown mime-type
  UNKNOWN( "application/octet-stream", "Binary Octet Stream", "bin" );
  

  //===========================================================================
  // Configuration
  
  public final String mimeType;
  public final String description;
  public final String extension;
  
  /** Parent Mime for the mime-type, if any - this Mime should be used when searching on a mime-type */
  public final Mime   mimeTypeParent;

  /** Parent Mime for the file extension, if any - this Mime should be used when searching on an extension */
  public final Mime   extensionParent;

  Mime( String mimeType, String description )
  {
    this( mimeType, description, null );
  }

  Mime( String mimeType, String description, String extension )
  {
    this( mimeType, description, extension, null, null );
  }

  Mime( String mimeType, String description, String extension, Mime mimeTypeParent, Mime extensionParent )
  {
    this.mimeType        = mimeType;
    this.description     = description;
    this.extension       = (extension == null) ? name().toLowerCase() : extension;
    this.mimeTypeParent  = mimeTypeParent;
    this.extensionParent = extensionParent;
  }


  //===========================================================================
  // Accessors
  
  /** Does mime-type match this instance */
  public boolean is( String mimeType ) { return this.mimeType.equals(mimeType); }

  /** Is an image based mime-type */
  public boolean isImage      () { return mimeType.startsWith( "image/" ); }

  /** Is a text based mime-type */
  public boolean isText       () 
  { 
    switch ( this )
    {
      case DTD   :
      case JS    :
      case JSON  :
      case SQL   :
      case XML   :
      case MATHML:
      case XSD   :
      case XSL   :
        return true;
        
      default:
        return mimeType.startsWith( "text/" ) || isXML(); 
    }
  }

  /** Is a XML based mime-type */
  public boolean isXML()
  {
    switch ( this )
    {
      case XML:
      case XSD:
      case XSL: return true;

      default : return false;
    }
  } // end of isXML


  /** Return mime-type string */
  public String mimeType   () { return mimeType; }
  
  /** Return file extension */
  public String extension  () { return extension; }

  /** Return description */
  public String description() { return description; }

 

  //===========================================================================

  /**
   * Return Mime instance for Mime-Type string
   *
   * @return Mime instance, UNKNOWN if none defined for Mime-type
   */
  static public Mime get( String mimeType )
  {
    if ( mimeType != null )
    {
      for ( Mime mime: values() )
        if ( mimeType.equals( mime.mimeType ) )
          if ( mime.mimeTypeParent != null )
            return mime.mimeTypeParent;
          else
            return mime;

      // some 'deprecated' names
      if ( mimeType.equals("text/json") )
        return JSON;
      else
      if ( mimeType.equals("text/xml") )
        return XML;
      else
      if ( mimeType.equals("text/xsl") )
        return XSL;
    }

    // unknown mime-type
    return UNKNOWN;

  } // end of get
  
  /** Return mime for file extension - returns null if ext is unknown */
  static public Mime fromExtension( String ext )
  {
    if ( ext != null )
      for ( Mime mime: values() )
        if ( mime.extension.equalsIgnoreCase( ext ) )
          if ( mime.extensionParent != null )
            return mime.extensionParent;
          else
            return mime;
    
    return null;
  }

  static public Mime from( File file, Mime mimeNoExtension )
  {
    if ( file == null )
      return null;
    
    String x = file.getName();
    int    i = x.lastIndexOf( '.' );
    
    return i < 0 ? mimeNoExtension : fromExtension( x.substring( i+1 ).toLowerCase() );
  }

  
  //===========================================================================
  // ICONs for MIME
  
  /** Return a Data-URI for an Icon for this Mime type, null if none defined */
  public String dataURI()  { return MAP.get( this ); }
  
  static private final Map<Mime,String> MAP = new EnumMap<>( Mime.class );
  static
  {
    MAP.put( Mime.CSV , "data:image/gif;base64,R0lGODlhEAAQANUAAClMGZnMmWaZZu/27zSHMd3q3EFlK4WvdbLWqlyARgBmADdcJWSqW9jn2F6TSEaTP////0tyOY2qgXuma06MNB13HOPu4l2aVnGbXpW3ivb59jBSHXu7cnWyYkiHQVZ6QEZsMmGHTGisY4XBe2qWV9/s31aMPC9RK3agZYq1e6fQnZC8gebw5maOVFmCVmuta1mkVEyWSnS2bP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAUUADMALAAAAAAQABAAAAa6wFXqcJigMKRWKJT4fCKRFAKhUgWuGWFxMokiIGCwZqwZmFGRwzdMLpsH6IkK8hKJLmOX3sXCgOQQMQgcHA4uLzAECn0gKAFgDyoyDB6JiywkBhgBZAQBDDAgixYWmSQZZIkrDjELAgIlDgstK2MwHTAVI6wLEgWyISkaMCfFJxUyvL8LCQdvLNASxicNJhsfE2bQLBYl3t7V19nb3d8N5xQAEUZHSCQO8CYmFOlQESD4BvoL/Bv+AEEAADs=" );
    MAP.put( Mime.DIF , "data:image/gif;base64,R0lGODlhEAAQANUAAClMGZnMmWaZZu/27zSHMd3q3EFlK4WvdbLWqlyARgBmADdcJWSqW9jn2F6TSEaTP////0tyOY2qgXuma06MNB13HOPu4l2aVnGbXpW3ivb59jBSHXu7cnWyYkiHQVZ6QEZsMmGHTGisY4XBe2qWV9/s31aMPC9RK3agZYq1e6fQnZC8gebw5maOVFmCVmuta1mkVEyWSnS2bP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAUUADMALAAAAAAQABAAAAa6wFXqcJigMKRWKJT4fCKRFAKhUgWuGWFxMokiIGCwZqwZmFGRwzdMLpsH6IkK8hKJLmOX3sXCgOQQMQgcHA4uLzAECn0gKAFgDyoyDB6JiywkBhgBZAQBDDAgixYWmSQZZIkrDjELAgIlDgstK2MwHTAVI6wLEgWyISkaMCfFJxUyvL8LCQdvLNASxicNJhsfE2bQLBYl3t7V19nb3d8N5xQAEUZHSCQO8CYmFOlQESD4BvoL/Bv+AEEAADs=" );
    

    MAP.put( Mime.DOC , "data:image/gif;base64,R0lGODlhEAAQAOYAAAAxd8nS4pOitylhtwQ1gmZ9s+/1/ZmovghKsnej69zi9EFtxHaR1a260DVJYzFatpCu1kxpp3aKnIuk3QVBm6W83Nbc6p243uHs/P///yc9bCBLmj9ltI+x42aK1aq+8ai2zVR6zjlZmMfa+H+e5tPi+7HB11+AzYyv5Ojw/YCn5wcuizdgrtzo+9Pi+jZKZFx0q0hzzPT3/oOc2vDy+nql6JW04rfI9JOr5tfl+s7e+LrF1F58w66+1Iml6TlouJamu6+830Npua21ztbm/1RspAc7iHuX2IWs5pyrwLLC2FF2yld90f///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAUUAE0ALAAAAAAQABAAAAfOgE2CSoQ9hg0giYJNODg+JEceTEsLAzIGSS+COBmdnp0Il0A7mj4fMycWJwpFE6EGAi+yJAozPApCChESCBAQKRUVLwwZDEJBGxMiARQyvxgVDh4ZBUIwBA8aGc3PFw4hGUUiNwQEDRlGBhAYGN4xGSssNAQcnQS+GC02Dj+fnwAp8uXo4GAAgoMUEhohAMBXjhwoHCw6kKJiRYE5dCCRKAgIO18gIejQoYJjEyAtWjzMUaLEyBE1TApwceGCDRsoUCBRUSOBSQdAgwoNGggAOw==" );
    MAP.put( Mime.DOCX, "data:image/png;base64,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" );

    MAP.put( Mime.DTD , "data:image/gif;base64,R0lGODlhEAAQANUAAPL1+/n6/Ky81eTs+e3y+tzn99/p+OHq+Orw+ebu+WuQvPD1+/X4+/f5+yhilnt0T3pzT3t0UH51TntzT3tzUId6TIN3TX90TpF/SI19Sox9Sp2FRZeCRrCPPqyMQKeKQqeLQqKIQ+DIj7SQPdSyaNSyadq9fNWyaf///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAACgALAAAAAAQABAAAAZ8QJRwSCwOR8gk0igcBZ7Q0ojZaVivptO06GEwHGCFyETyFD8AgEMhWKdBxdBi4RAsFI55qLghEOoEeH4bRRwICHUIeIccRRgJCXUJeJAYRRoDA2ttCpkZRRUHB2BrogcVRRYGq6ysFkUSBbKzsxdFFBEPELm6DxQTTMFEQQA7" );
    
    MAP.put( Mime.GIF , "data:image/gif;base64,R0lGODlhEAAQAPcAAAAAAAA5nABKrQBStQhStRBjvRhrxhhzxiF7zilSpSlrvTlSlFJSSlJrrVqMznNzpYRzhIyczozO95RrGJR7QpSEc5Sl1pSl3pSt3pS155TW/5xrAJyctZylvZyt3py13py155y955y975zG76VzEKWEOaWEQqWltaWlvaW976XG76XO76XO961zAK17CK2lta2tva21xrV7ALV7CLWEALWtrbW1vbW1xrW1zrW9zrXv/72EAL2EIb2MAL2MKb29xr29zsaMAMaMMcaUAMbGxs6UAM6UOc6la87n/9bn/9bv/96UMd6ca97v997v/+fv9+fv/+f3/+/v9+/39+/3//f39/+tSv+1Uv+1Y/+9Y/+9a/+9c//Ge//OhP/OjP/WnP/enP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAAALAAAAAAQABAAAAjDAAEIHEiw4MARK0YoHBGiYQgQHwiqqCKlosUnT6JEPCiFyI8fOW7EgAHjyUaBI6T8qFGChAsTJ040OQkg5Y0NMmj0GOKCwxMMBFPGWCLEiA8aQSg48UAwhJQTV7586eKiBwknQAeGeHLCSpcuXHjQmIC16ZMOTLJgweKiBQQlFsxyaFBhyxEGCx4kiav1SYIDDnRIIGAgwF6CIJIIQKBAggIDBQQcHvihiZMkmJEkQaKZr0AMHzB4sDDagmkLEQyqLhgQADs=" );
    MAP.put( Mime.HTML, "data:image/gif;base64,R0lGODlhDwAPALMAAISGAAAAhISGhMbHxv/78P//AAAA/wAAAP///wAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAAQALAAAAAAPAA8AAARRkEhBa5WYCMQ7GkKmed2AhNjGGQZ3vAcWsEaAtNwQEwFQ0LZSrPfjzIKfmI8GFAIGR5ITMDMMoDWn4VBlIZOEFuygEkoMZakudW2715KxHBYBADs=" );
    MAP.put( Mime.JPEG, "data:image/gif;base64,R0lGODlhEAAQAPcAAAAAAAA5nABKrQBStQhStRBjvRhrxhhzxiF7zilSpSlrvTlSlFJSSlJrrVqMznNzpYRzhIyczozO95RrGJR7QpSEc5Sl1pSl3pSt3pS155TW/5xrAJyctZylvZyt3py13py155y955y975zG76VzEKWEOaWEQqWltaWlvaW976XG76XO76XO961zAK17CK2lta2tva21xrV7ALV7CLWEALWtrbW1vbW1xrW1zrW9zrXv/72EAL2EIb2MAL2MKb29xr29zsaMAMaMMcaUAMbGxs6UAM6UOc6la87n/9bn/9bv/96UMd6ca97v997v/+fv9+fv/+f3/+/v9+/39+/3//f39/+tSv+1Uv+1Y/+9Y/+9a/+9c//Ge//OhP/OjP/WnP/enP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAAALAAAAAAQABAAAAjDAAEIHEiw4MARK0YoHBGiYQgQHwiqqCKlosUnT6JEPCiFyI8fOW7EgAHjyUaBI6T8qFGChAsTJ040OQkg5Y0NMmj0GOKCwxMMBFPGWCLEiA8aQSg48UAwhJQTV7586eKiBwknQAeGeHLCSpcuXHjQmIC16ZMOTLJgweKiBQQlFsxyaFBhyxEGCx4kiav1SYIDDnRIIGAgwF6CIJIIQKBAggIDBQQcHvihiZMkmJEkQaKZr0AMHzB4sDDagmkLEQyqLhgQADs=" );
    MAP.put( Mime.JS  , "data:image/gif;base64,R0lGODlhEAAQAOYAAEpZfHaEnHmGmihilkV2oFuFp3GUr6O3v7nGx4mPj4mQj0pUM0pTKtXet9XdsdzjtdzjttXcp9zhqtzhrNzhrebqvtfbk9fblNjcltjcmObpvufpttzdidzei9zejefotdXTUdXUWvDww5eXhubjd+HefuHff+HfgeHfg/DvvfDvvs3ITc3ITs3IT83IU+zlaOzlaufidebgdPj1w/DlX/DmYPDmYuzjbOzjbaKffvjqXvjqZaKaUYp7NYp9PYp9P5GFTrKoda6ld6ujeYp7OIp/Uf///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEYALAAAAAAQABAAAAeGgEaCg0FBg4eIRkEiFUKJiEEzGhCOj4KRKg8UlY+RKRsTRZaKPz4fEj0ZAoMIAwWCQA0ORD0YHiirRq2vgkMRPBcdJyQ4q7uHORYcJjE3NsauiCMlMjA1LQG60YgKLzQsAILHhwkLLivhBwYGAwaHCjs6DOFGBwTtiAAhIAmjhwD9/Ak8FAgAOw==" );
    MAP.put( Mime.PDF , "data:image/gif;base64,R0lGODlhEAAQANUAAP///8z//+7u7t3d3f/MzMzMzJnMzLu7u/+ZmaqqqsyZzMyZmZmZzJmZmf9mmf9mZsxmmcxmZv8zZv8zM8wzZswzM5kzM8wAALsAAKoAAJkAAHcAAGYAAFUAABERETMAABEAAP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAUUACEALAAAAAAQABAAAAafwJBwGJpMiEjko5IMRRwPB1TiqEQQj8dFSFl4v4uH4mvxhCgNxqEQCAAmCABgUT6nD4MBQFCpyOlmaAkFeQAPBwQTcxhmFQtrAwJ8AAR9SxMajgWEAQ9XC5ELFRgbFZAFS3KqCxgZHxVsBRGwe5ICdBghFQMBswkBhHkDgCEXkgOEBWubBcQXqm15BQbUzhDX2NnXdRYd3t/gHRwcICFBADs=" );
    MAP.put( Mime.PNG , "data:image/gif;base64,R0lGODlhEAAQAPcAAAAAAAA5nABKrQBStQhStRBjvRhrxhhzxiF7zilSpSlrvTlSlFJSSlJrrVqMznNzpYRzhIyczozO95RrGJR7QpSEc5Sl1pSl3pSt3pS155TW/5xrAJyctZylvZyt3py13py155y955y975zG76VzEKWEOaWEQqWltaWlvaW976XG76XO76XO961zAK17CK2lta2tva21xrV7ALV7CLWEALWtrbW1vbW1xrW1zrW9zrXv/72EAL2EIb2MAL2MKb29xr29zsaMAMaMMcaUAMbGxs6UAM6UOc6la87n/9bn/9bv/96UMd6ca97v997v/+fv9+fv/+f3/+/v9+/39+/3//f39/+tSv+1Uv+1Y/+9Y/+9a/+9c//Ge//OhP/OjP/WnP/enP///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAAAALAAAAAAQABAAAAjDAAEIHEiw4MARK0YoHBGiYQgQHwiqqCKlosUnT6JEPCiFyI8fOW7EgAHjyUaBI6T8qFGChAsTJ040OQkg5Y0NMmj0GOKCwxMMBFPGWCLEiA8aQSg48UAwhJQTV7586eKiBwknQAeGeHLCSpcuXHjQmIC16ZMOTLJgweKiBQQlFsxyaFBhyxEGCx4kiav1SYIDDnRIIGAgwF6CIJIIQKBAggIDBQQcHvihiZMkmJEkQaKZr0AMHzB4sDDagmkLEQyqLhgQADs=" );

    MAP.put( Mime.PPT , "data:image/gif;base64,R0lGODlhEAAQAOYAAFQLDu7AiNhfKa5QLf/v5tl+YowvI/7WwsFhSO6ER/ujZv3MstN3Wa5KNnMaFfy4lP/49cFzWrA4LpE5Mv7h0vV9P+mLXfXJl+ZxNuumd7BeQ+SIZs5hK51cR2wVEoUpH7hZK+6RSb1cQ/2tcZw7K8ttUeNsMP7z7eaOaf///2YSEP7fz8RdLP+8g6NDMfzAobxOLvmVV/3Uv+iTbf/p3pM0J3siG/F1M9F1V++1fveISf61erVKOrZUPv707///99+EY9hnMO2HVO+XZu+XWsVlS/3GquqYcatFMv/l14YqLNZ7W7RRO7VaQgAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAQUAP8ALAAAAAAQABAAAAfvgEczKBtAQEs4JUUIIiJMTEhHLS07IyMKCjExOjoVFTdIMy0ppKUpEBA+PgSgKKMoQAUMJYqNIqwuGzump6mrBDQYJAUjpDIaExMdDwTANCYkSwopMg0+PwATSg8LHhRBNTgKPxonP9gCQhpJSStBBiUxEDUVQigwQkM1Rh4rHB9F5E0IcCGHECFEaqxYcYCFDQQ6fGgIESCDBSEJNLzwcACEDREVTlQLQQSjhAcHUoJw0KNCMxkRatSIgDLlgQEeGtxwloSCT5sHFgxQofMGBhMmggThwIIFCBADhiJB4oIEiRoGPnyw4cCBBxVgAwEAOw==" );
    MAP.put( Mime.PPTX, "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABIAAAASCAYAAABWzo5XAAAACXBIWXMAAAsTAAALEwEAmpwYAAAKT2lDQ1BQaG90b3Nob3AgSUNDIHByb2ZpbGUAAHjanVNnVFPpFj333vRCS4iAlEtvUhUIIFJCi4AUkSYqIQkQSoghodkVUcERRUUEG8igiAOOjoCMFVEsDIoK2AfkIaKOg6OIisr74Xuja9a89+bN/rXXPues852zzwfACAyWSDNRNYAMqUIeEeCDx8TG4eQuQIEKJHAAEAizZCFz/SMBAPh+PDwrIsAHvgABeNMLCADATZvAMByH/w/qQplcAYCEAcB0kThLCIAUAEB6jkKmAEBGAYCdmCZTAKAEAGDLY2LjAFAtAGAnf+bTAICd+Jl7AQBblCEVAaCRACATZYhEAGg7AKzPVopFAFgwABRmS8Q5ANgtADBJV2ZIALC3AMDOEAuyAAgMADBRiIUpAAR7AGDIIyN4AISZABRG8lc88SuuEOcqAAB4mbI8uSQ5RYFbCC1xB1dXLh4ozkkXKxQ2YQJhmkAuwnmZGTKBNA/g88wAAKCRFRHgg/P9eM4Ors7ONo62Dl8t6r8G/yJiYuP+5c+rcEAAAOF0ftH+LC+zGoA7BoBt/qIl7gRoXgugdfeLZrIPQLUAoOnaV/Nw+H48PEWhkLnZ2eXk5NhKxEJbYcpXff5nwl/AV/1s+X48/Pf14L7iJIEyXYFHBPjgwsz0TKUcz5IJhGLc5o9H/LcL//wd0yLESWK5WCoU41EScY5EmozzMqUiiUKSKcUl0v9k4t8s+wM+3zUAsGo+AXuRLahdYwP2SycQWHTA4vcAAPK7b8HUKAgDgGiD4c93/+8//UegJQCAZkmScQAAXkQkLlTKsz/HCAAARKCBKrBBG/TBGCzABhzBBdzBC/xgNoRCJMTCQhBCCmSAHHJgKayCQiiGzbAdKmAv1EAdNMBRaIaTcA4uwlW4Dj1wD/phCJ7BKLyBCQRByAgTYSHaiAFiilgjjggXmYX4IcFIBBKLJCDJiBRRIkuRNUgxUopUIFVIHfI9cgI5h1xGupE7yAAygvyGvEcxlIGyUT3UDLVDuag3GoRGogvQZHQxmo8WoJvQcrQaPYw2oefQq2gP2o8+Q8cwwOgYBzPEbDAuxsNCsTgsCZNjy7EirAyrxhqwVqwDu4n1Y8+xdwQSgUXACTYEd0IgYR5BSFhMWE7YSKggHCQ0EdoJNwkDhFHCJyKTqEu0JroR+cQYYjIxh1hILCPWEo8TLxB7iEPENyQSiUMyJ7mQAkmxpFTSEtJG0m5SI+ksqZs0SBojk8naZGuyBzmULCAryIXkneTD5DPkG+Qh8lsKnWJAcaT4U+IoUspqShnlEOU05QZlmDJBVaOaUt2ooVQRNY9aQq2htlKvUYeoEzR1mjnNgxZJS6WtopXTGmgXaPdpr+h0uhHdlR5Ol9BX0svpR+iX6AP0dwwNhhWDx4hnKBmbGAcYZxl3GK+YTKYZ04sZx1QwNzHrmOeZD5lvVVgqtip8FZHKCpVKlSaVGyovVKmqpqreqgtV81XLVI+pXlN9rkZVM1PjqQnUlqtVqp1Q61MbU2epO6iHqmeob1Q/pH5Z/YkGWcNMw09DpFGgsV/jvMYgC2MZs3gsIWsNq4Z1gTXEJrHN2Xx2KruY/R27iz2qqaE5QzNKM1ezUvOUZj8H45hx+Jx0TgnnKKeX836K3hTvKeIpG6Y0TLkxZVxrqpaXllirSKtRq0frvTau7aedpr1Fu1n7gQ5Bx0onXCdHZ4/OBZ3nU9lT3acKpxZNPTr1ri6qa6UbobtEd79up+6Ynr5egJ5Mb6feeb3n+hx9L/1U/W36p/VHDFgGswwkBtsMzhg8xTVxbzwdL8fb8VFDXcNAQ6VhlWGX4YSRudE8o9VGjUYPjGnGXOMk423GbcajJgYmISZLTepN7ppSTbmmKaY7TDtMx83MzaLN1pk1mz0x1zLnm+eb15vft2BaeFostqi2uGVJsuRaplnutrxuhVo5WaVYVVpds0atna0l1rutu6cRp7lOk06rntZnw7Dxtsm2qbcZsOXYBtuutm22fWFnYhdnt8Wuw+6TvZN9un2N/T0HDYfZDqsdWh1+c7RyFDpWOt6azpzuP33F9JbpL2dYzxDP2DPjthPLKcRpnVOb00dnF2e5c4PziIuJS4LLLpc+Lpsbxt3IveRKdPVxXeF60vWdm7Obwu2o26/uNu5p7ofcn8w0nymeWTNz0MPIQ+BR5dE/C5+VMGvfrH5PQ0+BZ7XnIy9jL5FXrdewt6V3qvdh7xc+9j5yn+M+4zw33jLeWV/MN8C3yLfLT8Nvnl+F30N/I/9k/3r/0QCngCUBZwOJgUGBWwL7+Hp8Ib+OPzrbZfay2e1BjKC5QRVBj4KtguXBrSFoyOyQrSH355jOkc5pDoVQfujW0Adh5mGLw34MJ4WHhVeGP45wiFga0TGXNXfR3ENz30T6RJZE3ptnMU85ry1KNSo+qi5qPNo3ujS6P8YuZlnM1VidWElsSxw5LiquNm5svt/87fOH4p3iC+N7F5gvyF1weaHOwvSFpxapLhIsOpZATIhOOJTwQRAqqBaMJfITdyWOCnnCHcJnIi/RNtGI2ENcKh5O8kgqTXqS7JG8NXkkxTOlLOW5hCepkLxMDUzdmzqeFpp2IG0yPTq9MYOSkZBxQqohTZO2Z+pn5mZ2y6xlhbL+xW6Lty8elQfJa7OQrAVZLQq2QqboVFoo1yoHsmdlV2a/zYnKOZarnivN7cyzytuQN5zvn//tEsIS4ZK2pYZLVy0dWOa9rGo5sjxxedsK4xUFK4ZWBqw8uIq2Km3VT6vtV5eufr0mek1rgV7ByoLBtQFr6wtVCuWFfevc1+1dT1gvWd+1YfqGnRs+FYmKrhTbF5cVf9go3HjlG4dvyr+Z3JS0qavEuWTPZtJm6ebeLZ5bDpaql+aXDm4N2dq0Dd9WtO319kXbL5fNKNu7g7ZDuaO/PLi8ZafJzs07P1SkVPRU+lQ27tLdtWHX+G7R7ht7vPY07NXbW7z3/T7JvttVAVVN1WbVZftJ+7P3P66Jqun4lvttXa1ObXHtxwPSA/0HIw6217nU1R3SPVRSj9Yr60cOxx++/p3vdy0NNg1VjZzG4iNwRHnk6fcJ3/ceDTradox7rOEH0x92HWcdL2pCmvKaRptTmvtbYlu6T8w+0dbq3nr8R9sfD5w0PFl5SvNUyWna6YLTk2fyz4ydlZ19fi753GDborZ752PO32oPb++6EHTh0kX/i+c7vDvOXPK4dPKy2+UTV7hXmq86X23qdOo8/pPTT8e7nLuarrlca7nuer21e2b36RueN87d9L158Rb/1tWeOT3dvfN6b/fF9/XfFt1+cif9zsu72Xcn7q28T7xf9EDtQdlD3YfVP1v+3Njv3H9qwHeg89HcR/cGhYPP/pH1jw9DBY+Zj8uGDYbrnjg+OTniP3L96fynQ89kzyaeF/6i/suuFxYvfvjV69fO0ZjRoZfyl5O/bXyl/erA6xmv28bCxh6+yXgzMV70VvvtwXfcdx3vo98PT+R8IH8o/2j5sfVT0Kf7kxmTk/8EA5jz/GMzLdsAAAAgY0hSTQAAeiUAAICDAAD5/wAAgOkAAHUwAADqYAAAOpgAABdvkl/FRgAAA4JJREFUeNpUlE1sVGUUhp/vu9/c3jszTNuhBVoJ8qMNpCqRRAFXKBESo8aEEDcYdeHWBeqSxLhwoRs3Jq5MjBs3GlcsdKcYk4ppYVSQRFoiFBhmaDs/d+be7+e4mPp3Nic5581z8i7eo74//wUgpiTu0Ha//MZEJT2qo7EEp4I4jziBEBAr4IIKLkAQcAJEwvpGpd1zfRMFZ8rSfmWu7j9K51+qkdQQ78ALKngQj7gCQgDkn64kQByDDQwvNDBG7NG5qfBx+vip1LX/hNYtRAHOoQQoJagoBluAdyCbsBBQsUE7hQwLMTNy4830kZdTu3qF/Kv3USFHp1Vcp4PPFWpqH2OPHSPeOYfgIfgRTECJAkAQzMT4xFMSV5F8iLI5NBuQlFGlbchGl+zSAlnjJyZOv026dw7J803rFlwEAfABUyqVY8kHRPVZ1AvvUJz/kNBpMfbiOcbiMvLlB/QWfqC38C1j09ug20SGg9FRPBBBEWHEuYB3KDS6XMfnELo56cxB9OwBkpWrdBd+xDZv4W9exXTvIKIABRpQMcpPYsRuGvYW6a3jhh6XWVz7FrpUYbByHdsPJNt2Q2UX9s5dyNroSBMlMaQ1RGuMOEEhBG8RNMHUsWsN1j9/FzcsGDQuUtp3mC2nzmL2P4F76DDu0ne460uY5l2iuI9IIkZ8gCJD9e9BqUJQVYpuAfebiBmn8vRpaidfpVytwm8XKI3vwJw4Q9h4HvfHJcK1JdRagRFrke4dxFlU/UFsN8O7CuNn3iN98jm0BvXLN/DZW9DN4fjrqENVosk60ZFn4dAxxu/e1lqKHIoM0jpSqlOsdSk6Bj05Q1SuoYKD5Z+huQLlGmydgWuLcOUiAMQl1NYpNDaA0ujqLOIcLvPYXobPspEw2QKPnoADR6Dbgk/PwdefwHAw2jtH/9fLmGADxGWo7SIKmqnXzuJbq4ztmd8Mq4eZg/BMGbW3ATeXYXo3PHxwBOr3yZcWxWADVHegki1EwOTJ0/xdkmcwyAidDSQkyPR+9Pxxogf2/KtpN/E3b6CVjoKWHNZXIAz5b6k4AVOCYRfWbiOtVcg6/9OE35dQww5GZ8MKG6tw7zKU6zC+E+p7IJkErVHaEE3NQtjMV1oFa0eUYkB09SJGCmVa7UHcW2wh/Q6ENWAZ0stQ2YoyMSgFxozeyDCD+4ugG6D0aDbQ9Grb+WsAUv/TfRdiGuEAAAAASUVORK5CYII=" );
    
    MAP.put( Mime.SQL , "data:image/gif;base64,R0lGODlhEAAQAPcAAAAAAAAAMwAAZgAAmQAAzAAA/zMAADMAMzMAZjMAmTMAzDMA/2YAAGYAM2YAZmYAmWYAzGYA/5kAAJkAM5kAZpkAmZkAzJkA/8wAAMwAM8wAZswAmcwAzMwA//8AAP8AM/8AZv8Amf8AzP8A/wAzAAAzMwAzZgAzmQAzzAAz/zMzADMzMzMzZjMzmTMzzDMz/2YzAGYzM2YzZmYzmWYzzGYz/5kzAJkzM5kzZpkzmZkzzJkz/8wzAMwzM8wzZswzmcwzzMwz//8zAP8zM/8zZv8zmf8zzP8z/wBmAABmMwBmZgBmmQBmzABm/zNmADNmMzNmZjNmmTNmzDNm/2ZmAGZmM2ZmZmZmmWZmzGZm/5lmAJlmM5lmZplmmZlmzJlm/8xmAMxmM8xmZsxmmcxmzMxm//9mAP9mM/9mZv9mmf9mzP9m/wCZAACZMwCZZgCZmQCZzACZ/zOZADOZMzOZZjOZmTOZzDOZ/2aZAGaZM2aZZmaZmWaZzGaZ/5mZAJmZM5mZZpmZmZmZzJmZ/8yZAMyZM8yZZsyZmcyZzMyZ//+ZAP+ZM/+ZZv+Zmf+ZzP+Z/wDMAADMMwDMZgDMmQDMzADM/zPMADPMMzPMZjPMmTPMzDPM/2bMAGbMM2bMZmbMmWbMzGbM/5nMAJnMM5nMZpnMmZnMzJnM/8zMAMzMM8zMZszMmczMzMzM///MAP/MM//MZv/Mmf/MzP/M/wD/AAD/MwD/ZgD/mQD/zAD//zP/ADP/MzP/ZjP/mTP/zDP//2b/AGb/M2b/Zmb/mWb/zGb//5n/AJn/M5n/Zpn/mZn/zJn//8z/AMz/M8z/Zsz/mcz/zMz/////AP//M///Zv//mf//zP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAANcALAAAAAAQABAAAAi1AAcJCiVoEJ8+B/lMUSjl2rVTpwZBjDioj0WEDa8VNNVHEMI7WOxImSIlo6lWexyqVMmKkZgwYUzJ4nONEZowZ66dOXOoESNGN0yxCrTSYbRWYhjt5CEIpUZBRPdgCRRGqRAbQq+sjDbrqCExQoTAEMQqJVRB0XaeEXTGDA8DgkxpNdrqaKtTYHhI0Ci3lam4iP7+DSRWIxQWhxMjXrylqGOVhtDceOwYjRkblItukTA58+OAAAA7" );

    MAP.put( Mime.TEXT, "data:image/gif;base64,R0lGODlhEAAQANUAAPP1+/j5/O7y+vL1+/n6/Pj5+4aavoyfwvD0+5Omxpqsy6Kzz6i406y81d7o+N3n99/o9+Ts+efu+e3y+vP2+9zn99/p+N7o9+Hq+Orw+fb4++bu+fD1+/L2+/X4+/f5+3t0T3pzT3t0UH51TntzT3tzUId6TIN3TX90TpF/SI19Sox9Sp2FRZeCRrCPPqyMQKeKQqeLQqKIQ+DIj7SQPdSyaNSyadq9fNWyaf///wAAAAAAAAAAAAAAAAAAAAAAACH5BAEAADkALAAAAAAQABAAAAZ/wJxwSCwSacgk0iikEZ5QG43p+gQK1sANNy2+PIrEwaCZ3WqvImxA6VAAlDglVpRxGoxFeIyQFVkTEwKBhBMsRS0ZeHpiBhktRSkbEpSVkylFKxGLewYRKkUmGKOkpCZFJxaMYxAXDidFIxUVD7S0tShFJSIgIby9ICUkTMRDQQA7" );

    MAP.put( Mime.XLS , "data:image/gif;base64,R0lGODlhEAAQANUAAClMGZnMmWaZZu/27zSHMd3q3EFlK4WvdbLWqlyARgBmADdcJWSqW9jn2F6TSEaTP////0tyOY2qgXuma06MNB13HOPu4l2aVnGbXpW3ivb59jBSHXu7cnWyYkiHQVZ6QEZsMmGHTGisY4XBe2qWV9/s31aMPC9RK3agZYq1e6fQnZC8gebw5maOVFmCVmuta1mkVEyWSnS2bP///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAUUADMALAAAAAAQABAAAAa6wFXqcJigMKRWKJT4fCKRFAKhUgWuGWFxMokiIGCwZqwZmFGRwzdMLpsH6IkK8hKJLmOX3sXCgOQQMQgcHA4uLzAECn0gKAFgDyoyDB6JiywkBhgBZAQBDDAgixYWmSQZZIkrDjELAgIlDgstK2MwHTAVI6wLEgWyISkaMCfFJxUyvL8LCQdvLNASxicNJhsfE2bQLBYl3t7V19nb3d8N5xQAEUZHSCQO8CYmFOlQESD4BvoL/Bv+AEEAADs=" );
    MAP.put( Mime.XLSX, "data:image/png;base64,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" );
    
    MAP.put( Mime.XML , "data:image/gif;base64,R0lGODlhEAAQAOYAAPj5/PX3/ImUrIWTroaTroyWqvb4/Pn6/IWUr4WUroaUro2XquLq+OXs+fD0+4iWrOTs+eLq9+fu+erw+u3y+vP2+4+ZqN/p+OHq+ODp9+Lr+OTs+Ofu+Orw+fD0+vf5/JCap9/p9+Lr9+3y+fX4/PP2+uHr9+Tt+PL2+ylIa2uBmZScpYaYq/X4+/f5+5WdpJieopqgoJ6in5+jnKKkm6WnmaiomK2rlbCtkquplrWwj7Kukry1irmyjLawjr+2iODIj9SyaNSyadq9fNWyaf///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEUALAAAAAAQABAAAAeRgEWCg4SFhD+IiYiGgjwHj5BCP4w9kJBDRJOFOgCdLgAfQENBPoU4LS0pKgEqKSQGO4U5FSUsKSgpLBUVN4U1Dg4euSkewTaFMxTKtiwjyjSFMR3TwxPTMoUvEhK2HLnbMIUgGw2qEK0nGyuFCwwMGBoRIhgmGBaFDyH6GRkX+wWFFBAYoCCBAgQCCQhgxHBQIAA7" );
    MAP.put( Mime.XSD , "data:image/gif;base64,R0lGODlhEAAQAOYAAPj5/PT2+/H0++7y+vH0+vX3+/n6/OLq+Oju+ay81eDp+OTs+eXs+Ofu+e3y+vP2+93o+N/p+N7o9+Dp9+Lr+OXt+ejv+evx+urw+fD0+vf5/Pb4+93o99/p9+Hr+OTt+eXt+Ovx+e7z+muQvN/q+N/q9+fv+erx+fD1+vP3+/f5+yhilnt0T3pzT3t0UH51TntzT3tzUId6TIN3TX90TpF/SI19Sox9Sp2FRZeCRrCPPqyMQKeKQqeLQqKIQ+DIj7SQPdSyaNSyadq9fNWyaf///wAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACH5BAEAAEUALAAAAAAQABAAAAebgEWCg4SFg0CIiYiGgkAGj5BCQIw6ABqWGipDRJOFOxsFCSujP0NBO4U8Dw8rIwEjKw8pPYU+GQKtAgQEGSg+hTgiDrCtAwMiOIU5IRcnCbAjGBc5hTUNJg0ICLAIFjWFNyAVrQuwDB82hTIHFBSjKwkeBzKFMx0KHRElEyQTCjOFXkjgwGEgBIIcaBSK4YJFi4YOWcSAwagioUAAOw==" );
    MAP.put( Mime.XSL , "data:image/gif;base64,R0lGODlhEAAQAPcAACkQWjEYWjEhWjkhWjkhezk5MUIhY0Ihc0IpWkIpc0JKOUJKWkohnEope0o5c0pCOUpKSkpKUkpKWkpSMUpSWkpSY1Ixe1JCUlJKWlJSQlJSSlJSUlJSWlJaQlJaSlJaWlJaY1JjMVJjOVJjWlo5jFo5lFpaQlpaUlpaWlpaa1pac1pjQlpjSlpjUmMxtWNSa2NSc2NaWmNjUmNjWmNjY2NrY2tKnGtKtWtShGtalGtrY3NalHNanHNre3tStXtatXtjrYRS54RjtYRrrYRzpYRzvYxztZSEzpxzzpx7vZyEvaV71qWExqWEzqWE1q1757WE/72U/8ac/+/n/+/39/fn//fv//f35/f37/f39/f/5//e///n///v///37//3////7///9////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////yH5BAEAAGIALAAAAAAQABAAAAjhAMWIocEBw4kMJmQ8mBFDoEMxKLII9AKGohgNDR/WAINFTEcwAlGAUfCwAEiJXyROoXEFDAeHJ7CAsfLlixibEU604KBCoIabNYPWvGnlpZgeU4A4qNnlSxcBR6qI0eFwSAIuX3BYsOGUQJEvFwQaGVDzhosoJHZ06TLASBcxOxI07cIgCBQfBrisBWBE4JADX7YgeSIFSZIuWxoo4eIQSICmSpw0WUsCCJcXAmF0IYLgSxW9XRDkqIlBYMOaU4TaFGiUxZWbAkHavKKFCgiBE1ZwSKFBQ4cWEkCICHGiQkAAADs=" );

    MAP.put( Mime.ZIP , "data:image/gif;base64,R0lGODlhEAAQALMAAP//////zPj4+P//mfHx8f/MmczMZsDAwKGhFIaGhhQUFAAAAAAAAAAAAAAAAAAAACH5BAQUAP8ALAAAAAAQABAAAARZEMhJ60QYW4rISMOAbABiGMmxnFkmIUGQCEtsB4OhlHi6hMBBQccbqA7AgjC3gw0WiV9SSHQeUkulsoqDSrXUJu6KBBeG4oLXfK6e3nD4DqCo2+93kn5viQAAOw==" );
    
  }

} // end of enum Mime
