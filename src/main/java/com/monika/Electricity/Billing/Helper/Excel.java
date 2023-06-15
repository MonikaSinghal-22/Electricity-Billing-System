package com.monika.Electricity.Billing.Helper;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import com.monika.Electricity.Billing.System.Entity.Customers;
import com.monika.Electricity.Billing.System.Entity.Meters;
import com.monika.Electricity.Billing.System.Entity.Users;

public class Excel {
	
	public static boolean checkExcelFormat(MultipartFile file) {
		String contentType = file.getContentType();
		if(contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public static List<Customers> convertExcelToListOfCustomers(InputStream is){
		List<Customers> list = new ArrayList<>();
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(is);
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			if(sheet == null) {
				return list;
			}
			
			int rowNumber = 0;
			Iterator<Row> iterator = sheet.iterator();
			
			while(iterator.hasNext()) {
				Row row = iterator.next();
				if(rowNumber == 0) {
					rowNumber++;
					continue;
				}
				Iterator<Cell> cells = row.iterator();
				int cid = 0;
				Customers cust = new Customers();
				Users user = new Users();
				Meters meter = new Meters();
				DataFormatter fmt = new DataFormatter();
				while(cells.hasNext()) {
					Cell cell = cells.next();
					String s = fmt.formatCellValue(cell);
					switch(cid) {
						case 0:
							cust.setName(cell.getStringCellValue());
							user.setName(cell.getStringCellValue());
							break;
						case 1:
							cust.setAddress(cell.getStringCellValue());
							break;
						case 2:
							cust.setCity(cell.getStringCellValue());
							break;
						case 3:
							cust.setState(cell.getStringCellValue());
							break;
						case 4:
							cust.setEmail(cell.getStringCellValue());
							break;
						case 5:
							cust.setPhone(s);
							break;
						case 6:
							user.setUsername(cell.getStringCellValue());
							break;
						case 7:
							user.setPassword(cell.getStringCellValue());
							break;
						case 8:
							meter.setMeterNo(s);
							break;
						case 9:
							meter.setMeterLocation(cell.getStringCellValue());
							break;
						case 10:
							meter.setMeterType(cell.getStringCellValue());
							break;
						case 11:
							meter.setPhaseCode(s);
							break;
						case 12:
							meter.setBillType(cell.getStringCellValue());
							break;
						default:
							break;
					}
					cid++;
				}
				cust.setMeter(meter);
				cust.setUser(user);
				list.add(cust);
			}
				
		
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return list;
	}
	
}
