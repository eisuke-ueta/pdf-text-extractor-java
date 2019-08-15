package com.pdf.text.extractor.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ItemMapperConfig {

	@NonNull
	private HeaderConfig headerConfig;
	@NonNull
	private DetailConfig detailConfig;
	@NonNull
	private Boolean debug = Boolean.FALSE;

}
