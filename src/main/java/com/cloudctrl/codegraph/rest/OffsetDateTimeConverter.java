package com.cloudctrl.codegraph.rest;

import java.time.OffsetDateTime;

import jakarta.ws.rs.ext.ParamConverter;

public class OffsetDateTimeConverter implements ParamConverter<OffsetDateTime> {

	@Override
	public OffsetDateTime fromString(String s) {
		return OffsetDateTime.parse(s);
	}

	@Override
	public String toString(OffsetDateTime offsetDateTime) {
		return offsetDateTime.toString();
	}
}
