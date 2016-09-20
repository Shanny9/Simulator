package utils;

public class DepartmentService {
	/**
	 * The name of the business unit
	 */
	private String departmentName;
	/**
	 * The id of the service
	 */
	private byte serviceId;
	/**
	 * The name of the service
	 */
	private String serviceName;
	/**
	 * The percentage of the business unit in the service
	 */
	private double percentage;
	/**
	 * The expense of the service
	 */
	private double serviceExpense = 0.d;

	/**
	 * Empty Constructor
	 */
	public DepartmentService() {
		super();
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String bizUnitName) {
		this.departmentName = bizUnitName;
	}

	public byte getServiceId() {
		return serviceId;
	}

	public void setserviceId(byte serviceId) {
		this.serviceId = serviceId;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setserviceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getserviceExpense() {
		return serviceExpense;
	}

	public void addServiceExpense(double serviceGain) {
		this.serviceExpense += serviceGain;
	}

	public double getExpense() {
		return percentage * serviceExpense;
	}

	public String toString() {
		return departmentName + "-" + serviceName;
	}
}
