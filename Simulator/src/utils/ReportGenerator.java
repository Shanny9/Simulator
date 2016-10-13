package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import log.FilesUtils;

import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;

import com.jdbc.DBUtility;
import com.model.TblIncidentPK;

public class ReportGenerator {

	static short indexColor = 56;
	static final Workbook wb = new HSSFWorkbook();
	static final Sheet sheet = wb.createSheet("new sheet");
	static HSSFPalette palette = ((HSSFWorkbook) wb).getCustomPalette();
	static Map<String, CellStyle> styleRegistry = new HashMap<>();

	// headline style names
	static final String TITLE_STYLE = "TITLE_STYLE";
	static final String HEADER_STYLE = "HEADER_STYLE";
	static final String ROUND_STYLE = "ROUND_STYLE";

	// priority style names
	static final String LOW_STYLE = "LOW_STYLE";
	static final String MEDIUM_STYLE = "MEDIUM_STYLE";
	static final String HIGH_STYLE = "HIGH_STYLE";
	static final String MAJOR_STYLE = "MAJOR_STYLE";
	static final String CRITICAL_STYLE = "CRITICAL_STYLE";

	// incident type style name
	static final String SYS_STYLE = "SYS_STYLE";
	static final String REP_STYLE = "REP_STYLE";

	// boolean style names
	static final String TRUE_STYLE = "TRUE_STYLE";
	static final String FALSE_STYLE = "FALSE_STYLE";

	static final String MIDDLE_ALIGN_STYLE = "MIDDLE_ALIGN_STYLE";
	static final String LEFT_ALIGN_STYLE = "LEFT_ALIGN_STYLE";
	static final String LEFT_MIDDLE_ALIGN_STYLE = "LEFT_ALIGN_STYLE";
	static final String CENTER_MIDDLE_ALIGN_STYLE = "CENTER_MIDDLE_ALIGN_STYLE";

	// set color palette (max custom colors = 7)
	final static short nephritis = registerColor(39, 174, 96);
	final static short emerald = registerColor(46, 204, 113);
	final static short sunflower = registerColor(241, 196, 15);
	final static short orange = registerColor(243, 156, 18);
	final static short alizarin = registerColor(231, 76, 60);
	final static short pomegranate = registerColor(192, 57, 43);
	final static short peterRiver = registerColor(52, 152, 219);

	public static void generateTable(String courseName) {
		List<IncidentRow> incidents = generateIncidentsList(courseName);
		generateStyles();
		exportToExcel(incidents);
	}

	private static List<IncidentRow> generateIncidentsList(String courseName) {
		IncidentRow.setCourseName(courseName);
		List<IncidentRow> incidents = new ArrayList<>();

		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(Queries.report_data);
			while (rs.next()) {
				IncidentRow ir = new IncidentRow();
				TblIncidentPK inc = new TblIncidentPK();
				inc.setCiId(rs.getByte("ci_id"));
				inc.setTime(rs.getInt("time"));
				ir.setIncident(inc, rs.getDouble("pCost"),
						rs.getDouble("fixed_income"));
				ir.setCiName(rs.getString("ci_name"));
				ir.setEvent_id(rs.getInt("event_id"));
				ir.setPriority(rs.getString("priorityName"));
				ir.setService_code(rs.getString("service_code"));
				ir.setService_id(rs.getByte("service_id"));
				ir.setSol_cost(rs.getDouble("solution_cost"));
				ir.setSolutionId(rs.getInt("solution_id"));
				ir.setSol_marom(rs.getInt("solution_marom"));
				ir.setSol_rakia(rs.getInt("solution_rakia"));
				ir.setSystematic(rs.getBoolean("isSystematic"));
				incidents.add(ir);
			}
			Collections.sort(incidents);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return incidents;
	}

