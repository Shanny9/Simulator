package log;

import java.io.Serializable;
import java.util.ArrayList;

class CIItem implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private int id;

	private boolean isUp;

	/**
	 * @param id
	 * @param time
	 */
	CIItem(int id) {
		super();
		this.id = id;
		this.isUp = true;
	}
	
	void update(){
		isUp = !isUp;
	}
	
	boolean isUp(){
		return isUp;
	}
}
