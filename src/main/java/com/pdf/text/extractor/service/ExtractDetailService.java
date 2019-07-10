package com.pdf.text.extractor.service;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.pdf.text.extractor.config.DetailConfig;
import com.pdf.text.extractor.config.ExtractConfig;
import com.pdf.text.extractor.config.HeaderConfig;

public interface ExtractDetailService {

	/**
	 * Extract text from file
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> execute(DetailConfig config, File file);

}
