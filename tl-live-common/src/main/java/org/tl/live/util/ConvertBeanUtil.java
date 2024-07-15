package org.tl.live.util;

import org.springframework.beans.BeanUtils;

public class ConvertBeanUtil {
    public static <T> T convert(Object source, Class<T> targetClass) {
        if(source == null) {
            return null;
        }
        T targetObj = null;
        try {
            targetObj = targetClass.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(source, targetObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return targetObj;
    }
}
