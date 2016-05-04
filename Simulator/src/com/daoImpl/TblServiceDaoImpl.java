package com.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.dao.TblServiceDao;
import com.jdbc.DBUtility;
import com.model.TblService;
import com.model.TblSupplier;

public class TblServiceDaoImpl implements TblServiceDao{
	
	private Connection dbConnection;
	private PreparedStatement pStmt;

	public TblServiceDaoImpl() {
		dbConnection = DBUtility.getConnection();
	}
	
	@Override
	public void addService(TblService service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteService(String name) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateService(TblService service) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<TblService> getAllServices(int startPageIndex, int recordsPerPage) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TblService> getAllServices() {
		List<TblService> services = new ArrayList<TblService>();

		String query = "SELECT * FROM tblService";

		try {
			Statement stmt = dbConnection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			while (rs.next()) {
				TblService service = new TblService();

				service.setFixedCost(rs.getDouble("fixed_cost"));
				service.setFixedIncome(rs.getDouble("fixed_income"));
				service.setImpact(rs.getString("impact"));
				service.setIsActive(rs.getByte("isActive"));
				service.setIsTechnical(rs.getByte("isTechnical"));
				service.setServiceId(rs.getByte("service_id"));
				service.setServiceCode(rs.getString("service_code"));
				service.setServiceName(rs.getString("service_name"));
				service.setSupplierLevel2(rs.getString("supplier_level2"));
				service.setSupplierLevel3(rs.getString("supplier_level3"));
				service.setUrgency(rs.getString("urgency"));
				services.add(service);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return services;
	}
	
	@Override
	public TblService getServiceById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getServiceCount() {
		// TODO Auto-generated method stub
		return 0;
	}
}
