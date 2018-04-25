package com.iflytek.util;

import java.util.*;

/**
 * ClassName: CollectionUtil <br/>
 * Function: TODO ADD FUNCTION 集合工具类
 *
 * @author yusu4
 * @since JDK 1.7+
 */
public class CollectionUtil {

    /**
     * isNotEmpty:(检查集合是否为空，不为空返回true)
     *
     * @param <T>
     * @param c
     * @return
     * @author yolanda0608
     */
    public static <T> boolean isNotEmpty(Collection<T> c) {
        if (c != null && c.size() != 0) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * checkEmpty:(检查集合是否为空，为空返回true)
     *
     * @param <T>
     * @param c
     * @return
     * @author yolanda0608
     */
    public static <T> boolean isEmpty(Collection<T> c) {
        if (c == null || c.size() == 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 该sublist增加了下标检测，避免报错
     *
     * @param source List
     * @param from   from index
     * @param to     to index
     * @param <T>
     * @return
     */
    public static <T> List<T> subList(List<T> source, int from, int to) {
        if (CollectionUtil.isEmpty(source)) {
            return null;
        }
        int size = source.size();
        if (to > size) {
            return source.subList(from, size);
        } else {
            return source.subList(from, to);
        }
    }

    /**
     * 将数组转换为List,如果数组为空则返回一个空list
     *
     * @param a
     * @param <T>
     * @return
     */
    public static <T> List<T> asList(T... a) {
        if (null != a) {
            return Arrays.asList(a);
        } else {
            return new ArrayList<>(0);
        }
    }

    /**
     * 较差合并两个list的各项值交叉合并,合并有先后顺序，对于集合数据不为空的情况，
     * result1的值第一个值放最前面
     *
     * @param result1
     * @param result2
     * @return
     */
    public static <T> List<T> mergeAndSwap(List<T> result1, List<T> result2) {

        //两个数据都为空，则返回空的结合
        if (CollectionUtil.isEmpty(result1) && CollectionUtil.isEmpty(result2)) {
            return new ArrayList<>(0);
        }

        //近使用result2的结果，无需合并
        if (CollectionUtil.isEmpty(result1) && CollectionUtil.isNotEmpty(result2)) {
            return result2;
        }

        //仅仅使用result1的结果，无需合并
        if (CollectionUtil.isNotEmpty(result1) && CollectionUtil.isEmpty(result2)) {
            return result1;
        }

        int a = result1.size();
        int b = result2.size();
        int size = a + b;
        //两个list的值交叉合并算法(暂时未经过测试)
        List<T> finalResult = new ArrayList<>(size);
        if (a >= b) {
            for (int i = 0; i < size; i++) {
                if (i > (b << 1) - 1) {
                    finalResult.add(result1.get(i - b));
                } else {
                    if ((i & 1) == 0 && i >> 1 < a) {
                        finalResult.add(result1.get(i >> 1));
                    }
                }
                if ((i & 1) == 1 && (i - 1) >> 1 < b) {
                    finalResult.add(result2.get((i - 1) >> 1));
                }
            }
        } else {
            for (int i = 0; i < size; i++) {
                if ((i & 1) == 0 && (i >> 1) < a) {
                    finalResult.add(result1.get(i >> 1));
                }
                if ((i & 1) == 1 && (i >> 1) < a - 1) {
                    finalResult.add(result2.get((i - 1) >> 1));
                }
                if (i >= (a << 1) - 1) {
                    finalResult.add(result2.get(i - a));
                }
            }
        }
        return finalResult;
    }

}
