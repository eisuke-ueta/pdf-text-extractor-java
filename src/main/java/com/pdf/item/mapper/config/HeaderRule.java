package com.pdf.item.mapper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class HeaderRule {

	@NonNull
	private String key;
	@NonNull
	private String type;

	// For KEYWORD mapping
	private String regexp;

	// For POSITION mapping
	private Integer page;
	private Position position;

	@NonNull
	private Boolean trim = Boolean.TRUE;
	@NonNull
	private Boolean noLineBreak = Boolean.TRUE;
	@NonNull
	private Boolean onlyNumber = Boolean.FALSE;
}
