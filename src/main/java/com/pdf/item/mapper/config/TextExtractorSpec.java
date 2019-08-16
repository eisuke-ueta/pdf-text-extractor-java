package com.pdf.item.mapper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TextExtractorSpec {

	@NonNull
	private Integer startPage = 0;
	@NonNull
	private Boolean sortByPosition = Boolean.TRUE;

}
