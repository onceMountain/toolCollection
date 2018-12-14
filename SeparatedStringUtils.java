package com.cy.commander.common.util;

import java.util.*;



public class SeparatedStringUtils {
    public static String defultSeparatingStr = ",";

    public static boolean isEmpty(String separatedString) {
        return isEmpty(separatedString, defultSeparatingStr);
    }

    public static boolean isEmpty(String separatedString, String separatingStr) {
        return DataUtils.isEmpty(separatedString) || DataUtils.isEmpty(separatedString.replaceAll(separatingStr, ""));
    }

    public static boolean isNotEmpty(String separatedString) {
        return !isEmpty(separatedString);
    }

    public static boolean isNotEmpty(String separatedString, String separatingStr) {
        return !isEmpty(separatedString, separatingStr);
    }

    public static long sizeOf(String separatedString) {
        return sizeOf(separatedString, defultSeparatingStr);
    }

    public static long sizeOf(String separatedString, String separatingStr) {
        return toList(separatedString, separatingStr).size();
    }

    public static String getFromArray(Object[] array) {
        return getFromArray(array, defultSeparatingStr);
    }

    public static String getFromArray(Object[] array, String separatingStr) {
        return getFromCollection(new ArrayList<>(Arrays.asList(array)), separatingStr);
    }

    public static String wrap(String separatedString) {
        return wrap(separatedString, defultSeparatingStr);
    }

    public static String wrap(String separatedString, String separatingStr) {
        if (isEmpty(separatedString)) {
            return separatingStr + separatingStr;
        }
        if (!separatedString.startsWith(separatingStr) && !separatingStr.equals(separatedString)) {
            separatedString = separatingStr + separatedString;
        }
        if (!separatedString.endsWith(separatingStr)) {
            separatedString = separatedString + separatingStr;
        }
        return separatedString;
    }

    public static String unWrap(String separatedString) {
        return unWrap(separatedString, defultSeparatingStr);
    }

    public static String unWrap(String separatedString, String separatingStr) {
        if (isEmpty(separatedString)) {
            return "";
        }
        if (separatedString.startsWith(separatingStr)) {
            separatedString = separatedString.replaceFirst(separatingStr, "");
        }
        if (separatedString.endsWith(separatingStr)) {
            separatedString = separatedString.substring(0, separatedString.length() - separatingStr.length());
        }
        return separatedString;
    }

    public static String getFromSet(Set set) {
        return getFromCollection(set);
    }

    public static String getFromSet(Set set, String separatingStr) {
        return getFromCollection(set, separatingStr);
    }

    public static String getFromList(List list) {
        return getFromCollection(list);
    }

    public static String getFromList(List list, String separatingStr) {
        return getFromCollection(list, separatingStr);
    }

    public static String getFromCollection(Collection collection) {
        return getFromCollection(collection, defultSeparatingStr);
    }