	private static void exportToExcel(List<IncidentRow> incidents) {

		// declare headers
		String[] headers = new String[] { "Session", "Time", "Error",
				"CI Name", "ID", "Service", "Note", "Q", "Marom", "Rakia",
				"Remark" };

		int row = 0;
		// set title row
		Row title_row = sheet.createRow((short) row);
		Cell title_cell = title_row.createCell(0);
		title_cell.setCellValue("Incident Flow Table");
		sheet.addMergedRegion(new CellRangeAddress(row, row, 0,
				headers.length - 1));
		title_cell.setCellStyle((CellStyle) styleRegistry.get(TITLE_STYLE));

		// set header row
		Row header_row = sheet.createRow((short) ++row);
		for (int col = 0; col < headers.length; col++) {
			Cell header_cell = header_row.createCell(col);
			header_cell.setCellValue(headers[col]);
			header_cell.setCellStyle((CellStyle) styleRegistry
					.get(HEADER_STYLE));
		}

		int current_round = 0;
		String current_time = incidents.get(0).getTimeInRound();
		String current_ci = incidents.get(0).getCiName();
		int incident_in_session = 0;
		int incident_with_same_time = 0;
		int incident_with_same_ci = 0;
		for (IncidentRow record : incidents) {
			Row data_row = sheet.createRow((short) ++row);
			int col = 0;

			// round # row
			if (record.getRound() > current_round) {
				Cell c = data_row.createCell(col);
				c.setCellValue("Round " + record.getRound());
				sheet.addMergedRegion(new CellRangeAddress(row, row, col,
						headers.length - 1));
				data_row = sheet.createRow((short) ++row);
				c.setCellStyle((CellStyle) styleRegistry.get(ROUND_STYLE));
				current_round++;
				incident_in_session = 0;
			}

			// session column
			if (++incident_in_session == IncidentRow
					.getNumOfIncidentsInSession(record.getSession()).intValue()) {
				sheet.addMergedRegion(new CellRangeAddress(row
						- incident_in_session + 1, row, col, col));

				incident_in_session = 0;
			}
			Cell sessionCell = data_row.createCell(col++);
			sessionCell.setCellValue(record.getSessionInRound());
			sessionCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// time column
			if (record.getTimeInRound().equals(current_time)) {
				incident_with_same_time++;
			} else {
				if (incident_with_same_time > 1) {
					sheet.addMergedRegion(new CellRangeAddress(row
							- incident_with_same_time - 1, row - 1, col, col));

				}
				current_time = record.getTimeInRound();
				incident_with_same_time = 0;
			}
			Cell timeCell = data_row.createCell(col++);
			timeCell.setCellValue(record.getTimeInRound());
			timeCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// event id column
			Cell eventCell = data_row.createCell(col++);
			eventCell.setCellValue(record.getEvent_id());
			eventCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// ci name column
			if (record.getCiName().equals(current_ci)) {
				incident_with_same_ci++;
			} else {
				if (incident_with_same_ci > 1) {
					int start_row_index = row - incident_with_same_ci - 1;
					Row startRow = sheet.getRow(start_row_index);

					// merge ci name column
					sheet.addMergedRegion(new CellRangeAddress(start_row_index,
							row - 1, col, col));
					startRow.getCell(col).getCellStyle()
							.setVerticalAlignment(VerticalAlignment.CENTER);
					// merge note column
					int note_col = Arrays.asList(headers).indexOf("Note");
					sheet.addMergedRegion(new CellRangeAddress(start_row_index,
							row - 1, note_col, note_col));
					startRow.getCell(note_col).getCellStyle()
							.setVerticalAlignment(VerticalAlignment.CENTER);
					// merge sol id column
					int sol_id_col = Arrays.asList(headers).indexOf("Q");
					sheet.addMergedRegion(new CellRangeAddress(start_row_index,
							row - 1, sol_id_col, sol_id_col));
					startRow.getCell(sol_id_col).getCellStyle()
							.setVerticalAlignment(VerticalAlignment.CENTER);
					// merge sol marom column
					int marom_col = Arrays.asList(headers).indexOf("Marom");
					sheet.addMergedRegion(new CellRangeAddress(start_row_index,
							row - 1, marom_col, marom_col));
					startRow.getCell(marom_col).getCellStyle()
							.setVerticalAlignment(VerticalAlignment.CENTER);
					// merge sol rakia column
					int rakia_col = Arrays.asList(headers).indexOf("Rakia");
					sheet.addMergedRegion(new CellRangeAddress(start_row_index,
							row - 1, rakia_col, rakia_col));
					startRow.getCell(rakia_col).getCellStyle()
							.setVerticalAlignment(VerticalAlignment.CENTER);
				}
				current_ci = record.getCiName();
				incident_with_same_ci = 0;
			}
			data_row.createCell(col++).setCellValue(record.getCiName());

			// service id column
			Cell ServiceIdCell = data_row.createCell(col++);
			ServiceIdCell.setCellValue(record.getService_id());
			ServiceIdCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// service code column
			data_row.createCell(col++).setCellValue(record.getService_code());

			// note column
			Cell noteCell = data_row.createCell(col++);
			noteCell.setCellValue(record.getNote());
			String style = "";
			switch (record.getNote()) {
			case "True":
				style = TRUE_STYLE;
				break;
			case "False":
				style = FALSE_STYLE;
				break;
			case "1-Low":
				style = LOW_STYLE;
				break;
			case "2-Medium":
				style = MEDIUM_STYLE;
				break;
			case "3-High":
				style = HIGH_STYLE;
				break;
			case "4-Critical":
				style = CRITICAL_STYLE;
				break;
			case "5-Major":
				style = MAJOR_STYLE;
				break;
			case "Sys":
				style = SYS_STYLE;
				break;
			case "Rep":
				style = REP_STYLE;
				break;
			default:
			}
			if (!style.isEmpty()) {
				noteCell.setCellStyle((CellStyle) styleRegistry.get(style));
			}

			// solution id column
			Cell solIdCell = data_row.createCell(col++);
			solIdCell.setCellValue(record.getSolutionId());
			solIdCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// solution marom column
			Cell maromCell = data_row.createCell(col++);
			maromCell.setCellValue(record.getSol_marom());
			maromCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			// solution marom column
			Cell rakiaCell = data_row.createCell(col++);
			rakiaCell.setCellValue(record.getSol_rakia());
			rakiaCell.setCellStyle((CellStyle) styleRegistry
					.get(MIDDLE_ALIGN_STYLE));

			data_row.createCell(col++).setCellValue("");
		}

		for (int col = 0; col < headers.length; col++) {
			sheet.autoSizeColumn(col);
		}
		FileOutputStream fileOut;
		try {
			fileOut = new FileOutputStream("workbook.xls");
			wb.write(fileOut);
			wb.close();
			fileOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void generateStyles() {
		// set title style
		CellStyle title_style;
		Font titleFont = wb.createFont();
		titleFont.setBold(true);
		titleFont.setFontHeightInPoints((short) 12);
		title_style = wb.createCellStyle();
		title_style.setAlignment(HorizontalAlignment.CENTER);
		title_style.setFont(titleFont);
		styleRegistry.put(TITLE_STYLE, title_style);

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
		styleRegistry.put(HEADER_STYLE, header_style);

		// set round style
		CellStyle round_style;
		round_style = wb.createCellStyle();
		round_style.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		round_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleRegistry.put(ROUND_STYLE, round_style);

		// set middle style
		CellStyle middle_align_style;
		middle_align_style = wb.createCellStyle();
		middle_align_style.setAlignment(HorizontalAlignment.CENTER);
		middle_align_style.setVerticalAlignment(VerticalAlignment.CENTER);
		styleRegistry.put(MIDDLE_ALIGN_STYLE, middle_align_style);

		// set left-middle style
		CellStyle left_middle_align_style;
		left_middle_align_style = wb.createCellStyle();
		left_middle_align_style.setAlignment(HorizontalAlignment.LEFT);
		left_middle_align_style.setVerticalAlignment(VerticalAlignment.CENTER);
		styleRegistry.put(LEFT_MIDDLE_ALIGN_STYLE, left_middle_align_style);

		// set center-middle style
		CellStyle center_middle_align_style;
		center_middle_align_style = wb.createCellStyle();
		center_middle_align_style.setAlignment(HorizontalAlignment.CENTER);
		center_middle_align_style
				.setVerticalAlignment(VerticalAlignment.CENTER);
		styleRegistry.put(CENTER_MIDDLE_ALIGN_STYLE, center_middle_align_style);

		// set sys style
		CellStyle sys_style;
		sys_style = wb.createCellStyle();
		sys_style.setAlignment(HorizontalAlignment.LEFT);
		sys_style.setVerticalAlignment(VerticalAlignment.CENTER);
		sys_style.setFillForegroundColor(orange);
		sys_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleRegistry.put(SYS_STYLE, sys_style);

		generateSimpleStyle(REP_STYLE, peterRiver);
		generateSimpleStyle(LOW_STYLE, nephritis);
		generateSimpleStyle(MEDIUM_STYLE, sunflower);
		generateSimpleStyle(HIGH_STYLE, orange);
		generateSimpleStyle(CRITICAL_STYLE, alizarin);
		generateSimpleStyle(MAJOR_STYLE, pomegranate);
		generateSimpleStyle(TRUE_STYLE, emerald);
		generateSimpleStyle(FALSE_STYLE, alizarin);
	}

	private static void generateSimpleStyle(String style_name, short color_index) {
		CellStyle low_style;
		low_style = wb.createCellStyle();
		low_style.setFillForegroundColor(color_index);
		low_style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		styleRegistry.put(style_name, low_style);
	}

	private final static short registerColor(int red, int green, int blue) {
		if (red >= 0 && red < 256 && green >= 0 && green < 256 && blue >= 0
				&& blue < 256) {
			indexColor++;
			if (wb instanceof HSSFWorkbook) {
				palette.setColorAtIndex(indexColor, (byte) red, (byte) green,
						(byte) blue);
			}
			return indexColor;
		}
		return 0;
	}

	static class IncidentRow implements Comparable<IncidentRow> {

		private static int rows = 0;
		private static HashSet<Byte> past_incidents = new HashSet<>();
		private static HashMap<Integer, Integer> incidents_in_session = new HashMap<>();
		private static HashMap<TblIncidentPK, Double> incident_max_downtime_costs = new HashMap<>();

		private int row;
		private int event_id;
		private TblIncidentPK incident;
		private String ci_name;
		private byte service_id;
		private String service_code;
		private int sol_id;
		private int sol_marom;
		private int sol_rakia;
		private String priority;
		private boolean isSystematic;
		private boolean isRepeating;
		private double sol_cost;

		IncidentRow() {
			this.row = ++rows;
		}

		public static void setCourseName(String courseName) {
			FilesUtils.openSettings(courseName);
		}

		public static Integer getNumOfIncidentsInSession(int session) {
			return incidents_in_session.get(session);
		}

		/**
		 * @return the row_num
		 */
		public int getRow() {
			return row;
		}

		/**
		 * @return the time
		 */
		public SimulationTime getSimulationTime() {
			return incident.getSimulationTime();
		}

		public String getTimeInRound() {
			int time = incident.getSimulationTime().getRunTimeInRound();
			int hours = time / 3600;
			int minutes = (time % 3600) / 60;
			int seconds = time % 60;
			return String.format("%02d:%02d:%02d", hours, minutes, seconds);
		}

		/**
		 * @return the event_id
		 */
		public int getEvent_id() {
			return event_id;
		}

		/**
		 * @param event_id
		 *            the event_id to set
		 */
		public void setEvent_id(int event_id) {
			this.event_id = event_id;
		}

		/**
		 * @return the ci_id
		 */
		public byte getCi_id() {
			return incident.getCiId();
		}

		/**
		 * @param incident
		 *            the incident to set
		 */
		public void setIncident(TblIncidentPK incident, double priorityCost,
				double fixed_income) {
			this.incident = incident;
			this.isRepeating = IncidentRow.past_incidents.contains(incident
					.getCiId());
			double max_downtime_cost = (priorityCost + fixed_income)
					* incident.getSimulationTime().getTimeUntilSessionEnds();

			Double sum_downtime_cost = IncidentRow.incident_max_downtime_costs
					.get(incident);
			if (sum_downtime_cost == null) {
				sum_downtime_cost = 0.d;
			}
			sum_downtime_cost += max_downtime_cost;
			IncidentRow.incident_max_downtime_costs.put(incident,
					sum_downtime_cost);
			IncidentRow.past_incidents.add(incident.getCiId());
			int current_session = incident.getSimulationTime().getSession();
			Integer count = incidents_in_session.get(current_session);
			if (count == null) {
				count = 0;
			}
			count++;
			incidents_in_session.put(current_session, count);
		}

		/**
		 * @return the service_id
		 */
		public byte getService_id() {
			return service_id;
		}

		/**
		 * @param service_id
		 *            the service_id to set
		 */
		public void setService_id(byte service_id) {
			this.service_id = service_id;
		}

		/**
		 * @return the service_code
		 */
		public String getService_code() {
			return service_code;
		}

		/**
		 * @param service_code
		 *            the service_code to set
		 */
		public void setService_code(String service_code) {
			this.service_code = service_code;
		}

		/**
		 * @return the ci_code
		 */
		public String getCiName() {
			return ci_name;
		}

		/**
		 * @param ci_name
		 *            the ci_name to set
		 */
		public void setCiName(String ci_name) {
			this.ci_name = ci_name;
		}

		/**
		 * @return the sol_id
		 */
		public int getSolutionId() {
			return this.sol_id;
		}

		public void setSolutionId(int sol_id) {
			this.sol_id = sol_id;
		}

		/**
		 * @return the sol_marom
		 */
		public int getSol_marom() {
			return sol_marom;
		}

		/**
		 * @param sol_marom
		 *            the sol_marom to set
		 */
		public void setSol_marom(int sol_marom) {
			this.sol_marom = sol_marom;
		}

		/**
		 * @return the sol_rakia
		 */
		public int getSol_rakia() {
			return sol_rakia;
		}

		/**
		 * @param sol_rakia
		 *            the sol_rakia to set
		 */
		public void setSol_rakia(int sol_rakia) {
			this.sol_rakia = sol_rakia;
		}

		/**
		 * @return the priority
		 */
		public String getPriority() {
			return priority;
		}

		/**
		 * @param priority
		 *            the priority to set
		 */
		public void setPriority(String priority) {
			this.priority = priority;
		}

		/**
		 * @return the note
		 */
		public String getNote() {
			switch (getRound()) {
			case 1:
				String p = "";
				switch (this.priority) {
				case "Low":
					p = "1";
					break;
				case "Medium":
					p = "2";
					break;
				case "High":
					p = "3";
					break;
				case "Critical":
					p = "4";
					break;
				case "Major":
					p = "5";
					break;
				}
				return p + "-" + this.priority;
			case 2:
				String res = "";
				if (isSystematic) {
					res += "Sys";
				} else if (isRepeating) {
					res += (res.isEmpty()) ? "Rep" : ", Rep";
				}
				return res;
			case 3:
				return (isWorthBuy()) ? "True" : "False";
			default:
				return "";
			}
		}

		/**
		 * @return the round
		 */
		public int getRound() {
			return incident.getSimulationTime().getRound();
		}

		/**
		 * @return the session
		 */
		public int getSessionInRound() {
			return incident.getSimulationTime().getSessionInRound();
		}

		public int getSession() {
			return incident.getSimulationTime().getSession();
		}

		/**
		 * @param sol_cost
		 *            the sol_cost to set
		 */
		public void setSol_cost(double sol_cost) {
			this.sol_cost = sol_cost;
		}

		/**
		 * @return the isWorthBuy
		 */
		public boolean isWorthBuy() {
			return this.sol_cost < incident_max_downtime_costs.get(incident);
		}

		/**
		 * @return the isSystematic
		 */
		public boolean isSystematic() {
			return this.isSystematic;
		}

		/**
		 * @return isSystematic the isSystematic to set
		 */
		public void setSystematic(boolean isSystematic) {
			this.isSystematic = isSystematic;
		}

		@Override
		public int compareTo(IncidentRow o) {
			return (getSimulationTime().before(o.getSimulationTime())) ? 1 : 0;

		}
	}
}
