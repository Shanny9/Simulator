package report;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import com.model.TblService;
import com.model.TblSupplier;

public class ReportGenerator {
	static FileOutputStream fileOut;
	private static String courseName = "";

	public static void generateReports(String courseName, String fileName){
		
		ReportGenerator.courseName = courseName;
		
		ExcelGenerator excelGen = new ExcelGenerator();
		HSSFWorkbook wb = excelGen.getWb();
		setColors(excelGen);
		setStyles(excelGen);
		
		HSSFSheet sheet1 = generateSheet(excelGen, DataFactory.INCIDENTS_FLOW);
		alignMergedCells(sheet1);
		autoWidthColumns(sheet1);

		HSSFSheet sheet2 = generateSheet(excelGen,
				DataFactory.SERVICE_PRIORITIZATION);
		alignMergedCells(sheet2);
		autoWidthColumns(sheet2);
		
		HSSFSheet sheet3 = generateSheet(excelGen,
				DataFactory.SUPPLIER_PRICE_LIST);
		alignMergedCells(sheet3);
		autoWidthColumns(sheet3);
		
		HSSFSheet sheet4 = generateSheet(excelGen,
				DataFactory.SERVICE_EVENT_MAPPING);
		alignMergedCells(sheet4);
		autoWidthColumns(sheet4);

		export(wb, fileName);
	}

	public static void setColors(ExcelGenerator excelGen) {
		excelGen.addColor("nephritis", 39, 174, 96);
		excelGen.addColor("emerald", 46, 204, 113);
		excelGen.addColor("sunflower", 241, 196, 15);
		excelGen.addColor("orange", 243, 156, 18);
		excelGen.addColor("alizarin", 231, 76, 60);
		excelGen.addColor("pomegranate", 192, 57, 43);
		excelGen.addColor("peterRiver", 52, 152, 219);
	}

	public static void setStyles(ExcelGenerator excelGen) {
		HSSFWorkbook wb = excelGen.getWb();
		// title style
		CellStyle title_style;
		Font titleFont = wb.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 12);
		title_style = wb.createCellStyle();
		title_style.setAlignment(HorizontalAlignment.CENTER);
		title_style.setFont(titleFont);
		excelGen.addStyle("STYLE_TITLE", title_style);

		// set header style
		CellStyle header_style;
		Font headerFont = wb.createFont();
		headerFont.setFontHeightInPoints((short) 10);
		headerFont.setColor(HSSFColor.WHITE.index);
		headerFont.setBold(true);
		header_style = wb.createCellStyle();
		header_style.setAlignment(HorizontalAlignment.CENTER);
		header_style.setFillForegroundColor(HSSFColor.BLACK.index);
		header_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		header_style.setFont(headerFont);
		excelGen.addStyle("STYLE_HEADER", header_style);

		// set round style
		CellStyle round_style;
		round_style = wb.createCellStyle();
		round_style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		round_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		excelGen.addStyle("STYLE_ROUND", round_style);

		// set middle style
		CellStyle middle_align_style;
		middle_align_style = wb.createCellStyle();
		middle_align_style.setAlignment(HorizontalAlignment.CENTER);
		excelGen.addStyle("STYLE_MIDDLE_ALIGN", middle_align_style);

		// set left-middle style
		CellStyle left_middle_align_style;
		left_middle_align_style = wb.createCellStyle();
		left_middle_align_style.setAlignment(HorizontalAlignment.LEFT);
		excelGen.addStyle("STYLE_LEFT_MIDDLE_ALIGN", left_middle_align_style);

		// set center-middle style
		CellStyle center_middle_align_style;
		center_middle_align_style = wb.createCellStyle();
		center_middle_align_style.setAlignment(HorizontalAlignment.CENTER);
		center_middle_align_style
				.setVerticalAlignment(VerticalAlignment.CENTER);
		excelGen.addStyle("STYLE_CENTER_MIDDLE_ALIGN",
				center_middle_align_style);

		// set sys style
		CellStyle sys_style;
		sys_style = wb.createCellStyle();
		sys_style.setAlignment(HorizontalAlignment.LEFT);
		sys_style.setVerticalAlignment(VerticalAlignment.CENTER);
		sys_style.setFillForegroundColor(excelGen.colors.get("orange"));
		sys_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		excelGen.addStyle("STYLE_SYS", sys_style);

