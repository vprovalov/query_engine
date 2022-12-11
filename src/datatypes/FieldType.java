package datatypes;

import java.util.Date;

public enum FieldType {
    BooleanType(Boolean.class),
    Int32Type(Integer.class),
    Int64Type(Long.class),
    FloatType(Float.class),
    DoubleType(Double.class),
    StringType(String.class),
    DateTime(Date.class);

    public final Class clazz;

    FieldType(Class clazz) {
        this.clazz = clazz;
    }
}
