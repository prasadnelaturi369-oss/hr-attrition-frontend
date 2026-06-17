package com.ecommerce_cart_checkout.payload.request;

import jakarta.validation.constraints.NotNull;

public class PaymentRequest {
	@NotNull(message = "Order ID is required")
	private Long orderId;

	@NotNull(message = "Payment status is required")
	private boolean paymentSuccess;

	private String paymentMethod = "CREDIT_CARD";
	private String transactionId;

	public PaymentRequest() {
	}

	public PaymentRequest(Long orderId, boolean paymentSuccess) {
		this.orderId = orderId;
		this.paymentSuccess = paymentSuccess;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public boolean isPaymentSuccess() { // Note: isPaymentSuccess() for boolean
		return paymentSuccess;
	}

	public void setPaymentSuccess(boolean paymentSuccess) {
		this.paymentSuccess = paymentSuccess;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}
}