		generateSimpleStyle(excelGen, "STYLE_REP", "peterRiver");
		generateSimpleStyle(excelGen, "STYLE_LOW", "nephritis");
		generateSimpleStyle(excelGen, "STYLE_MEDIUM", "sunflower");
		generateSimpleStyle(excelGen, "STYLE_HIGH", "orange");
		generateSimpleStyle(excelGen, "STYLE_CRITICAL", "alizarin");
		generateSimpleStyle(excelGen, "STYLE_MAJOR", "pomegranate");
		generateSimpleStyle(excelGen, "STYLE_TRUE", "emerald");
		generateSimpleStyle(excelGen, "STYLE_FALSE", "alizarin");
	}

	private static void generateSimpleStyle(ExcelGenerator excelGen,
			String style, String color) {
		CellStyle low_style;
		low_style = excelGen.getWb().createCellStyle();
		low_style.setFillForegroundColor(excelGen.colors.get(color));
		low_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		excelGen.addStyle(style, low_style);
	}

	private static int mergeCount(int count, HSSFRow end_row, int col) {
		if (count > 1) {
			// merge
			int first_row_index = end_row.getRowNum() - count + 1;
			end_row.getSheet().addMergedRegion(
					new CellRangeAddress(first_row_index, end_row.getRowNum(),
							col, col));
			return 0;
		}
		return ++count;
	}

	private static <T> int mergeCountCondition(int count, T a, T b,
			HSSFRow end_row, int... col) {
		boolean isEqual = (a.getClass().isPrimitive()) ? a == b : a.equals(b);
		if (isEqual) {
			return ++count;
		} else {
			// merge
			if (count > 1) {
				for (int c = 0; c < col.length; c++) {
					int first_row_index = end_row.getRowNum() - count;
					end_row.getSheet().addMergedRegion(
							new CellRangeAddress(first_row_index, end_row
									.getRowNum() - 1, col[c], col[c]));
				}
			}
			return 1;
		}
	}

	private static HSSFSheet generateSheet(ExcelGenerator excelGen, int report) {
		switch (report) {
		case DataFactory.INCIDENTS_FLOW:
			return generateIncidentFlow(excelGen, "Incident Flow");
		case DataFactory.SERVICE_PRIORITIZATION:
			return generateServicePrioritization(excelGen,
					"Service Prioritization");
		case DataFactory.SUPPLIER_PRICE_LIST:
			return generateSupplierPriceList(excelGen, "Supplier Price List");
		case DataFactory.SERVICE_EVENT_MAPPING:
			return generateServiceEventsMapping(excelGen, "Service Event Mapping");
		default:
			return null;
		}
	}
	
	private static HSSFSheet generateServiceEventsMapping(
			ExcelGenerator excelGen, String sheetName) {
		HSSFWorkbook wb = excelGen.getWb();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// declare headers
		String[] headers = new String[] { "Service", "Error"};

		int row = 0;
		excelGen.createTitle(sheet.createRow(row++), 0, "STYLE_TITLE",
				sheetName, headers.length - 1);
		excelGen.createHeaders(sheet.createRow(row++), 0, "STYLE_HEADER",
				headers);

		List<Object> object_list = DataFactory.getInstance().getReportData(
				DataFactory.SERVICE_EVENT_MAPPING, courseName);
		
		for (Object obj : object_list) {
			TblService record = (TblService) obj;
			HSSFRow data_row = null;
			int col = 0;
			data_row = sheet.createRow(row++);

			// service name column
			excelGen.createCell(data_row, col++, record.getServiceName());

			// error column
			excelGen.createCell(data_row, col++, record.getEventId());
		}
		return sheet;
	}
	
	private static HSSFSheet generateSupplierPriceList(
			ExcelGenerator excelGen, String sheetName) {
		HSSFWorkbook wb = excelGen.getWb();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// declare headers
		String[] headers = new String[] { "#", "SUpplier", "Cost"};

		int row = 0;
		excelGen.createTitle(sheet.createRow(row++), 0, "STYLE_TITLE",
				sheetName, headers.length - 1);
		excelGen.createHeaders(sheet.createRow(row++), 0, "STYLE_HEADER",
				headers);

		List<Object> object_list = DataFactory.getInstance().getReportData(
				DataFactory.SUPPLIER_PRICE_LIST, courseName);
		
		int count = 0;
		for (Object obj : object_list) {
			TblSupplier record = (TblSupplier) obj;
			HSSFRow data_row = null;
			int col = 0;
			count++;
			data_row = sheet.createRow(row++);

			// # column
			excelGen.createCell(data_row, col++, count);

			// name column
			excelGen.createCell(data_row, col++, record.getSupplierName());

			// price column
			excelGen.createCell(data_row, col++, (int)record.getSolutionCost() + " " + record.getCurrency());
		}
		return sheet;
	}

	private static HSSFSheet generateServicePrioritization(
			ExcelGenerator excelGen, String sheetName) {
		HSSFWorkbook wb = excelGen.getWb();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// declare headers
		String[] headers = new String[] { "#", "Code", "Name", "Urgency",
				"Impact", "Priority" };

		int row = 0;
		excelGen.createTitle(sheet.createRow(row++), 0, "STYLE_TITLE",
				sheetName, headers.length - 1);
		excelGen.createHeaders(sheet.createRow(row++), 0, "STYLE_HEADER",
				headers);

		List<Object> object_list = DataFactory.getInstance().getReportData(
				DataFactory.SERVICE_PRIORITIZATION, courseName);

		int count = 0;
		for (Object obj : object_list) {
			ServiceRow record = (ServiceRow) obj;
			HSSFRow data_row = null;
			int col = 0;

			// headline
			if (count == 0) {
				data_row = sheet.createRow(row++);
				String title = "Business Services";
				excelGen.createTitle(data_row, 0, "STYLE_ROUND", title,
						headers.length - 1);
			} else if (count == ServiceRow.getNumOfBizServices() - 1) {
				data_row = sheet.createRow(row++);
				String title = "Business Services";
				excelGen.createTitle(data_row, 0, "STYLE_ROUND", title,
						headers.length - 1);
			}

			count++;
			data_row = sheet.createRow(row++);

			// id column
			excelGen.createCell(data_row, col++, record.getId(),"STYLE_MIDDLE_ALIGN");

			// code column
			excelGen.createCell(data_row, col++, record.getCode());

			// name column
			excelGen.createCell(data_row, col++, record.getName());

			// impact column
			excelGen.createCell(data_row, col++, record.getImpact());

			// urgency column
			excelGen.createCell(data_row, col++, record.getUrgency());

			// priority column
			excelGen.createCell(data_row, col++, record.getPriority());
		}
		return sheet;
	}

	private static HSSFSheet generateIncidentFlow(ExcelGenerator excelGen,
			String sheetName) {
		HSSFWorkbook wb = excelGen.getWb();
		HSSFSheet sheet = wb.createSheet(sheetName);

		// declare headers
		String[] headers = new String[] { "Session", "Time", "CI", "Error",
				"Service", "Note", "Q", "Marom", "Rakia", "Remark" };

		// setting note types
		IncidentRow.setNoteType(1, IncidentRow.NOTE_PRIORITY);
		IncidentRow.setNoteType(2, IncidentRow.NOTE_INCIDENT_TYPE);
		IncidentRow.setNoteType(3, IncidentRow.NOTE_SUPPLIER_PROFITABILITY);

		int row = 0;
		excelGen.createTitle(sheet.createRow(row++), 0, "STYLE_TITLE",
				sheetName, headers.length - 1);
		excelGen.createHeaders(sheet.createRow(row++), 0, "STYLE_HEADER",
				headers);

		List<Object> object_list = DataFactory.getInstance().getReportData(
				DataFactory.INCIDENTS_FLOW, courseName);

		int current_round = 0;
		IncidentRow first = (IncidentRow) object_list.get(0);
		String current_time = first.getTimeInRound();
		byte current_ci = first.getCiId();
		int incident_in_session = 0;
		int incident_with_same_time = 0;
		int incident_with_same_ci = 0;
		for (Object obj : object_list) {
			IncidentRow record = (IncidentRow) obj;
			HSSFRow data_row = null;
			int col = 0;

			// round # row
			if (record.getRound() > current_round) {
				data_row = sheet.createRow(row++);
				String title = "Round " + record.getRound();
				excelGen.createTitle(data_row, 0, "STYLE_ROUND", title,
						headers.length - 1);
				current_round++;
				incident_in_session = 0;
			}

			// session column
			data_row = sheet.createRow(row++);

			if (++incident_in_session == IncidentRow
					.getNumOfIncidentsInSession(record.getSession()).intValue()) {
				incident_in_session = mergeCount(incident_in_session, data_row,
						col);
			}
			excelGen.createCell(data_row, col++, record.getSessionInRound(),
					"STYLE_MIDDLE_ALIGN");

			// time column
			incident_with_same_time = mergeCountCondition(
					incident_with_same_time, record.getTimeInRound(),
					current_time, data_row, col);
			current_time = record.getTimeInRound();
			excelGen.createCell(data_row, col++, record.getTimeInRound(),
					"STYLE_MIDDLE_ALIGN");

			// ci name column
			incident_with_same_ci = mergeCountCondition(incident_with_same_ci,
					record.getCiId(), current_ci, data_row, col,
					Arrays.asList(headers).indexOf("Note"),
					Arrays.asList(headers).indexOf("Q"), Arrays.asList(headers)
							.indexOf("Marom"),
					Arrays.asList(headers).indexOf("Rakia"));
			current_ci = record.getCiId();
			excelGen.createCell(data_row, col++, record.getCiName());

			// error column
			excelGen.createCell(data_row, col++, record.getEventId(),
					"STYLE_MIDDLE_ALIGN");

			// service column
			excelGen.createCell(data_row, col++, record.getServiceCode());

			// note column
			excelGen.createCell(data_row, col++, record.getNote());

			// solution id column
			excelGen.createCell(data_row, col++, record.getSolutionId(),
					"STYLE_MIDDLE_ALIGN");

			// solution marom column
			excelGen.createCell(data_row, col++, record.getSolMarom(),
					"STYLE_MIDDLE_ALIGN");

			// solution marom column
			excelGen.createCell(data_row, col++, record.getSolRakia(),
					"STYLE_MIDDLE_ALIGN");
		}

		return sheet;
		// SheetConditionalFormatting sheetCF = sheet
		// .getSheetConditionalFormatting();
		//
		// ConditionalFormattingRule rule1 = sheetCF
		// .createConditionalFormattingRule(ComparisonOperator.EQUAL, "\""
		// + IncidentRow.NOTE_TRUE + "\"");
		//
		// PatternFormatting pattern1 = rule1.createPatternFormatting();
		// pattern1.setFillBackgroundColor(excelGen.colors.get("emerald"));
		//
		// ConditionalFormattingRule rule2 = sheetCF
		// .createConditionalFormattingRule(ComparisonOperator.EQUAL, "\""
		// + IncidentRow.NOTE_FALSE + "\"");
		// PatternFormatting pattern2 = rule2.createPatternFormatting();
		// pattern2.setFillBackgroundColor(excelGen.colors.get("alizarin"));
		//
		// ConditionalFormattingRule[] cfRules = { rule1, rule2 };
		//
		// CellRangeAddress[] regions = { CellRangeAddress.valueOf("$A1:$L1000")
		// };
		//
		// sheetCF.addConditionalFormatting(regions, cfRules);

		// auto-width columns
	}

	private static void alignMergedCells(HSSFSheet sheet) {
		List<CellRangeAddress> merged = sheet.getMergedRegions();
		for (CellRangeAddress range : merged) {
			int rowIndex = range.getFirstRow();
			int cellnum = range.getFirstColumn();
			CellStyle style = sheet.getRow(rowIndex).getCell(cellnum)
					.getCellStyle();
			if (style == null) {
				style = sheet.getWorkbook().createCellStyle();
			}
			style.setVerticalAlignment(VerticalAlignment.CENTER);
		}
	}

	private static void autoWidthColumns(HSSFSheet sheet) {
		for (int col = 0; col < sheet.getRow(1).getLastCellNum(); col++) {
			sheet.autoSizeColumn(col);
		}
	}

	private static void export(HSSFWorkbook wb, String fileName) {

		try {
			fileOut = new FileOutputStream(fileName);
			wb.write(fileOut);
			wb.close();
			fileOut.close();

//			Desktop.getDesktop().open(new File(fileName + ".xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
