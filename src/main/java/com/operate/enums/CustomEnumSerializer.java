package com.operate.enums;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import static org.apache.commons.beanutils.PropertyUtils.getPropertyDescriptor;

/**
 * 自定义枚举序列化器，查找特定属性并进行序列化，如果无法找到，则序列化为枚举名。
 *
 * @author liuxiaodong
 * @since 2018-02-13
 */
@SuppressWarnings({"rawtypes"})
public class CustomEnumSerializer extends JsonSerializer<Enum> {

    private String prop;

    /**
     * @param prop 属性名
     */
    public CustomEnumSerializer(String prop) {

        this.prop = prop;
    }

    @Override
    public void serialize(Enum value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value == null) {
            gen.writeNull();
            return;
        }
        try {
            PropertyDescriptor pd = getPropertyDescriptor(value, prop);
            if (pd == null || pd.getReadMethod() == null) {
                gen.writeString(value.name());
                return;
            }
            Method m = pd.getReadMethod();
            m.setAccessible(true);
            gen.writeObject(m.invoke(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
