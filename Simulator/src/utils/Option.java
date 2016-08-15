package utils;

public class Option {
	
	private String DisplayText;
	private String Value;
	
	/**
	 * @param displayText
	 * @param value
	 */
	public Option(String displayText, String value) {
		super();
		DisplayText = displayText;
		Value = value;
	}
	/**
	 * @return the displayText
	 */
	public String getDisplayText() {
		return DisplayText;
	}
	/**
	 * @param displayText the displayText to set
	 */
	public void setDisplayText(String displayText) {
		DisplayText = displayText;
	}
	/**
	 * @return the value
	 */
	public String getValue() {
		return Value;
	}
	/**
	 * @param value the value to set
	 */
	public void setValue(String value) {
		Value = value;
	}
}
