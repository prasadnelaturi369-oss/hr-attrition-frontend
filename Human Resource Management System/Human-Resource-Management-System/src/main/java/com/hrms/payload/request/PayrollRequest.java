package com.hrms.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PayrollRequest {

	@NotNull(message = "Employee ID is required")
	private Long employeeId;

	@NotBlank(message = "Month is required")
	private String month;

	private Double hra;
	private Double da;
	private Double ta;
	private Double otherAllowances;
	private Double pfDeduction;
	private Double professionalTax;
	private Double incomeTax;
	private Double otherDeductions;

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getHra() {
		return hra;
	}

	public void setHra(Double hra) {
		this.hra = hra;
	}

	public Double getDa() {
		return da;
	}

	public void setDa(Double da) {
		this.da = da;
	}

	public Double getTa() {
		return ta;
	}

	public void setTa(Double ta) {
		this.ta = ta;
	}

	public Double getOtherAllowances() {
		return otherAllowances;
	}

	public void setOtherAllowances(Double otherAllowances) {
		this.otherAllowances = otherAllowances;
	}

	public Double getPfDeduction() {
		return pfDeduction;
	}

	public void setPfDeduction(Double pfDeduction) {
		this.pfDeduction = pfDeduction;
	}

	public Double getProfessionalTax() {
		return professionalTax;
	}

	public void setProfessionalTax(Double professionalTax) {
		this.professionalTax = professionalTax;
	}

	public Double getIncomeTax() {
		return incomeTax;
	}

	public void setIncomeTax(Double incomeTax) {
		this.incomeTax = incomeTax;
	}

	public Double getOtherDeductions() {
		return otherDeductions;
	}

	public void setOtherDeductions(Double otherDeductions) {
		this.otherDeductions = otherDeductions;
	}

}
