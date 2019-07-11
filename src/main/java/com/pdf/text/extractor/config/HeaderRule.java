package com.pdf.text.extractor.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class HeaderRule {

	@NonNull
	private String key;
	@NonNull
	private String regexp;
	private Filter filter;
	@NonNull
	private Boolean trim = Boolean.TRUE;
	@NonNull
	private Boolean onlyNumber = Boolean.FALSE;
}
