package com.sample.app;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.app.config.ExtractConfig;
import com.sample.app.config.ExtractResult;
import com.sample.app.service.ExtractHeaderService;
import com.sample.app.service.ExtractHeaderServiceImpl;
import com.sample.app.service.ExtractLayoutService;
import com.sample.app.service.ExtractLayoutServiceImpl;

/**
 * Hello world!
 *
 */
public class App {

	final static String FILE_PATH = System.getenv("FILE_PATH");
	final static String JSON_PATH = System.getenv("JSON_PATH");

	public static void main(String[] args) {

		final File file = new File(FILE_PATH);

		// Build configuration
		ExtractConfig config = getExtractConfigFromJSON();
//		config.getHeaderConfig().setDebug(true);

		// Extract header items
		ExtractHeaderService extractHeaderService = new ExtractHeaderServiceImpl();
		final Map<String, String> headerItems = extractHeaderService.execute(config.getHeaderConfig(), file);

		// Extract detail items
		ExtractLayoutService extractLayoutService = new ExtractLayoutServiceImpl();
		final List<Map<String, String>> detailItems = extractLayoutService.execute(config, file);

		// Build result
		final ExtractResult result = new ExtractResult();
		result.setHeaderItems(headerItems);
		result.setDetailItems(detailItems);

		// Output result
		for (Map.Entry<String, String> entry : result.getHeaderItems().entrySet()) {
			System.out.println("Key:" + entry.getKey() + ", Value:" + entry.getValue());
		}

	}

	private static ExtractConfig getExtractConfigFromJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ExtractConfig config = null;
		try {
			File file = new File(JSON_PATH);
			config = mapper.readValue(file, ExtractConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

}
