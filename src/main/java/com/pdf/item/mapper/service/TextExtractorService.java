package com.pdf.item.mapper.service;

import java.io.InputStream;
import java.util.Map;

import com.pdf.item.mapper.config.HeaderSpec;
import com.pdf.item.mapper.config.TextExtractorSpec;

public interface TextExtractorService {

	/**
	 * Extract text from PDF.
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public String execute(TextExtractorSpec config, InputStream file);

}
