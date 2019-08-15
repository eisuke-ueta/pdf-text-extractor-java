package com.pdf.text.extractor.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TextExtractorConfig {

	@NonNull
	private Integer startPage = 0;
	@NonNull
	private Boolean sortByPosition = Boolean.TRUE;

}
