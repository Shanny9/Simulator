package report;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import log.FilesUtils;
import utils.Queries;

import com.daoImpl.TblSupplierDaoImpl;
import com.jdbc.DBUtility;
import com.model.TblIncidentPK;

public class DataFactory {
	
	// maps reports to their queries
	private static HashMap<Integer, String> report_queries = new HashMap<>();
	
	// report names
	public final static int INCIDENTS_FLOW = 1;
	public final static int SERVICE_PRIORITIZATION = 2;
	public final static int CHANGE_FLOW = 3;
	public final static int CHANGE_SPECIFICATION = 4;
	public final static int CHANGE_WORKPLAN = 5;
	public final static int CHANGE_DETAILS = 6;
	public final static int SUPPLIER_PRICE_LIST = 7;
	public final static int SERVICE_EVENT_MAPPING = 8;
	
	private static DataFactory instance;

	DataFactory() {
		report_queries.put(INCIDENTS_FLOW, Queries.incidents_flow);
		report_queries.put(SERVICE_PRIORITIZATION, Queries.service_prioritization);
		report_queries.put(CHANGE_FLOW, "");
		report_queries.put(CHANGE_SPECIFICATION, "");
		report_queries.put(CHANGE_WORKPLAN, "");
		report_queries.put(CHANGE_DETAILS, "");
		report_queries.put(SUPPLIER_PRICE_LIST, "select * FROM tblSupplier");
		report_queries.put(SERVICE_EVENT_MAPPING,
				"select service_name, event_id from tblService");
	}
	
	public static DataFactory getInstance(){
		if (instance == null){
			instance = new DataFactory();
		}
		return instance;
	}

	public List<Object> getReportData(int report, String courseName) {
		
		FilesUtils.openSettings(courseName);
		
		ResultSet rs = null;
		try {
			Statement stmt = DBUtility.getConnection().createStatement();
			rs = stmt.executeQuery(report_queries.get(report));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (rs == null) {
			return null;
		}

		List<Object> rows = new ArrayList<Object>();
		switch (report) {
		case INCIDENTS_FLOW:
			try {
				while (rs.next()) {
					TblIncidentPK inc_pk = new TblIncidentPK();
					inc_pk.setCiId(rs.getByte("ci_id"));
					inc_pk.setTime(rs.getInt("time"));
					String ci_name = rs.getString("ci_name");
					int event = rs.getInt("event_id");
					String priority = rs.getString("priorityName");
					double priority_cost = rs.getDouble("pCost");
					double fixed_income = rs.getDouble("fixed_income");
					String service_code = rs.getString("service_code");
					byte service_id = rs.getByte("service_id");
					double sol_cost = rs.getDouble("solution_cost");
					int sol_id = rs.getInt("solution_id");
					int sol_marom = rs.getInt("solution_marom");
					int sol_rakia = rs.getInt("solution_rakia");
					boolean isSystematic = rs.getBoolean("isSystematic");

					IncidentRow ir = new IncidentRow(event, inc_pk, ci_name,
							service_id, service_code, sol_id, sol_marom, sol_rakia,
							priority, isSystematic, sol_cost, priority_cost,
							fixed_income);
					rows.add(ir);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case SERVICE_PRIORITIZATION:
			try {
				while (rs.next()){
					byte id = rs.getByte("service_id");
					String code = rs.getString("service_code");
					String name = rs.getString("service_name");
					String urgency = rs.getString("urgency");
					String impact = rs.getString("impact");
					String priority = rs.getString("priorityName");
					boolean isTechnical = rs.getBoolean("isTechnical");
					
					ServiceRow sr = new ServiceRow(id, code, name, urgency, impact, priority, isTechnical);
					rows.add(sr);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case SUPPLIER_PRICE_LIST:
			rows.addAll(new TblSupplierDaoImpl().getAllActiveSuppliers());
		}
		return rows;
	}
}
