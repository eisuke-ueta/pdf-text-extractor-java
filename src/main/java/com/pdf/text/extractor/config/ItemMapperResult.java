package com.pdf.text.extractor.config;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ItemMapperResult {

	private Map<String, String> headerItems;
	private List<Map<String, String>> detailItems;

}
