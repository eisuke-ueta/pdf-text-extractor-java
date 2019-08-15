package com.pdf.text.extractor.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class HeaderConfig {

	@NonNull
	private Integer startPage = 0;
	@NonNull
	private Boolean sortByPosition = Boolean.TRUE;
	@NonNull
	private List<HeaderRule> rules;
}
