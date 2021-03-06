package com.operate.enums;

import lombok.extern.slf4j.Slf4j;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Optional;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptor;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * 枚举工具类
 *
 * @author liuxiaodong
 * @since 2018-02-13
 */
@Slf4j
public class Enums {


    /**
     * 匹配枚举集合中指定的Bean属性, 返回该枚举值
     *
     * @param prop      Bean属性名
     * @param propValue Bean属性值
     */
    public static <T extends Enum> Optional<T> getEnum(Class<T> enumCls, String prop, String propValue) {

        T[] enumValues = enumCls.getEnumConstants();
        if (isEmpty(enumValues)) {
            return empty();
        }
        try {
            for (T enumValue : enumValues) {
                PropertyDescriptor pd = getPropertyDescriptor(enumValue, prop);
                if (pd == null || pd.getReadMethod() == null) {
                    continue;
                }
                Method m = pd.getReadMethod();
                m.setAccessible(true);
                if (propValue.equals(m.invoke(enumValue).toString())) {
                    return of(enumValue);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return empty();
    }
}
