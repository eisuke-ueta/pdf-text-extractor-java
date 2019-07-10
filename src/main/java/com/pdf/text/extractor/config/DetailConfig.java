package com.pdf.text.extractor.config;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailConfig {

	private int startPage = 1;
	private int endPage;
	private boolean sortByPosition = true;

	@NonNull
	private DetailPageRule defaultPageRule;
	private List<DetailPageRule> pageRules = new ArrayList<>();

}
