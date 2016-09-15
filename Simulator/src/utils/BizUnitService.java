package utils;

public class BizUnitService {
	/**
	 * The name of the business unit
	 */
	private String bizUnitName;
	/**
	 * The id of the service
	 */
	private byte serviceId;
	/**
	 * The percentage of the business unit in the service
	 */
	private double percentage;
	/**
	 * The gain (or loss) of the service
	 */
	private double serviceGain = 0.d;

	/**
	 * Empty Constructor
	 */
	public BizUnitService() {
		super();
	}

	public String getBizUnitName() {
		return bizUnitName;
	}

	public void setBizUnitName(String bizUnitName) {
		this.bizUnitName = bizUnitName;
	}

	public byte getServiceId() {
		return serviceId;
	}

	public void setserviceId(byte serviceId) {
		this.serviceId = serviceId;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getserviceGain() {
		return serviceGain;
	}

	public void addServiceGain(double serviceGain) {
		this.serviceGain += serviceGain;
	}
	
	public double getGain(){
		return percentage * serviceGain;
	}
}
