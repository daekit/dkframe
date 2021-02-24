package com.dksys.biz.config;

import java.math.BigDecimal;

import org.apache.commons.collections4.map.ListOrderedMap;

@SuppressWarnings("all")
public class CamelMap extends ListOrderedMap <String, Object> {

    private String toProperCase(String s, boolean isCapital) {

        String rtnValue = "";

        if(isCapital){
            rtnValue = s.substring(0, 1).toUpperCase() +
                    s.substring(1).toLowerCase();
        }else{
            rtnValue = s.toLowerCase();
        }
        return rtnValue;
    }

    private String toCamelCase(String s) {
        String[] parts = s.split("_");
        StringBuilder camelCaseString = new StringBuilder();

        for (int i = 0; i < parts.length ; i++) {
            String part = parts[i];
            camelCaseString.append(toProperCase(part, (i != 0 ? true : false))) ;
        }

        return camelCaseString.toString();
    }

    @Override
    public Object put(String key, Object value) {
    	if(value instanceof BigDecimal) {
    		value = value.toString();
    	}
    	return super.put(toCamelCase((String)key), value);
    }
}