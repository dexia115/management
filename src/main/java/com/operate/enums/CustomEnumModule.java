package com.operate.enums;

import com.fasterxml.jackson.databind.module.SimpleModule;

public class CustomEnumModule extends SimpleModule{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 213930060937637671L;

	/**
     * @param prop 属性名
     */
    public CustomEnumModule(String prop){

        addDeserializer(Enum.class, new CustomEnumDeserializer(prop));
//        addSerializer(Enum.class, new CustomEnumSerializer(prop));
    }

}
