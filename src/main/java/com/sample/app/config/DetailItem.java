package com.sample.app.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class DetailItem {

	@NonNull
	private String key;
	@NonNull
	private Position position;

}
