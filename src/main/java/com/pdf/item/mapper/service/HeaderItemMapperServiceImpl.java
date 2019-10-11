package com.pdf.item.mapper.service;

import java.awt.Rectangle;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.pdf.item.mapper.config.ExtractorType;
import com.pdf.item.mapper.config.HeaderRule;
import com.pdf.item.mapper.config.HeaderSpec;
import com.pdf.item.mapper.config.Position;
import com.pdf.item.mapper.config.TextExtractorSpec;

public class HeaderItemMapperServiceImpl implements HeaderItemMapperService {

	private static final int FIRST_MATCH = 1;

	@Override
	public Map<String, String> execute(final HeaderSpec spec, final ExtractorType type, final InputStream file) {

		switch (type) {
		case KEYWORD:
			return executeByKeyword(spec, file);
		case POSITION:
			return executeByPosition(spec, file);
		default:
			return new HashMap<String, String>();
		}
	}

	private Map<String, String> executeByKeyword(final HeaderSpec config, final InputStream inputStream) {

		final String text = extractText(config, inputStream);

		final List<HeaderRule> rules = config.getRules().stream()
				.filter(rule -> rule.getType().equals(ExtractorType.KEYWORD.name())).collect(Collectors.toList());

		return extractItemsByKeyword(text, rules);
	}

	private Map<String, String> executeByPosition(final HeaderSpec config, final InputStream inputStream) {
		final Map<String, String> result = new HashMap<String, String>();

		final List<HeaderRule> rules = config.getRules().stream()
				.filter(rule -> rule.getType().equals(ExtractorType.POSITION.name())).collect(Collectors.toList());

		try {
			final PDDocument document = PDDocument.load(inputStream);
			final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(config.getSortByPosition());

			int currentPage = 0;
			final int endPage = document.getPages().getCount();
			while (currentPage < endPage) {

				// Get settings
				final PDPage pdPage = document.getPage(currentPage);

				// Get items on tables
				for (HeaderRule rule : rules) {

					final String KEY = rule.getKey();
					final int page = rule.getPage();

					// Skip to next rule
					if (currentPage + 1 != page)
						continue;

					final Position position = rule.getPosition();
					final int LEFT = position.getLeft();
					final int TOP = position.getTop();
					final int WIDTH = position.getWidth();
					final int HEIGHT = position.getHeight();

					final Rectangle rectangle = new Rectangle(LEFT, TOP, WIDTH, HEIGHT);
					stripper.addRegion(KEY, rectangle);
					stripper.extractRegions(pdPage);
					String value = stripper.getTextForRegion(KEY);

					// Apply Formatter
					value = format(rule, value);

					result.put(KEY, value);
				}

				currentPage++;
			}

			return result;
		} catch (Exception e) {
			System.out.println(e);
			return result;
		}
	}

	private Map<String, String> extractItemsByKeyword(final String text, final List<HeaderRule> keywordTypeRules) {
		final Map<String, String> result = new HashMap<String, String>();

		// Extract target line from text
		final Map<String, String> lines = new HashMap<String, String>();
		for (HeaderRule rule : keywordTypeRules) {
			String line = extractLine(text, rule);
			lines.put(rule.getKey(), line);
		}

		// Extract item from line
		for (HeaderRule rule : keywordTypeRules) {

			// Go to next rule
			final String value = lines.get(rule.getKey());
			if (StringUtils.isEmpty(value)) {
				result.put(rule.getKey(), "");
				continue;
			}

			// Find matched item from line
			final Pattern pattern = Pattern.compile(rule.getRegexp());
			final Matcher matcher = pattern.matcher(value);
			if (!matcher.find()) {
				result.put(rule.getKey(), "");
				continue;
			}

			// Extract matched value
			String matchedValue = matcher.group(FIRST_MATCH);

			// Apply Formatter
			matchedValue = format(rule, matchedValue);

			result.put(rule.getKey(), matchedValue);
		}

		return result;
	}

	private String format(HeaderRule rule, String value) {
		if (rule.getOnlyNumber()) {
			value = value.replaceAll("[^0-9]", "");
		}
		if (rule.getNoLineBreak()) {
			value = value.replace("\n", "");
			value = value.replace("\r", "");
		}
		if (rule.getTrim()) {
			value = value.trim();
		}
		return value;
	}

	/**
	 * Extract text from PDF file.
	 * 
	 * @param headerConfig
	 * @param inputStream
	 * @return
	 */
	private String extractText(HeaderSpec headerConfig, InputStream inputStream) {

		final TextExtractorService textExtractorService = new TextExtractorServiceImpl();

		// Set configuration
		final TextExtractorSpec textExtractorConfig = new TextExtractorSpec();
		textExtractorConfig.setStartPage(headerConfig.getStartPage());
		textExtractorConfig.setSortByPosition(headerConfig.getSortByPosition());

		return textExtractorService.execute(textExtractorConfig, inputStream);

	}

	/**
	 * Extract target line from text.
	 * 
	 * @param text
	 * @param rule
	 * @return
	 */
	private String extractLine(String text, HeaderRule rule) {

		final Pattern pattern = Pattern.compile(rule.getRegexp());

		final ListIterator<String> iterator = splitToLines(text).collect(Collectors.toList()).listIterator();

		while (iterator.hasNext()) {
			String line = iterator.next();

			// Go to next line
			final Matcher matcher = pattern.matcher(line);
			if (!matcher.find())
				continue;

			return line;
		}

		return "";
	}

	private Stream<String> splitToLines(String text) {
		return Stream.of(text.split("\\r?\\n"));
	}

}
