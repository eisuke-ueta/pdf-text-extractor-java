package com.sample.app.service;

import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import com.sample.app.config.DetailItem;
import com.sample.app.config.DetailPage;
import com.sample.app.config.DetailConfig;
import com.sample.app.config.ExtractConfig;
import com.sample.app.config.Position;

public class ExtractLayoutServiceImpl implements ExtractLayoutService {

	@Override
	public List<Map<String, String>> execute(ExtractConfig config, File file) {

		List<Map<String, String>> result = new ArrayList<>();

		try {
			final DetailConfig rule = config.getDetailConfig();
			final PDDocument doc = PDDocument.load(file);
			final PDFTextStripperByArea stripper = new PDFTextStripperByArea();
			stripper.setSortByPosition(rule.isSortByPosition());

			final int pageSize = rule.getEndPage() - rule.getEndPage() + 1;
			final List<DetailPage> detailPages = rule.getPages();

			int pageNumber = rule.getStartPage();
			// TODO
			while (pageNumber < rule.getEndPage()) {

				final PDPage page = doc.getPage(pageNumber);

				// Get detail page setting
				DetailPage pageRule = getDetailPageRule(rule, detailPages, pageNumber);

				final int START_LINE = pageRule.getStartLine();
				final int END_LINE = pageRule.getEndLine();

				Map<String, List<String>> rowItems = new HashMap<String, List<String>>();

				// Get items on tables
				final List<DetailItem> detailItems = pageRule.getItems();
				for (DetailItem detailItem : detailItems) {

					final Position position = detailItem.getPosition();
					final int LEFT = position.getLeft();
					final int WIDTH = position.getWidth();
					final int HEIGHT = position.getHeight();
					final String KEY = detailItem.getKey();

					// Add region
					// TODO HEIGHT can be text
					final int ITEM_SIZE = (END_LINE - START_LINE) / HEIGHT;
					for (int i = 0; i < ITEM_SIZE; i++) {
						Rectangle rect = new Rectangle(LEFT, START_LINE + HEIGHT * i, WIDTH, HEIGHT);
						stripper.addRegion(KEY + i, rect);
					}

					// Extract from page
					stripper.extractRegions(page);

					// Get items on column
					List<String> items = new ArrayList<>();
					for (int i = 0; i < ITEM_SIZE; i++) {
						String item = stripper.getTextForRegion(KEY + i);
						item = item.trim();
						item = item.replace("\n", "");
						item = item.replace("\r", "");
						items.add(item);
					}

					rowItems.put(KEY, items);
				}

//				for (Map.Entry<String, List<String>> entry : rowItems.entrySet()) {
//					System.out.println("=== " + entry.getKey() + " ===");
//					for (String item : entry.getValue()) {
//						System.out.println(item);
//					}
//				}

				final int ITEM_SIZE = rowItems.entrySet().stream().map(entrySet -> {
					return entrySet.getValue().size();
				}).findFirst().get();

				// Convert to details
				List<Map<String, String>> details = new ArrayList<>();
				for (int i = 0; i < ITEM_SIZE; i++) {
					Map<String, String> detail = new HashMap<>();

					for (DetailItem detailItem : detailItems) {
						final String KEY = detailItem.getKey();
						List<String> tableItems = rowItems.get(KEY);
						detail.put(KEY, tableItems.get(i));
					}

					details.add(detail);
				}

				result.addAll(details);

				pageNumber++;
			}

//			for (Map<String, String> row : result) {
//				for (Map.Entry<String, String> entry : row.entrySet()) {
//					System.out.print(entry.getValue() + ", ");
//				}
//				System.out.print(row.get("detail_no") + ", ");
//				System.out.print(row.get("detail_name_1") + ", ");
//				System.out.print(row.get("detail_name_2") + ", ");
//				System.out.print(row.get("detail_spec_1") + ", ");
//				System.out.print(row.get("detail_spec_2"));
//				System.out.println();
//			}

			return result;
		} catch (Exception e) {
			System.out.println(e);
			return result;
		}
	}

	private DetailPage getDetailPageRule(final DetailConfig rule, final List<DetailPage> pages, int pageNumber) {
		for (DetailPage page : pages) {
			if (page.getNumber() == pageNumber) {
				return page;
			}
		}
		return rule.getDefaultPage();
	}

	private boolean indexExists(final List list, final int index) {
		return index >= 0 && index < list.size();
	}

}
