package com.ist.common.es.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CollectionUtils {
    private CollectionUtils() {
    }

    /**
     * 求两个集合中的交集
     * 
     * @param sourceSet
     *            源集合
     * @param targetSet
     *            目标集合
     * @return <T> Set<T> 返回新的交集集合
     */
    public static <T> Set<T> intersect(Set<T> sourceSet, Set<T> targetSet) {
        Set<T> set = new HashSet<T>();
        int sourceSetSize = sourceSet.size();
        int targetSetSize = targetSet.size();
        Set<T> minSet = null;
        Set<T> maxSet = null;
        if (sourceSetSize <= targetSetSize) {
            minSet = sourceSet;
            maxSet = targetSet;
        } else {
            minSet = targetSet;
            maxSet = sourceSet;

        }

        for (T t : minSet) {
            if (maxSet.contains(t)) {
                set.add(t);
            }
        }
        return set;
    }

    /**
     * 按key所对应的值进行分组 并将其值作为新map的key key2所对应的值作为list中的value
     * 
     * @param result
     * @param key
     * @param key2
     * @return Map<Object, List<String>>
     */
    public static Map<String, List<String>> classify(List<Map<String, Object>> result, String key, String key2) {
        Map<String, List<String>> map = new HashMap<String, List<String>>();
        List<String> values = null;
        for (Map<String, Object> record : result) {
            String mapKey = String.valueOf(record.get(key));
            String mapkey2 = String.valueOf(record.get(key2));
            if (!map.containsKey(mapKey)) {
                values = new LinkedList<String>();
                values.add(mapkey2);
            } else {
                map.get(mapKey).add(mapkey2);
            }
            map.put(mapKey, values);
        }
        return map;
    }

    /**
     * 带顺序去除重复元素
     * 
     * @param list
     * @return
     */
    public static <T> List<T> removeDuplicateWithOrder(List<T> list) {
        Set<T> hashSet = new HashSet<T>();
        List<T> newlist = new ArrayList<T>();
        for (Iterator<T> iterator = list.iterator(); iterator.hasNext();) {
            T element = iterator.next();
            if (hashSet.add(element)) {
                newlist.add(element);
            }
        }
        list.clear();
        list.addAll(newlist);
        return list;
    }
}
