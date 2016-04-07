package com.dao;

import com.model.TblGeneral_parameter;

public interface TblGeneralParametersDao {
	
	public void addGeneralParameters(TblGeneral_parameter generalParameter);
	public void deleteGeneralParameters(int pk);
	public void updateGeneralParameters(TblGeneral_parameter generalParameter);
	public TblGeneral_parameter getGeneralParameters();
	public int getSessionTime();
	public int getRoundTime();
}
