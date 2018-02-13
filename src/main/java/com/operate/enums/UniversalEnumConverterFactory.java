package com.operate.enums;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

@Component
@SuppressWarnings({ "rawtypes", "unchecked" })
public class UniversalEnumConverterFactory implements ConverterFactory<String, NamedEnum> {


	@Override
	public <T extends NamedEnum> Converter<String, T> getConverter(Class<T> targetType) {
		return new IntegerToEnum(targetType);
	}

	class IntegerToEnum<T extends NamedEnum> implements Converter<String, T> {
		
		private final Class<T> enumType;

		public IntegerToEnum(Class<T> enumType) {
			this.enumType = enumType;
		}

		@Override
		public T convert(String value) {
			T[] list = enumType.getEnumConstants();
			for(T e : list) {
				Integer v = Integer.parseInt(value);
				if(e.getValue() == v) {
					return e;
				}
			}
			return null;
		}
	}

}