package com.flop.service.inter;

import com.flop.model.Status;

public abstract class ExcelHandler {

	int i = 0;

	// 获取Excel文档的路径
	public abstract Status getInfoFromExcel(String filePath);

	protected boolean isExcel2003(String filePath) {
		return filePath.matches("^.+\\.(?i)(xls)$");
	}

	protected boolean isExcel2007(String filePath) {
		return filePath.matches("^.+\\.(?i)(xlsx)$");
	}
}