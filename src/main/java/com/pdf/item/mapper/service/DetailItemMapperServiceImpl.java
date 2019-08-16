package com.pdf.item.mapper.service;

import java.awt.Rectangle;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.pdf.item.mapper.config.DetailPageRule;
import com.pdf.item.mapper.config.DetailRule;
import com.pdf.item.mapper.config.DetailSpec;
import com.pdf.item.mapper.config.ItemMapperConfig;
import com.pdf.item.mapper.config.Position;

public class DetailItemMapperServiceImpl implements DetailItemMapperService {

	@Override
	public List<Map<String, String>> execute(final DetailSpec config, final InputStream inputStream) {

		final List<Map<String, String>> result = new ArrayList<>();

		try {
			final PDDocument document = PDDocument.load(inputStream);
			final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(config.getSortByPosition());

			int page = config.getStartPage();
			final int endPage = getEndPage(config, document);
			while (page < endPage) {

				// Get settings
				final PDPage pdPage = document.getPage(page);
				final DetailPageRule pageRule = getDetailPageRule(config, page);
				final int START_LINE = pageRule.getStartLine();
				final int END_LINE = pageRule.getEndLine();

				// Get items on tables
				final Map<String, List<String>> tableItems = new HashMap<String, List<String>>();
				final List<DetailRule> rules = pageRule.getRules();
				for (DetailRule rule : rules) {

					final String KEY = rule.getKey();
					final Position position = rule.getPosition();
					final int LEFT = position.getLeft();
					final int WIDTH = position.getWidth();
					final int HEIGHT = position.getHeight();
					final int ITEM_SIZE = (END_LINE - START_LINE) / HEIGHT;

					// Extract regions
					for (int i = 0; i < ITEM_SIZE; i++) {
						Rectangle rect = new Rectangle(LEFT, START_LINE + HEIGHT * i, WIDTH, HEIGHT);
						stripper.addRegion(KEY + i, rect);
					}
					stripper.extractRegions(pdPage);

					// Get items on column
					List<String> items = new ArrayList<>();
					for (int i = 0; i < ITEM_SIZE; i++) {
						String item = stripper.getTextForRegion(KEY + i);
						item = item.replace("\n", "");
						item = item.replace("\r", "");
						item = item.trim();
						items.add(item);
					}

					tableItems.put(KEY, items);
				}

				List<Map<String, String>> details = convertToDetails(tableItems, rules);
				List<Map<String, String>> filteredDetails = filterEmptyDetail(details);
				result.addAll(filteredDetails);

				page++;
			}

			return result;
		} catch (Exception e) {
			System.out.println(e);
			return result;
		}
	}

	private int getEndPage(DetailSpec config, final PDDocument document) {
		return Objects.nonNull(config.getEndPage()) ? config.getEndPage() : document.getPages().getCount();
	}

	/**
	 * Only return non empty details.
	 * 
	 * @param details
	 * @return
	 */
	private List<Map<String, String>> filterEmptyDetail(List<Map<String, String>> details) {
		List<Map<String, String>> filteredDetails = new ArrayList<>();
		for (Map<String, String> detail : details) {
			Optional<String> stringOpt = detail.values().stream().filter(value -> !StringUtils.isEmpty(value))
					.findAny();
			if (!stringOpt.isPresent())
				continue;
			filteredDetails.add(detail);
		}
		return filteredDetails;
	}

	private List<Map<String, String>> convertToDetails(final Map<String, List<String>> tableItems,
			final List<DetailRule> rules) {

		// Get item size
		final int ITEM_SIZE = tableItems.entrySet().stream().map(entrySet -> {
			return entrySet.getValue().size();
		}).findFirst().get();

		// Convert to details
		List<Map<String, String>> details = new ArrayList<>();

		for (int i = 0; i < ITEM_SIZE; i++) {
			Map<String, String> detail = new HashMap<>();

			for (DetailRule rule : rules) {
				final String KEY = rule.getKey();
				List<String> items = tableItems.get(KEY);

				detail.put(KEY, items.get(i));
			}

			details.add(detail);
		}

		return details;
	}

	/**
	 * Get detail page rule. Return specific rule if exist, otherwise return default
	 * page rule.
	 * 
	 * @param config
	 * @param page
	 * @return
	 */
	private DetailPageRule getDetailPageRule(final DetailSpec config, int page) {
		final List<DetailPageRule> pageRules = config.getPageRules();
		for (DetailPageRule pageRule : pageRules) {
			if (pageRule.getPage() == page) {
				return pageRule;
			}
		}
		return config.getDefaultPageRule();
	}

}
