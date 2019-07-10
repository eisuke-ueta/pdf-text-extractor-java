package com.pdf.text.extractor.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailPageRule {

	private int page;
	private int startLine;
	private int endLine;

	@NonNull
	private List<DetailRule> rules;

}
