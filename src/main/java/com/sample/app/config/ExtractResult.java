package com.sample.app.config;

import java.util.List;
import java.util.Map;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ExtractResult {

	private Map<String, String> headerItems;
	private List<Map<String, String>> detailItems;

}
