package com.hrms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "payrolls")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payroll {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "employee_id", nullable = false)
	private Employee employee;

	@Column(nullable = false)
	private String month; // YYYY-MM

	@Column(nullable = false)
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

	@Column(name = "generated_at")
	private LocalDateTime generatedAt;

	@PrePersist
	protected void onCreate() {
		generatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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