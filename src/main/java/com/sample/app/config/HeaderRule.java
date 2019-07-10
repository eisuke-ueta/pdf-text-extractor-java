package com.sample.app.config;

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
	private boolean trim = true;
	private boolean onlyNumber = false;
}
