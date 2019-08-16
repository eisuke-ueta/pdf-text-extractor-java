package com.pdf.item.mapper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailRule {

	@NonNull
	private String key;
	@NonNull
	private Position position;

}
