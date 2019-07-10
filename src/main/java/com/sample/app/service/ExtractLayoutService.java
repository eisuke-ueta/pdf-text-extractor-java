package com.sample.app.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sample.app.config.ExtractConfig;
import com.sample.app.config.HeaderConfig;

public interface ExtractLayoutService {

	/**
	 * Extract text from file
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> execute(ExtractConfig config, File file);

}
