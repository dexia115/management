package com.operate.tools;

import java.util.Date;
import org.springframework.util.Assert;
import com.fasterxml.jackson.annotation.JsonCreator;

public class PropertyFilter {

	public static final String OR_SEPARATOR = "_OR_";

	public enum MatchType {
		AND(1,"and"), OR(2,"or"), NE(3,"不等于"), EQ(4,"等于"),LIKE(5,"包含"),
		LT(6,"小于"), GT(7,"大于"), LE(8,"小于等于"), GE(9,"大于等于"), BETWEEN(10,"之间"),
		IN(11,"in"),NOTIN(12,"not in"),NULL(13,"null"), NOTNULL(14,"not null"),LIKESTART(15,"前模糊") // like 'value%'		
		;
		
		private Integer value;
		
		private String describle;
		
		private MatchType(Integer value,String describle){
			this.value = value;
			this.describle = describle;
		}
		
		@JsonCreator
	    public static MatchType forValue(Integer value) {
			MatchType[] array = MatchType.values();
	        for (MatchType obj : array) {
	            if (obj.getValue().equals(value)) {
	                return obj;
	            }
	        }
	        return null;
	    }

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public String getDescrible() {
			return describle;
		}

		public void setDescrible(String describle) {
			this.describle = describle;
		}
	}

	public enum Type {
		INTEGER, LONG, FLOAT, STRING, DATE, DOUBLE, BOOLEAN
	}

	public enum PropertyType {
		S(String.class), I(Integer.class), L(Long.class), N(Double.class), D(Date.class), B(Boolean.class);

		private Class<?> clazz;

		private PropertyType(Class<?> clazz) {
			this.clazz = clazz;
		}

		public Class<?> getValue() {
			return clazz;
		}
	}

	private MatchType matchType = null;
	private Object matchValue = null;

	private Class<?> propertyClass = null;
	private String[] propertyNames = null;

	public PropertyFilter() {
	}

	public Class<?> getPropertyClass() {
		return propertyClass;
	}

	public MatchType getMatchType() {
		return matchType;
	}

	public Object getMatchValue() {
		return matchValue;
	}

	public String[] getPropertyNames() {
		return propertyNames;
	}

	public String getPropertyName() {
		Assert.isTrue(propertyNames.length == 1, "There are not only one property in this filter.");
		return propertyNames[0];
	}

	public boolean hasMultiProperties() {
		return (propertyNames.length > 1);
	}
}