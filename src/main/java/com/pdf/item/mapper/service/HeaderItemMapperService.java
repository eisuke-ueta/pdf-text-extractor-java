package com.pdf.item.mapper.service;

import java.io.InputStream;
import java.util.Map;

import com.pdf.item.mapper.config.ExtractorType;
import com.pdf.item.mapper.config.HeaderSpec;

public interface HeaderItemMapperService {

	/**
	 * Header item mapper service.
	 * 
	 * @param spec
	 * @param type
	 * @param file
	 * @return
	 */
	public Map<String, String> execute(final HeaderSpec spec, final ExtractorType type, final InputStream file);

}
