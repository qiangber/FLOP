package com.flop.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import com.flop.model.Status;
import com.flop.model.User;
import com.flop.model.UserInfo;
import com.flop.service.inter.ExcelHandler;
import com.flop.utils.HibernateUtils;

@Service("addUserByExcel")
public class AddUserByExcel extends ExcelHandler {

	int i = 0;

	// 获取Excel文档的路径
	public Status getInfoFromExcel(String filePath) {
		Workbook workbook = null;
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
			List<String> list = new ArrayList<String>();
			User user = new User();
			rows: for (i = 1; i < rows; i++) {
				// 读取左上端单元格
				Row row = sheet.getRow(i);
				// 行不为空
				if (row != null) {
					// 获取到Excel文件中的所有的列
					int cells = row.getPhysicalNumberOfCells();
					if (cells < 13) {
						cells = 13;
					}
					list.removeAll(list);
					// 遍历列
					for (int j = 0; j < cells; j++) {
						// 获取到列的值
						Cell cell = row.getCell(j);
						if (cell != null) {
							switch (cell.getCellType()) {
							case Cell.CELL_TYPE_FORMULA:// 公式
								break;
							case Cell.CELL_TYPE_NUMERIC:// 数字格式
								list.add(((int)cell.getNumericCellValue() + "")
										.trim());
								break;
							case Cell.CELL_TYPE_STRING:// 字符串格式
								list.add(cell.getStringCellValue().trim());
								break;
							default:
								list.add("");
								break;
							}
						} else {
							list.add("");
						}
					}
					// 将数据插入到mysql数据库中
					if (list.get(0) == null || list.get(0).equals("")) {
						break rows;
					}
					int k = 0;
					user.setUsername(list.get(k) == null ? "" : list.get(k++));
					user.setPassword(list.get(k) == null ? "" : list.get(k++));
					UserInfo userInfo = new UserInfo();
					userInfo.setName(list.get(k) == null ? "" : list.get(k++));
					userInfo.setSex(list.get(k) == null ? "" : list.get(k++));
					userInfo.setCollege(list.get(k) == null ? "" : list.get(k++));
					userInfo.setGrade(list.get(k) == null ? "" : list.get(k++));
					userInfo.setTeam(list.get(k) == null ? "" : list.get(k++));
					userInfo.setEmail(list.get(k) == null ? "" : list.get(k++));
					userInfo.setPhone(list.get(k) == null ? "" : list.get(k++));
					userInfo.setWriting(list.get(k++).equals("1") ? true : false);
					userInfo.setSpeaking(list.get(k++).equals("1") ? true : false);
					userInfo.setLab(list.get(k++).equals("1") ? true : false);
					userInfo.setType(list.get(k) == null ? "" : list.get(k++));
					userInfo.setUsername(user.getUsername());
					user.setUserInfo(userInfo);
					if (checkUsername(user.getUsername())) {
						HibernateUtils.save(user);
					} else {
						System.out.println("账号重复！");
					}
				}
			}
			return new Status("success", "添加成功!");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return new Status("error", i + "");
		} catch (IOException e) {
			e.printStackTrace();
			return new Status("error", i + "");
		} catch (Exception e) {
			e.printStackTrace();
			return new Status("error", i + "");
		}
	}

	private boolean checkUsername(String username) {
		boolean flag = false;
		Session session = null;
		try {
			session = HibernateUtils.openSession();
			String hql = "from User where username = ?";
			if (session.createQuery(hql).setString(0, username).uniqueResult() == null) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return flag;
	}
}