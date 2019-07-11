package com.pdf.text.extractor.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Position {

	@NonNull
	private Integer left;
	@NonNull
	private Integer width;
	@NonNull
	private Integer height;

}
