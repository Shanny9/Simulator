package utils;

public class SolutionElement {
	private int ci_id;
	private int solution_marom;
	private int solution_rakia;
	private double solution_cost;
	private String currency;
	/**
	 * @param ci_id
	 * @param solution_marom
	 * @param solution_rakia
	 * @param solution_cost
	 * @param currency
	 */
	public SolutionElement(int ci_id, int solution_marom, int solution_rakia, double solution_cost,
			String currency) {
		super();
		this.ci_id = ci_id;
		this.solution_marom = solution_marom;
		this.solution_rakia = solution_rakia;
		this.solution_cost = solution_cost;
		this.currency = currency;
	}
	/**
	 * @return the incident_id
	 */
	public int getIncident_id() {
		return ci_id;
	}
	/**
	 * @param ci_id the inciden_id to set
	 */
	public void setIncident_id(int inciden_id) {
		this.ci_id = inciden_id;
	}
	/**
	 * @return the solution_marom
	 */
	public int getSolution_marom() {
		return solution_marom;
	}
	/**
	 * @param solution_marom the solution_marom to set
	 */
	public void setSolution_marom(int solution_marom) {
		this.solution_marom = solution_marom;
	}
	/**
	 * @return the solution_rakia
	 */
	public int getSolution_rakia() {
		return solution_rakia;
	}
	/**
	 * @param solution_rakia the solution_rakia to set
	 */
	public void setSolution_rakia(int solution_rakia) {
		this.solution_rakia = solution_rakia;
	}
	/**
	 * @return the solution_cost
	 */
	public double getSolution_cost() {
		return solution_cost;
	}
	/**
	 * @param solution_cost the solution_cost to set
	 */
	public void setSolution_cost(double solution_cost) {
		this.solution_cost = solution_cost;
	}
	/**
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}
	/**
	 * @param currency the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
