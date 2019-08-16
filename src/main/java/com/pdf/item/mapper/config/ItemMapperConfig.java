package com.pdf.item.mapper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ItemMapperConfig {

	@NonNull
	private HeaderSpec headerSpec;
	@NonNull
	private DetailSpec detailSpec;
	@NonNull
	private Boolean debug = Boolean.FALSE;

}
