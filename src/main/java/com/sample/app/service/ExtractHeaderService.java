package com.sample.app.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.sample.app.config.HeaderConfig;

public interface ExtractHeaderService {

	/**
	 * Extract items
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public Map<String, String> execute(HeaderConfig config, File file);

}
