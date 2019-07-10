package com.sample.app.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailPage {

	private int number;
	private int startLine;
	private int endLine;

	@NonNull
	private List<DetailItem> items;

}
