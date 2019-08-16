package com.pdf.item.mapper.app;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.io.IOUtils;

import com.pdf.item.mapper.config.ExtractorType;
import com.pdf.item.mapper.config.ItemMapperConfig;
import com.pdf.item.mapper.config.ItemMapperResult;
import com.pdf.item.mapper.config.TextExtractorSpec;
import com.pdf.item.mapper.service.DetailItemMapperService;
import com.pdf.item.mapper.service.DetailItemMapperServiceImpl;
import com.pdf.item.mapper.service.HeaderItemMapperService;
import com.pdf.item.mapper.service.HeaderItemMapperServiceImpl;
import com.pdf.item.mapper.service.TextExtractorService;
import com.pdf.item.mapper.service.TextExtractorServiceImpl;

public class PDFItemMapper implements ItemMapper {

	@Override
	public ItemMapperResult execute(final ItemMapperConfig config, final InputStream file) throws IOException {

		// Convert to bytes
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		IOUtils.copy(file, baos);
		final byte[] bytes = baos.toByteArray();

		if (config.getDebug())
			outputText(bytes);

		final Map<String, String> headerItems = new HashMap<String, String>();
		final HeaderItemMapperService headerItemMapperService = new HeaderItemMapperServiceImpl();

		// Extract header items by keyword
		final ByteArrayInputStream headerISByKeyword = new ByteArrayInputStream(bytes);
		headerItems.putAll(
				headerItemMapperService.execute(config.getHeaderSpec(), ExtractorType.KEYWORD, headerISByKeyword));

		// Extract header items by position
		final ByteArrayInputStream headerISByPosition = new ByteArrayInputStream(bytes);
		headerItems.putAll(
				headerItemMapperService.execute(config.getHeaderSpec(), ExtractorType.POSITION, headerISByPosition));

		// Extract detail items
		final ByteArrayInputStream detailInputStream = new ByteArrayInputStream(bytes);
		final DetailItemMapperService detailItemMapperService = new DetailItemMapperServiceImpl();
		final List<Map<String, String>> detailItems = detailItemMapperService.execute(config.getDetailSpec(),
				detailInputStream);

		// Build result
		final ItemMapperResult result = new ItemMapperResult();
		result.setHeaderItems(headerItems);
		result.setDetailItems(detailItems);
		return result;

	}

	/**
	 * Output all text information on file.
	 * 
	 * @param bytes
	 */
	private static void outputText(final byte[] bytes) {
		final ByteArrayInputStream debugInputStream = new ByteArrayInputStream(bytes);
		final TextExtractorService textExtractorService = new TextExtractorServiceImpl();
		final TextExtractorSpec textExtractorConfig = new TextExtractorSpec();
		final String text = textExtractorService.execute(textExtractorConfig, debugInputStream);
		System.out.println(text);
	}

}