    public static String getFromCollection(Collection collection, String separatingStr) {
        if (DataUtils.isEmpty(collection)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();

        for (Object object : collection) {
            sb.append(object.toString()).append(separatingStr);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public static String[] toArray(String separatedString) {
        return toArray(separatedString, defultSeparatingStr);
    }

    public static String[] toArray(String separatedString, String separatingStr) {
        return toList(separatedString, separatingStr).toArray(new String[(int)sizeOf(separatedString, separatingStr)]);
    }

    public static Set<String> toSet(String separatedString) {
        return new LinkedHashSet<>(toList(separatedString));
    }

    public static Set<String> toSet(String separatedString, String separatingStr) {
        return new LinkedHashSet<>(toList(separatedString, separatingStr));
    }

    public static List<String> toList(String separatedString) {
        return toList(separatedString, defultSeparatingStr);
    }

    public static List<String> toList(String separatedString, String separatingStr) {
        if (isEmpty(separatedString)) {
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<>();
        for (String str : separatedString.split(separatingStr)) {
            if (DataUtils.isNotEmpty(str)) {
                list.add(str);
            }
        }
        return list;
    }

    public static boolean contains(String separatedString, String str) {
        return contains(separatedString, str, defultSeparatingStr);
    }

    public static boolean contains(String separatedString, String str, String separatingStr) {
        if (isEmpty(separatedString)) {
            return isEmpty(str);
        }
        str = wrap(str, separatingStr);
        separatedString = wrap(separatedString, separatingStr);
        return separatedString.contains(str);
    }

    public static boolean startsWith(String separatedString, String str) {
        return startsWith(separatedString, str, defultSeparatingStr);
    }

    public static boolean startsWith(String separatedString, String str, String separatingStr) {
        if (isEmpty(separatedString)) {
            return isEmpty(str);
        }
        str = wrap(str, separatingStr);
        separatedString = wrap(separatedString, separatingStr);
        return separatedString.startsWith(str);
    }

    public static boolean endsWith(String separatedString, String str) {
        return endsWith(separatedString, str, defultSeparatingStr);
    }

    public static boolean endsWith(String separatedString, String str, String separatingStr) {
        if (isEmpty(separatedString)) {
            return isEmpty(str);
        }
        str = wrap(str, separatingStr);
        separatedString = wrap(separatedString, separatingStr);
        return separatedString.endsWith(str);
    }

    public static String intersect(String string1, String string2) {
        if (isEmpty(string1) || isEmpty((string2))) {
            return "";
        }
        Set<String> set1 = SeparatedStringUtils.toSet(string1);
        Set<String> set2 = SeparatedStringUtils.toSet(string2);
        Set<String> set3 = new LinkedHashSet<>();
        //交集
        set3.addAll(set1);
        set3.retainAll(set2);

        return SeparatedStringUtils.getFromSet(set3);
    }

    public static String intersect(String string1, String string2, String separatingStr) {
        if (isEmpty(string1, separatingStr) || isEmpty(string2, separatingStr)) {
            return "";
        }
        Set<String> set1 = SeparatedStringUtils.toSet(string1, separatingStr);
        Set<String> set2 = SeparatedStringUtils.toSet(string2, separatingStr);
        Set<String> set3 = new LinkedHashSet<>();
        //交集
        set3.addAll(set1);
        set3.retainAll(set2);

        return SeparatedStringUtils.getFromSet(set3, separatingStr);
    }

    public static String union(String string1, String string2) {
        Set<String> set1 = SeparatedStringUtils.toSet(string1);
        Set<String> set2 = SeparatedStringUtils.toSet(string2);
        Set<String> set3 = new LinkedHashSet<>();
        //并集
        set3.addAll(set1);
        set3.addAll(set2);

        return SeparatedStringUtils.getFromSet(set3);
    }

    public static String union(String string1, String string2, String separatingStr) {
        Set<String> set1 = SeparatedStringUtils.toSet(string1, separatingStr);
        Set<String> set2 = SeparatedStringUtils.toSet(string2, separatingStr);
        Set<String> set3 = new LinkedHashSet<>();
        //并集
        set3.addAll(set1);
        set3.addAll(set2);

        return SeparatedStringUtils.getFromSet(set3, separatingStr);
    }

    public static String diff(String string1, String string2) {
        Set<String> set1 = SeparatedStringUtils.toSet(string1);
        Set<String> set2 = SeparatedStringUtils.toSet(string2);
        Set<String> set3 = new LinkedHashSet<>();
        //差集
        set3.addAll(set1);
        set3.removeAll(set2);

        return SeparatedStringUtils.getFromSet(set3);
    }

    public static String diff(String string1, String string2, String separatingStr) {
        Set<String> set1 = SeparatedStringUtils.toSet(string1, separatingStr);
        Set<String> set2 = SeparatedStringUtils.toSet(string2, separatingStr);
        Set<String> set3 = new LinkedHashSet<>();
        //差集
        set3.addAll(set1);
        set3.removeAll(set2);

        return SeparatedStringUtils.getFromSet(set3, separatingStr);
    }
}