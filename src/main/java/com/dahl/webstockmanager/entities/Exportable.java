package com.dahl.webstockmanager.entities;

import java.util.Map;

public interface Exportable {
	
	public String[] ToRowContent(); // For PDF docs
	public Map<String, Object> toRowMap(); // For Excel docs
}
