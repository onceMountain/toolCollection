import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;


public class SQLUtils {

    //以下修改自Spring用于将对象的驼峰属性名转换为数据库表中对应的下划线字段名
    public static String getUnderscoreName(String name) {
        if (DataUtils.isEmpty(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.substring(0, 1).toLowerCase(Locale.US));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = s.toLowerCase(Locale.US);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

    //以下改自mybatis.generator用于将数据库表中对应的下划线字段名转换为对象的驼峰属性名
    public static String getCamelCaseName(String name) {
        if (DataUtils.isEmpty(name)) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        boolean nextUpperCase = false;
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);

            switch (c) {
                case '_':
                case '-':
                case '@':
                case '$':
                case '#':
                case ' ':
                case '/':
                case '&':
                    if (sb.length() > 0) {
                        nextUpperCase = true;
                    }
                    break;

                default:
                    if (nextUpperCase) {
                        sb.append(Character.toUpperCase(c));
                        nextUpperCase = false;
                    } else {
                        sb.append(Character.toLowerCase(c));
                    }
                    break;
            }
        }
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    /**
     * 根据有规则的字符串生成排序sql
     * @param clazz
     * @param sort  （"属性名@顺序,属性名@顺序,属性名@顺序" 顺序参数有ASC和DESC 不写默认ASC，兼容大小写）
     * @return
     */
    public static String generateOrderSql(Class clazz, String sort) {
        if (clazz == null) {
            throw new RuntimeException("类型参数clazz不可为空");
        }
        if (SeparatedStringUtils.isEmpty(sort)) {
            return "";
        }
        StringBuilder orderSql = new StringBuilder();
        String fieldName;
        String defaultOrder;
        for (String s : SeparatedStringUtils.toList(sort)) {
            String[] t = s.split("@");
            if (t.length > 0) {
                fieldName = t[0];
                defaultOrder = " ASC ";
                if (t.length > 1) {
                    if (DataUtils.equals(t[1].trim().toUpperCase(), "DESC")) {
                        defaultOrder = " DESC ";
                    } else if (!DataUtils.equals(t[1].trim().toUpperCase(), "ASC")) {
                        throw new RuntimeException("传入了错误的排序参数 " + t[1]);
                    }
                }
                for (Field field : clazz.getDeclaredFields()) {
                    field.setAccessible(true);
                    if (field.getName().equals(fieldName)) {
                        orderSql.append(" `" + getUnderscoreName(field.getName()) + "` " + defaultOrder + " ,");
                    }
                }
            }
        }
        if (orderSql.length() > 0)//有值
        {
            orderSql.deleteCharAt(orderSql.length() - 1);//删除最后的,
        }
        return orderSql.toString();
    }

    /**
     * 根据有规则的字符串生成in条件排序sql
     * @param clazz
     * @param fieldName            属性名
     * @param filterFieldValueList 属性值字符串或数值list
     * @param order                ASC和DESC 不写默认ASC，兼容大小写
     * @return
     */
    public static String generateMultiInOrderSql(Class clazz, String fieldName, List filterFieldValueList,
        String order) {
        if (clazz == null) {
            throw new RuntimeException("类型参数clazz不可为空");
        }
        if (DataUtils.isEmpty(filterFieldValueList) || DataUtils.isEmpty(order)) {
            return "";
        }

        try {
            clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(clazz.getSimpleName() + "类不存在: " + fieldName + " 属性", e);
        }

        StringBuilder multiInOrderSql = new StringBuilder();

        for (Object fieldValue : filterFieldValueList) {
            //加单引号可兼容数字字符串
            multiInOrderSql.append(", '" + fieldValue.toString().replace("'", "''") + "'");
        }
        if (multiInOrderSql.length() > 0) {
            multiInOrderSql.append(") ");
        }

        multiInOrderSql.insert(0, " field(`" + getUnderscoreName(fieldName) + "` ");

        String defaultOrder = " ASC ";
        if (DataUtils.equals(order.trim().toUpperCase(), "DESC")) {
            defaultOrder = " DESC ";
        } else if (!DataUtils.equals(order.trim().toUpperCase(), "ASC")) {
            throw new RuntimeException("传入了错误的排序参数 " + order);
        }
        multiInOrderSql.append(defaultOrder);
        return multiInOrderSql.toString();
    }
}

