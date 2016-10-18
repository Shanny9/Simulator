package report;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.util.CellRangeAddress;

public class ExcelGenerator {
	protected HSSFWorkbook wb;
	protected HashMap<String, CellStyle> styles;
	protected HashMap<String, Short> colors;
	private static short indexColor = 56;
	protected HSSFPalette palette;

	public ExcelGenerator() {
		wb = new HSSFWorkbook();
		styles = new HashMap<>();
		colors = new HashMap<>();
		palette = ((HSSFWorkbook) wb).getCustomPalette();
	}

	/**
	 * method can be overwritten to create additional styles
	 */

	public HSSFWorkbook getWb() {
		return wb;
	}

	protected CellStyle style(String name) {
		return styles.get(name);
	}

	protected HSSFCell createCell(HSSFRow row, int column, Object value,
			String style) {
		HSSFCell cell = createCell(row, column, value);
		if (style != null)
			cell.setCellStyle(styles.get(style));
		return cell;
	}
	
	protected HSSFCell getCell(HSSFRow row, int column){
		return row.getCell(column);
	}

	protected HSSFCell createCell(HSSFRow row, int column, Object value) {
		HSSFCell cell = row.createCell(column);
		if (value instanceof String)
			cell.setCellValue((String) value);
		else if (value instanceof Enum)
			cell.setCellValue(((Enum) value).name());
		else if (value instanceof Calendar)
			cell.setCellValue((Calendar) value);
		else if (value instanceof Date)
			// do not forget to set a cellStyle with a dataFormat
			cell.setCellValue((Date) value);
		else if (value instanceof Number)
			cell.setCellValue(((Number) value).doubleValue());
		else if (value instanceof Boolean)
			cell.setCellValue((Boolean) value);
		else if (value instanceof RichTextString) {
			cell.setCellValue((RichTextString) value);
		} else if (value != null)
			throw new RuntimeException("Unsupported cell value " + value);
		return cell;
	}

	protected void createHeaders(HSSFRow row, int column, String style,
			String... headers) {
		int i = 0;
		for (String header : headers) {
			createCell(row, column + i, header, style);
			i++;
		}
	}

	protected void createTitle(HSSFRow row, int column, String style,
			String title, int length) {
		createCell(row, column, title, style);
		row.getSheet().addMergedRegion(
				new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0,
						length));
	}

	protected void addStyle(String name, CellStyle style) {
		styles.put(name, style);
	}

	public void addColor(String name, int red, int green, int blue) {
		if (red >= 0 && red < 256 && green >= 0 && green < 256 && blue >= 0
				&& blue < 256) {
			colors.put(name, ++indexColor);
			palette.setColorAtIndex(indexColor, (byte) red, (byte) green,
					(byte) blue);

		}
	}
}