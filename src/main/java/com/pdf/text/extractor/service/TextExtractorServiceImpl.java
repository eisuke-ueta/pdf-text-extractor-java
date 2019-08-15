package com.pdf.text.extractor.service;

import java.io.InputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import com.pdf.text.extractor.config.TextExtractorConfig;

public class TextExtractorServiceImpl implements TextExtractorService {

	@Override
	public String execute(TextExtractorConfig config, InputStream file) {

		try {
			final PDDocument document = PDDocument.load(file);
			final PDFTextStripper pdfStripper = new PDFTextStripper();

			pdfStripper.setStartPage(config.getStartPage());
			pdfStripper.setSortByPosition(config.getSortByPosition());

			return pdfStripper.getText(document);
		} catch (Exception e) {
			System.out.println(e);
			return StringUtils.EMPTY;
		}
	}

}
