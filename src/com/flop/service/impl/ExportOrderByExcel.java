package com.flop.service.impl;

import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.flop.controller.AppointmentController;
import com.flop.model.Appointment;
import com.flop.model.Order;

@Service("exportOrderByExcel")
public class ExportOrderByExcel {
	public XSSFWorkbook createExcel(Appointment appoint, List<Order> orders) {
		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFCellStyle style = workbook.createCellStyle();
		style.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont font = workbook.createFont();
		font.setFontName("等线");
		font.setFontHeightInPoints((short) 12);
		font.setBold(true);
		style.setFont(font);
		style.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style.setBorderRight(XSSFCellStyle.BORDER_THIN);
		XSSFSheet sheet = workbook.createSheet("Attendance Sheet");
		// the 1st row
		XSSFRow row = sheet.createRow(0);
		XSSFCell cell = row.createCell(0);
		cell.setCellValue("");
		cell.setCellStyle(style);
		cell = row.createCell(1);
		cell.setCellValue(new DateTime(appoint.getDate()).toString("yyyy/M/d"));
		cell.setCellStyle(style);
		cell = row.createCell(2);
		cell.setCellValue(new DateTime(appoint.getDate()).dayOfWeek().getAsText(Locale.US));
		cell.setCellStyle(style);
		cell = row.createCell(3);
		cell.setCellValue(AppointmentController.map.get(appoint.getLesson() + ""));
		cell.setCellStyle(style);
		cell = row.createCell(4);
		cell.setCellValue(appoint.getPlace());
		cell.setCellStyle(style);
		for (int i = 0; i < 4; i++) {
			sheet.addMergedRegion(new CellRangeAddress(0, 1, i, i));
			setRegionBorder(XSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 1, i, i), sheet, workbook);
		}
		sheet.addMergedRegion(new CellRangeAddress(0, 1, 4, 6));
		setRegionBorder(XSSFCellStyle.BORDER_THIN, new CellRangeAddress(0, 1, 4, 6), sheet, workbook);
		
		row = sheet.createRow(2);
		cell = row.createCell(0);
		cell.setCellValue(appoint.getUserInfo().getName());
		XSSFCellStyle nameStyle = workbook.createCellStyle();
		nameStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
		nameStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		nameStyle.setFont(font);
		nameStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
		nameStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		nameStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		nameStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cell.setCellStyle(nameStyle);
		sheet.addMergedRegion(new CellRangeAddress(2, 3, 0, 6));
		setRegionBorder(XSSFCellStyle.BORDER_THIN, new CellRangeAddress(2, 3, 0, 6), sheet, workbook);
		
		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		style2.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		XSSFFont font2 = workbook.createFont();
		font2.setFontName("等线");
		font2.setFontHeightInPoints((short) 11);
		style2.setFont(font2);
		style2.setBorderTop(XSSFCellStyle.BORDER_THIN);
		style2.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		style2.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		style2.setBorderRight(XSSFCellStyle.BORDER_THIN);
		
		row = sheet.createRow(4);
		cell = row.createCell(0);
		cell.setCellValue("");
		cell.setCellStyle(style2);
		cell = row.createCell(1);
		cell.setCellValue("Name");
		cell.setCellStyle(style2);
		cell = row.createCell(2);
		cell.setCellValue("NO.");
		cell.setCellStyle(style2);
		cell = row.createCell(3);
		cell.setCellValue("College");
		cell.setCellStyle(style2);
		cell = row.createCell(4);
		cell.setCellValue("Grade");
		cell.setCellStyle(style2);
		cell = row.createCell(5);
		cell.setCellValue("Signature");
		cell.setCellStyle(style2);
		cell = row.createCell(6);
		cell.setCellValue("Score");
		cell.setCellStyle(style2);
		
		int i = 5;		
		for (Order order : orders) {
			row = sheet.createRow(i);
			cell = row.createCell(0);
			cell.setCellValue(i - 4);
			cell.setCellStyle(style2);
			cell = row.createCell(1);
			cell.setCellValue(order.getUserInfo().getName());
			cell.setCellStyle(style2);
			cell = row.createCell(2);
			cell.setCellValue(order.getUserInfo().getUsername());
			cell.setCellStyle(style2);
			cell = row.createCell(3);
			cell.setCellValue(order.getUserInfo().getCollege());
			cell.setCellStyle(style2);
			cell = row.createCell(4);
			cell.setCellValue(order.getUserInfo().getGrade());
			cell.setCellStyle(style2);
			cell = row.createCell(5);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			cell = row.createCell(6);
			cell.setCellValue("");
			cell.setCellStyle(style2);
			i++;
		}
		row = sheet.createRow(i + 3);
		cell = row.createCell(3);
		cell.setCellValue("Teacher Signature:");
		XSSFCellStyle styleWithoutBorder = workbook.createCellStyle();
		styleWithoutBorder.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		styleWithoutBorder.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		styleWithoutBorder.setFont(font2);
		cell.setCellStyle(styleWithoutBorder);
		sheet.addMergedRegion(new CellRangeAddress(i + 3, i + 4, 3, 4));
		for (int j = 0; j < 7; j++) {
			sheet.autoSizeColumn(j);
		}
		sheet.setColumnWidth(0, 2000);
		sheet.setColumnWidth(1, 4000);
		sheet.setColumnWidth(2, 4000);
		sheet.setColumnWidth(3, 6000);
		sheet.setColumnWidth(4, 4000);
		sheet.setColumnWidth(5, 4000);
		sheet.setColumnWidth(6, 2000);
		return workbook;
	}
	
	private void setRegionBorder(int border, CellRangeAddress region, Sheet sheet, Workbook wb) {
		RegionUtil.setBorderTop(border, region, sheet, wb);
		RegionUtil.setBorderBottom(border, region, sheet, wb);
		RegionUtil.setBorderLeft(border, region, sheet, wb);
		RegionUtil.setBorderRight(border, region, sheet, wb);
	}
}
