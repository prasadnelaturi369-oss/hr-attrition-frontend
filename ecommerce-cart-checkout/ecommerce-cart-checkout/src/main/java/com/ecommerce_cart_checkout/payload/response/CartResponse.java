package com.ecommerce_cart_checkout.payload.response;

import java.math.BigDecimal;
import java.util.List;

public class CartResponse {
	private Long cartId;
	private Long userId;
	private String userName;
	private List<CartItemDTO> items;
	private BigDecimal subtotal;
	private BigDecimal discountAmount;
	private BigDecimal totalAmount;
	private String appliedCouponCode;

	public static class CartItemDTO {
		private Long productId;
		private String productName;
		private BigDecimal productPrice;
		private Integer quantity;
		private BigDecimal itemTotal;

		public Long getProductId() {
			return productId;
		}

		public void setProductId(Long productId) {
			this.productId = productId;
		}

		public String getProductName() {
			return productName;
		}

		public void setProductName(String productName) {
			this.productName = productName;
		}

		public BigDecimal getProductPrice() {
			return productPrice;
		}

		public void setProductPrice(BigDecimal productPrice) {
			this.productPrice = productPrice;
		}

		public Integer getQuantity() {
			return quantity;
		}

		public void setQuantity(Integer quantity) {
			this.quantity = quantity;
		}

		public BigDecimal getItemTotal() {
			return itemTotal;
		}

		public void setItemTotal(BigDecimal itemTotal) {
			this.itemTotal = itemTotal;
		}
	}

	public Long getCartId() {
		return cartId;
	}

	public void setCartId(Long cartId) {
		this.cartId = cartId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public List<CartItemDTO> getItems() {
		return items;
	}

	public void setItems(List<CartItemDTO> items) {
		this.items = items;
	}

	public BigDecimal getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(BigDecimal subtotal) {
		this.subtotal = subtotal;
	}

	public BigDecimal getDiscountAmount() {
		return discountAmount;
	}

	public void setDiscountAmount(BigDecimal discountAmount) {
		this.discountAmount = discountAmount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getAppliedCouponCode() {
		return appliedCouponCode;
	}

	public void setAppliedCouponCode(String appliedCouponCode) {
		this.appliedCouponCode = appliedCouponCode;
	}
}