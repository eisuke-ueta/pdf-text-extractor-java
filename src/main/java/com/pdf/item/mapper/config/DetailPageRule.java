package com.pdf.item.mapper.config;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailPageRule {

	@NonNull
	private Integer page;
	@NonNull
	private Integer startLine;
	@NonNull
	private Integer endLine;
	@NonNull
	private Integer rowHeight;

	@NonNull
	private List<DetailRule> rules;

}
