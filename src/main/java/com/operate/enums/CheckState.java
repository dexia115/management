package com.operate.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 验券状态:1.已锁定,2.已验证,3.已作废
 * <p>Created by taoping on 2017/11/1.</p>
 *
 * @author taoping
 */
public enum CheckState implements NamedEnum {
    Locked(1, "已锁定"), Checked(2, "已验证"), Obsolete(3, "已作废");

    private Integer value;
    
    private String name;

    CheckState(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getValue() {
        return value;
    }
    
//    @JsonCreator
//    public static CheckState forValue(Integer value) {
//    	CheckState[] array = CheckState.values();
//        for (CheckState obj : array) {
//            if (obj.getValue().equals(value)) {
//                return obj;
//            }
//        }
//        return null;
//    }
}
