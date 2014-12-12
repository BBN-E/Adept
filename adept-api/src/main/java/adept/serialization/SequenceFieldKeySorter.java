//package adept.serialization;
//
//import java.lang.annotation.Annotation;
//import java.util.Map;
//
//import com.thoughtworks.xstream.converters.reflection.FieldKeySorter;
//
//@Retention(RetentionPolicy.RUNTIME)
//@Target(ElementType.TYPE)
//public @interface XMLSequence {
//    String[] value();
//} 
//
//@XMLSequence({
//    "accountIds",
//    "addresses",
//    "birthDate",
//    "contact",
//    "name",
//    "status",
//})
//
//
//public class SequenceFieldKeySorter implements FieldKeySorter {
//    @Override
//    public Map sort(final Class type, final Map keyedByFieldKey) {
//        Annotation sequence = type.getAnnotation(XMLSequence.class);
//        if (sequence != null) {
//            final String[] fieldsOrder = ((XMLSequence) sequence).value();
//            Map result = new OrderRetainingMap();
//            Set<Map.Entry<FieldKey, Field>> fields = keyedByFieldKey.entrySet();
//            for (String fieldName : fieldsOrder) {
//                if (fieldName != null) {
//                    for (Map.Entry<FieldKey, Field> fieldEntry : fields) {
//                        if
//(fieldName.equals(fieldEntry.getKey().getFieldName())) {
//                            result.put(fieldEntry.getKey(),
//fieldEntry.getValue());
//                        }
//                    }
//                }
//            }
//            return result;
//        } else {
//            return keyedByFieldKey;
//        }
//
//    }
//} 