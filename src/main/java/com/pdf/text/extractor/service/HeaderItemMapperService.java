package com.pdf.text.extractor.service;

import java.io.InputStream;
import java.util.Map;

import com.pdf.text.extractor.config.ExtractorType;
import com.pdf.text.extractor.config.HeaderConfig;

public interface HeaderItemMapperService {

	/**
	 * Extract items
	 * 
	 * @param config
	 * @param type
	 * @param file
	 * @return
	 */
	public Map<String, String> execute(final HeaderConfig config, final ExtractorType type, final InputStream file);

}
