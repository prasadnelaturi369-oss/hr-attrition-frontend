package com.hrms.payload.response;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class PayrollResponse {

	private Long id;
	private Long employeeId;
	private String employeeName;
	private String employeeCode;
	private String month;
	private Double basicSalary;
	private Double hra;
	private Double da;
	private Double ta;
	private Double otherAllowances;
	private Double totalEarnings;
	private Double pfDeduction;
	private Double professionalTax;
	private Double incomeTax;
	private Double otherDeductions;
	private Double totalDeductions;
	private Double netSalary;
	private Integer totalPresentDays;
	private Integer totalAbsentDays;
	private Integer totalLeavesTaken;
	private String status;
	private LocalDateTime generatedAt;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeeCode() {
		return employeeCode;
	}

	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}

	public String getMonth() {
		return month;
	}

	public void setMonth(String month) {
		this.month = month;
	}

	public Double getBasicSalary() {
		return basicSalary;
	}

	public void setBasicSalary(Double basicSalary) {
		this.basicSalary = basicSalary;
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

	public Double getTotalEarnings() {
		return totalEarnings;
	}

	public void setTotalEarnings(Double totalEarnings) {
		this.totalEarnings = totalEarnings;
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

	public Double getTotalDeductions() {
		return totalDeductions;
	}

	public void setTotalDeductions(Double totalDeductions) {
		this.totalDeductions = totalDeductions;
	}

	public Double getNetSalary() {
		return netSalary;
	}

	public void setNetSalary(Double netSalary) {
		this.netSalary = netSalary;
	}

	public Integer getTotalPresentDays() {
		return totalPresentDays;
	}

	public void setTotalPresentDays(Integer totalPresentDays) {
		this.totalPresentDays = totalPresentDays;
	}

	public Integer getTotalAbsentDays() {
		return totalAbsentDays;
	}

	public void setTotalAbsentDays(Integer totalAbsentDays) {
		this.totalAbsentDays = totalAbsentDays;
	}

	public Integer getTotalLeavesTaken() {
		return totalLeavesTaken;
	}

	public void setTotalLeavesTaken(Integer totalLeavesTaken) {
		this.totalLeavesTaken = totalLeavesTaken;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getGeneratedAt() {
		return generatedAt;
	}

	public void setGeneratedAt(LocalDateTime generatedAt) {
		this.generatedAt = generatedAt;
	}

}