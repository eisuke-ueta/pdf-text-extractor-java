package com.pdf.item.mapper.app;

import java.io.IOException;
import java.io.InputStream;

import com.pdf.item.mapper.config.ItemMapperConfig;
import com.pdf.item.mapper.config.ItemMapperResult;

public interface ItemMapper {

	/**
	 * Extract text information and convert to structured data.
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	public ItemMapperResult execute(final ItemMapperConfig config, final InputStream file) throws IOException;

}
