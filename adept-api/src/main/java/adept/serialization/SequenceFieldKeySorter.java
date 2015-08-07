/*
* ------
* Adept
* -----
* Copyright (C) 2014 Raytheon BBN Technologies Corp.
* -----
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
* -------
*/

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