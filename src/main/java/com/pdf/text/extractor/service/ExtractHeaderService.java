package com.pdf.text.extractor.service;

import java.io.InputStream;
import java.util.Map;

import com.pdf.text.extractor.config.HeaderConfig;

public interface ExtractHeaderService {

	/**
	 * Extract items
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public Map<String, String> execute(HeaderConfig config, InputStream file);

}
