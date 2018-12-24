import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.*;


public class BeanUtils {

    public static void copyProperties(Object source, Object target) {
        if (source == null) {
            throw new RuntimeException("copyProperties fail,source object is null!");
        }
        org.springframework.beans.BeanUtils.copyProperties(source, target);
    }

    private static ObjectMapper objectMapper = new ObjectMapper();

    public static Object clone(Object object) {
        if (object == null) {
            throw new RuntimeException("clone fail,source object is null!");
        }
        try {
            return objectMapper.readValue(objectMapper.writeValueAsString(object), object.getClass());
        } catch (Exception e) {
            throw new RuntimeException("clone fail!", e);
        }
    }

    public static LinkedHashMap<String, Object> toMap(Object object) {
        try {
            if (object == null) {
                throw new RuntimeException("toMap fail,source object is null!");
            }
            return objectMapper.readValue(objectMapper.writeValueAsString(object),
                objectMapper.getTypeFactory().constructMapType(LinkedHashMap.class, String.class, Object.class));
        } catch (Exception e) {
            throw new RuntimeException("toMap fail!", e);
        }
    }

    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        for (Field field : object.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                if (field.get(object) != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    /**
     * http://www.css88.com/doc/underscore/
     * https://github.com/javadev/underscore-java
     */
    public static <T> List<T> pluck(Collection objectCollection, String fieldName, Class<T> tClass) {
        return pluck(objectCollection, fieldName);
    }

    public static List pluck(Collection objectCollection, String fieldName) {
        if (objectCollection == null || objectCollection.isEmpty()) {
            return new ArrayList<>(0);
        }
        List list = new ArrayList();
        try {
            for (Object object : objectCollection) {
                Field f = object.getClass().getDeclaredField(fieldName);
                f.setAccessible(true);

                if (f.get(object) != null) {
                    list.add(f.get(object));
                }

            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(
                "pluck fail!" + objectCollection.iterator().next().getClass().getSimpleName() + "类不存在:" + fieldName
                    + "属性", e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(
                "pluck fail!" + objectCollection.iterator().next().getClass().getSimpleName() + "类属性:" + fieldName
                    + "值获取失败", e);
        }
        return list;
    }

    public static void setEemptyStrFieldToNull(Object object) {
        if (object != null) {
            Field[] fields = object.getClass().getDeclaredFields();
            for (int i = 0; i < fields.length; i++) {
                Field field = fields[i];
                field.setAccessible(true);
                if (DataUtils.equals(field.getType(), String.class)) {
                    try {
                        if (field.get(object) != null && DataUtils.equals(field.get(object).toString(), "")) {
                            field.set(object, null);
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
