package log;

public class ServiceLogItem extends CILogItem{
	private double fixed_cost;
	private double fixed_income;
	private double down_cost;
	private double value;
	
	public ServiceLogItem(int id, double fixed_cost, double fixed_income, double down_cost){
		super(id);
		this.fixed_cost = fixed_cost;
		this.fixed_income = fixed_income;
		this.down_cost = down_cost;
	}
	
	void updateStatus(int time) {
		getTimes().add(time);
		int timesSize = getTimes().size();
		int newDuration = getTimes().get(timesSize - 1) - getTimes().get(timesSize - 2);
		if (isUp()) {
			// last time is up - updated downLength
			addDownDuration(newDuration);
			value-=newDuration*(fixed_cost+down_cost);	
		} else {
			// last time is down - updated upLength
			addUpDuration(newDuration);
			value+=newDuration*(fixed_income-fixed_cost);
		}
	}

	/**
	 * @return the fixed_cost
	 */
	public double getFixed_cost() {
		return fixed_cost;
	}

	/**
	 * @return the fixed_income
	 */
	public double getFixed_income() {
		return fixed_income;
	}

	/**
	 * @return the down_cost
	 */
	public double getDown_cost() {
		return down_cost;
	}

	/**
	 * @return the value
	 */
	public double getValue() {
		return value;
	}
	
	
}