package com.sample.app.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class HeaderConfig {
	
	private int startPage = 1;
	private boolean sortByPosition = true;
	private boolean debug = false;

	@NonNull
	private List<HeaderRule> rules;
}
