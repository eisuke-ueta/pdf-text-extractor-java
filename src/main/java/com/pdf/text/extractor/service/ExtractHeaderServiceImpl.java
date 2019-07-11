package com.pdf.text.extractor.service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.pdf.text.extractor.config.HeaderConfig;
import com.pdf.text.extractor.config.HeaderRule;
import com.pdf.text.extractor.config.TargetLine;

public class ExtractHeaderServiceImpl implements ExtractHeaderService {

	private static final int FIRST_MATCH = 1;

	@Override
	public Map<String, String> execute(HeaderConfig config, File file) {

		final String text = extractText(config, file);
		if (config.getDebug()) {
			System.out.println(text);
		}
		final Map<String, String> result = extractItems(config, text);

		return result;
	}

	/**
	 * Extract items from text.
	 * 
	 * @param config
	 * @param text
	 * @return
	 */
	private Map<String, String> extractItems(final HeaderConfig config, final String text) {
		final Map<String, String> result = new HashMap<String, String>();

		final List<HeaderRule> rules = config.getRules();

		// Extract target line from text
		final Map<String, String> lines = new HashMap<String, String>();
		for (HeaderRule rule : rules) {
			String line = extractLine(text, rule);
			lines.put(rule.getKey(), line);
		}

		// Extract item from line
		for (HeaderRule rule : rules) {

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

			// Adjust item
			String matchedValue = matcher.group(FIRST_MATCH);
			if (rule.getOnlyNumber()) {
				matchedValue = matchedValue.replaceAll("[^0-9]", "");
			}
			if (rule.getTrim()) {
				matchedValue = matchedValue.trim();
			}

			result.put(rule.getKey(), matchedValue);
		}

		return result;
	}

	/**
	 * Extract text from PDF file.
	 * 
	 * @param config
	 * @param file
	 * @return
	 */
	private String extractText(HeaderConfig config, File file) {

		try {
			final PDDocument pdDoc = PDDocument.load(file);
			final PDFTextStripper pdfStripper = new PDFTextStripper();

			pdfStripper.setStartPage(config.getStartPage());
			pdfStripper.setSortByPosition(config.getSortByPosition());

			return pdfStripper.getText(pdDoc);
		} catch (Exception e) {
			System.out.println(e);
			return StringUtils.EMPTY;
		}
	}

	/**
	 * Extract target line from text.
	 * 
	 * @param text
	 * @param rule
	 * @return
	 */
	private String extractLine(String text, HeaderRule rule) {

		final Pattern pattern = getLineExtractPattern(rule);
		final ListIterator<String> iterator = text.lines().collect(Collectors.toList()).listIterator();

		while (iterator.hasNext()) {
			String line = iterator.next();

			// Go to next line
			final Matcher matcher = pattern.matcher(line);
			if (!matcher.find())
				continue;

			if (Objects.isNull(rule.getFilter())) {
				return line;
			}
//			if () {
//				
//			}
			if (rule.getFilter().getTarget().equals(TargetLine.PREVIOUS.toString())) {
				return iterator.hasPrevious() ? iterator.previous() : "";
			}
			if (rule.getFilter().getTarget().equals(TargetLine.NEXT.toString())) {
				return iterator.hasNext() ? iterator.next() : "";
			}
			return line;
		}

		return "";
	}

	private Pattern getLineExtractPattern(HeaderRule rule) {
		if (Objects.nonNull(rule.getFilter())) {
			return Pattern.compile(rule.getFilter().getRegexp());
		}
		return Pattern.compile(rule.getRegexp());
	}

}
