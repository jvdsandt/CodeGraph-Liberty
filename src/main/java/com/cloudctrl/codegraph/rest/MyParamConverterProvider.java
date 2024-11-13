package com.cloudctrl.codegraph.rest;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.time.OffsetDateTime;

import jakarta.ws.rs.ext.ParamConverter;
import jakarta.ws.rs.ext.ParamConverterProvider;
import jakarta.ws.rs.ext.Provider;

@Provider
public class MyParamConverterProvider implements ParamConverterProvider {

	@Override
	public <T> ParamConverter<T> getConverter(Class<T> rawType, Type genericType, Annotation[] annotations) {
		if (rawType.equals(OffsetDateTime.class)) {
			return (ParamConverter<T>) new OffsetDateTimeConverter();
		}
		return null;
	}
}
