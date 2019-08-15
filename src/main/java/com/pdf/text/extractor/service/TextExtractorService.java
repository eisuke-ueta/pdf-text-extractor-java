package com.pdf.text.extractor.service;

import java.io.InputStream;
import java.util.Map;

import com.pdf.text.extractor.config.HeaderConfig;
import com.pdf.text.extractor.config.TextExtractorConfig;

public interface TextExtractorService {

	/**
	 * Extract text from PDF.
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public String execute(TextExtractorConfig config, InputStream file);

}
