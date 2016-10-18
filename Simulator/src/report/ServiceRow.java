package report;

public class ServiceRow implements Comparable<ServiceRow>{
	
	private static int businessServicesCount = 0;
	
	private Byte id;
	private String code;
	private String name;
	private String urgency;
	private String impact;
	private String priority;
	private boolean isTechnical;
	
	public ServiceRow(byte id, String code, String name, String urgency, String impact,
			String priority, boolean isTechnical) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.urgency = urgency;
		this.impact = impact;
		this.priority = priority;
		this.isTechnical = isTechnical;
		
		if (!isTechnical){
			businessServicesCount++;
		}
	}
	
	public static int getNumOfBizServices(){
		return businessServicesCount;
	}
	
	
	public byte getId(){
		return id;
	}
	
	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the urgency
	 */
	public String getUrgency() {
		return urgency;
	}

	/**
	 * @return the impact
	 */
	public String getImpact() {
		return impact;
	}

	/**
	 * @return the priority
	 */
	public String getPriority() {
		return priority;
	}

	/**
	 * @return the isTechnical
	 */
	public boolean isTechnical() {
		return isTechnical;
	}

	@Override
	public int compareTo(ServiceRow o) {
		if (!this.isTechnical  && o.isTechnical)
			return -1;
		
		if (this.isTechnical  && !o.isTechnical){
			return 1;
		}
		
		return this.id.compareTo(id);
	}
}
