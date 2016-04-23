package log;

public class ServiceLogItem extends CILogItem{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double fixed_cost;
	private double fixed_income;
	private double down_cost;
	private double totalProfit;
	/*
	 * the profit/loss per second
	 */
	private double diff;
	/*
	 * counts how many CI's the service depends on, are currently down
	 */
	private int cisDown;
	
	public ServiceLogItem(int id, double fixed_cost, double fixed_income, double down_cost){
		super(id);
		this.cisDown = 0;
		this.fixed_cost = fixed_cost;
		this.fixed_income = fixed_income;
		this.down_cost = down_cost;
		this.diff = fixed_income-fixed_cost;
	}
	
	void updateStatus(int time) {
		super.updateStatus(time);
		diff = getLastDuaration() * ((isUp())? (-fixed_cost-down_cost) : (fixed_income-fixed_cost));		
		totalProfit+=diff;
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
		return totalProfit;
	}
	
	/*
	 * Updates the CI counter of the service, and, if needed, updates the service's status.
	 * The function returns the change in the service's profit gain speed.
	 */
	public double ciUpdate(boolean isUp, int time){
		double oldDiff = diff;
		if (isUp){
			// if all CIs ARE DOWN, updates service status
			this.cisDown--;
			if (cisDown == 0){
				updateStatus(time);
			}
		} else{
			// if all CIs WERE DOWN, updates service status
			if (cisDown == 0){
				updateStatus(time);
			}
			this.cisDown++;
		}
		return diff-oldDiff; 
	}	
}