package com.pdf.item.mapper.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.pdf.item.mapper.config.DetailSpec;

public interface DetailItemMapperService {

	/**
	 * Detail item mapper service.
	 * 
	 * @param spec
	 * @param file
	 * @return
	 */
	public List<Map<String, String>> execute(final DetailSpec spec, final InputStream file);

}
