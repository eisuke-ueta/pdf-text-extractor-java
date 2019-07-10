package com.sample.app.config;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtractConfig {

	private HeaderConfig headerConfig;
	private DetailConfig detailConfig;

}
