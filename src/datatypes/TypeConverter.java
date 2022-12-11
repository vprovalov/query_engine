package datatypes;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TypeConverter {
    private final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
    private final static ParsePosition parsePosition= new ParsePosition(0);
    public static Object toObject( Class clazz, String value ) {
        if( Boolean.class == clazz ) return Boolean.parseBoolean( value );
        if( Byte.class == clazz || byte.class == clazz) return Byte.parseByte( value );
        if( Short.class == clazz || short.class == clazz) return Short.parseShort( value );
        if( Integer.class == clazz ) return Integer.parseInt( value );
        if( Long.class == clazz ) return Long.parseLong( value );
        if( Float.class == clazz ) return Float.parseFloat( value );
        if( Double.class == clazz ) return Double.parseDouble( value );
        if( int.class == clazz ) return Integer.parseUnsignedInt( value );
        if( long.class == clazz ) return Long.parseUnsignedLong( value );
        if ( Date.class == clazz ) return simpleDateFormat.parse( value, parsePosition );
        return value;
    }
}
