package com.sample.app.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Filter {
	
	@NonNull
	private String regexp;
	@NonNull
	private String target;

}
