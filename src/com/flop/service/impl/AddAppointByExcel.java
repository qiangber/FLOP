package com.flop.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tomcat.util.buf.StringCache;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flop.model.Appointment;
import com.flop.model.Status;
import com.flop.model.User;
import com.flop.model.UserInfo;
import com.flop.service.inter.AppointServiceInter;
import com.flop.service.inter.CategoryServiceInter;
import com.flop.service.inter.ExcelHandler;
import com.flop.service.inter.UserServiceInter;
import com.flop.utils.HibernateUtils;

@Service("addAppointByExcel")
public class AddAppointByExcel extends ExcelHandler {
	
	@Autowired
	UserServiceInter userService;
	
	@Autowired
	CategoryServiceInter categoryService;
	
	@Autowired
	AppointServiceInter appointService;

	int i = 0;

	// 获取Excel文档的路径
	public Status getInfoFromExcel(String filePath) {
		Workbook workbook = null;
		StringBuilder msg = new StringBuilder();
		// 创建对Excel工作簿文件的引用
		try {
			if (isExcel2003(filePath) && !isExcel2007(filePath)) {
				workbook = new HSSFWorkbook(new FileInputStream(filePath));
			} else {
				workbook = new XSSFWorkbook(new FileInputStream(filePath));
			}
			// 在Excel文档中，第一张工作表的缺省索引是0
			// 其语句为：HSSFSheet sheet = workbook.getSheetAt(0);
			Sheet sheet = workbook.getSheet("Sheet1");
			// 获取到Excel文件中的所有行数
			int rows = sheet.getPhysicalNumberOfRows();
			// 遍历行 从第二行开始遍历
			System.out.println(rows);
			LinkedList<String> list = new LinkedList<>();
			Appointment appoint = new Appointment();
			rows: for (i = 1; i < rows; i++) {
				// 读取左上端单元格
				Row row = sheet.getRow(i);
				// 行不为空
				if (row != null) {
					// 获取到Excel文件中的所有的列
					int cells = row.getPhysicalNumberOfCells();
					if (cells < 6) {
						cells = 6;
					}
					// 遍历列
					cells: for (int j = 0; j < cells; j++) {
						// 获取到列的值
						Cell cell = row.getCell(j);
						String stringCell = "";
						if (cell != null) {
							switch (cell.getCellType()) {
								case Cell.CELL_TYPE_FORMULA:// 公式
									break;
								case Cell.CELL_TYPE_NUMERIC:// 数字格式
									if (DateUtil.isCellDateFormatted(cell)) {
										appoint.setDate(DateUtil.getJavaDate(cell.getNumericCellValue()));
										continue cells;
									} else {
										stringCell = ((int)cell.getNumericCellValue() + "").trim();									
									}
									break;
								case Cell.CELL_TYPE_STRING:// 字符串格式
									stringCell = cell.getStringCellValue().trim();
									break;
								default:
									break;
							}
						} else {
							break rows;
						}
						list.add(stringCell);
					}
					// 将数据插入到mysql数据库中
					if (list.get(0) == null || list.get(0).equals("")) {
						continue rows;
					}
					
					int lesson = Integer.parseInt(list.poll());
					if (lesson < 1 || lesson > 11) {
						msg.append(String.format("第%d行", i)).append("课程节数应在1-11之间！\n");
						continue rows;
					}
					appoint.setLesson(lesson);					
					
					Integer userId = userService.CheckUsername(list.poll());
					if (userId == null) {
						msg.append(String.format("第%d行", i)).append("该账号用户不存在！\n");
						continue rows;
					}
					appoint.setUserId(userId.toString());
					
					String type = list.poll();
					if (!type.matches("(writing|speaking|lab)")) {
						msg.append(String.format("第%d行", i)).append("类型格式应为writing, speaking, lab之一！\n");
						continue rows;
					}
					appoint.setType(type);
					
					Integer categoryId = categoryService.findIdByName(list.poll(), type);
					if (categoryId == null) {
						msg.append(String.format("第%d行", i)).append(type).append("中该类别不存在！\n");
						continue rows;
					}
					appoint.setCategoryId(categoryId.toString());
					appoint.setPlace(list.poll());
					appoint.setPublishTime(new DateTime().toString("YYYY-MM-dd HH:mm:ss"));
					
					appointService.add(appoint);
				}
			}
			return new Status("success", msg.append("添加完毕！").toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new Status("error", String.format("第%d行\n", i) + msg.toString());
		} catch (IOException e) {
			e.printStackTrace();
			return new Status("error", String.format("第%d行\n", i) + msg.toString());
		} catch (Exception e) {
			e.printStackTrace();
			return new Status("error", String.format("第%d行\n", i) + msg.toString());
		}
	}
}