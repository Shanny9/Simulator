package com.dao;

import java.sql.SQLException;
import java.util.List;

import com.model.TblCurrency;

public interface TblCurrencyDao {
	
	public void addCurrency(TblCurrency cur) throws SQLException;
	public void deleteCurrency(String name) throws SQLException;
	public List<TblCurrency> getAllCurrencies(int startPageIndex, int recordsPerPage);
	public List<TblCurrency> getAllCurrencies();
	public List<TblCurrency> getAllActiveCurrencies();
	public TblCurrency getCurrencyById(String name);
	public int getCurrencyCount();
	void updateCurrency(TblCurrency cur, String name) throws SQLException;
}
