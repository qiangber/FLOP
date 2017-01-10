package com.flop.service.impl;

import java.util.List;

import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.flop.model.Order;

@Service("exportOrderByExcel")
public class ExportOrderByExcel {
	public XSSFWorkbook createExcel(String fileName, List<Order> orders) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		XSSFSheet sheet = workbook.createSheet("Attendance Sheet");
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue(fileName.replace("-", ":") + " Attendance Sheet");
		cell.setCellStyle(style);
		sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 3));
		row = sheet.createRow(1);
		cell = row.createCell(0);
		cell.setCellValue("No");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue("Name");
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue("Student ID");
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue("Sign In");
		cell.setCellStyle(style);
		int i = 2;
		for (Order order : orders) {
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(i - 1);
			cell.setCellStyle(style);
			cell = row.createCell(1);
			cell.setCellValue(order.getUserInfo().getName());
			cell.setCellStyle(style);
			cell = row.createCell(2);
			cell.setCellValue(order.getUserInfo().getUsername());
			cell.setCellStyle(style);
			i++;
		}
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 6000);
		sheet.setColumnWidth(3, 4000);
		return workbook;
	}
}
