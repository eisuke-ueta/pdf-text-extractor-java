package com.pdf.item.mapper.app;

import java.io.ByteArrayInputStream;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.pdfbox.io.IOUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
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

public class App {

	final static String FILE_PATH = System.getenv("FILE_PATH");
	final static String JSON_PATH = System.getenv("JSON_PATH");

	public static void main(String[] args) {

		try {
			// Load file
			final File file = new File(FILE_PATH);
			final InputStream inputStream = new FileInputStream(file);

			// Convert to bytes
			final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			IOUtils.copy(inputStream, baos);
			final byte[] bytes = baos.toByteArray();

			// Build configuration
			final ItemMapperConfig config = getItemMapperConfigFromJSON();

			if (config.getDebug()) {
				outputText(bytes);
			}

			final Map<String, String> headerItems = new HashMap<String, String>();
			final HeaderItemMapperService headerItemMapperService = new HeaderItemMapperServiceImpl();

			// Extract header items by keyword
			final ByteArrayInputStream headerISByKeyword = new ByteArrayInputStream(bytes);
			headerItems.putAll(
					headerItemMapperService.execute(config.getHeaderSpec(), ExtractorType.KEYWORD, headerISByKeyword));

			// Extract header items by position
			final ByteArrayInputStream headerISByPosition = new ByteArrayInputStream(bytes);
			headerItems.putAll(headerItemMapperService.execute(config.getHeaderSpec(), ExtractorType.POSITION,
					headerISByPosition));

			// Extract detail items
			final ByteArrayInputStream detailInputStream = new ByteArrayInputStream(bytes);
			final DetailItemMapperService detailItemMapperService = new DetailItemMapperServiceImpl();
			final List<Map<String, String>> detailItems = detailItemMapperService.execute(config.getDetailSpec(),
					detailInputStream);

			// Build result
			final ItemMapperResult result = new ItemMapperResult();
			result.setHeaderItems(headerItems);
			result.setDetailItems(detailItems);

			// Output result
			output(result);
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static void outputText(final byte[] bytes) {
		final ByteArrayInputStream debugInputStream = new ByteArrayInputStream(bytes);
		final TextExtractorService textExtractorService = new TextExtractorServiceImpl();
		final TextExtractorSpec textExtractorSpec = new TextExtractorSpec();
		final String text = textExtractorService.execute(textExtractorSpec, debugInputStream);
		System.out.println(text);
	}

	private static void output(final ItemMapperResult result) {
		for (Map.Entry<String, String> entry : result.getHeaderItems().entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}

		for (Map<String, String> details : result.getDetailItems()) {
			System.out.print(details.get("detail_no") + ", ");
			System.out.print(details.get("detail_name_1") + ", ");
			System.out.print(details.get("detail_name_2") + ", ");
			System.out.print(details.get("detail_spec_1") + ", ");
			System.out.print(details.get("detail_spec_2") + ", ");
			System.out.print(details.get("detail_quantity") + ", ");
			System.out.print(details.get("detail_unit") + ", ");
			System.out.print(details.get("detail_unit_amount") + ", ");
			System.out.print(details.get("detail_amount") + ", ");
			System.out.print(details.get("detail_memo"));
			System.out.println("");
		}
	}

	private static ItemMapperConfig getItemMapperConfigFromJSON() {
		ObjectMapper mapper = new ObjectMapper();
		ItemMapperConfig config = null;
		try {
			File file = new File(JSON_PATH);
			config = mapper.readValue(file, ItemMapperConfig.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

}