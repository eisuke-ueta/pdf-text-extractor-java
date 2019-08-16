package com.pdf.item.mapper.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class Position {

	@NonNull
	private Integer left;
	private Integer top; // No need to specify for detail items
	@NonNull
	private Integer width;
	@NonNull
	private Integer height;

}
