package com.msis.common.parser;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;

public abstract class JsonMapper {
	
	private static ObjectMapper mapper = newInstance();
	
	public static ObjectMapper newInstance() {
        ObjectMapper mapper = new ObjectMapper();
        final SerializationConfig serConfig = mapper.getSerializationConfig();
        serConfig.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);

        final DeserializationConfig deserConfig = mapper.getDeserializationConfig();
        deserConfig.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }
	
	public static ObjectMapper getInstance() {
        return mapper;
    }
}
