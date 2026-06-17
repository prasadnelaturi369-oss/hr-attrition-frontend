package com.ecommerce_cart_checkout.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_cart_checkout.entity.Cart;
import com.ecommerce_cart_checkout.entity.CartItem;
import com.ecommerce_cart_checkout.entity.Order;
import com.ecommerce_cart_checkout.entity.OrderItem;
import com.ecommerce_cart_checkout.entity.Product;
import com.ecommerce_cart_checkout.entity.User;
import com.ecommerce_cart_checkout.exception.BusinessException;
import com.ecommerce_cart_checkout.exception.InsufficientStockException;
import com.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.ecommerce_cart_checkout.payload.request.CheckoutRequest;
import com.ecommerce_cart_checkout.payload.request.PaymentRequest;
import com.ecommerce_cart_checkout.payload.response.OrderResponse;
import com.ecommerce_cart_checkout.repository.CartRepository;
import com.ecommerce_cart_checkout.repository.OrderItemRepository;
import com.ecommerce_cart_checkout.repository.OrderRepository;
import com.ecommerce_cart_checkout.repository.ProductRepository;
import com.ecommerce_cart_checkout.repository.UserRepository;

@Service
public class CheckoutService {
	private final CartRepository cartRepository;
	private final OrderRepository orderRepository;
	private final OrderItemRepository orderItemRepository;
	private final ProductRepository productRepository;
	private final UserRepository userRepository;
	private final CouponService couponService;

	public CheckoutService(CartRepository cartRepository, OrderRepository orderRepository,
			OrderItemRepository orderItemRepository, ProductRepository productRepository, UserRepository userRepository,
			CouponService couponService) {
		this.cartRepository = cartRepository;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.productRepository = productRepository;
		this.userRepository = userRepository;
		this.couponService = couponService;
	}

	@Transactional
	public Order createOrderFromCart(CheckoutRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Cart cart = cartRepository.findByUser(user)
				.orElseThrow(() -> new BusinessException("Cart is empty. Please add items to cart first."));

		if (cart.getItems().isEmpty()) {
			throw new BusinessException("Cart is empty. Cannot create order.");
		}

		for (CartItem cartItem : cart.getItems()) {
			Product product = cartItem.getProduct();
			if (product.getStockQuantity() < cartItem.getQuantity()) {
				throw new InsufficientStockException("Insufficient stock for product: " + product.getName()
						+ ". Available: " + product.getStockQuantity() + ", Requested: " + cartItem.getQuantity());
			}
		}

		BigDecimal subtotal = cart.getTotalPrice();
		BigDecimal discount = BigDecimal.ZERO;
		String appliedCouponCode = null;

		if (request.getCouponCode() != null && !request.getCouponCode().isEmpty()) {
			try {
				discount = couponService.applyCoupon(request.getCouponCode(), subtotal);
				appliedCouponCode = request.getCouponCode();
			} catch (Exception e) {
				throw new BusinessException("Failed to apply coupon: " + e.getMessage());
			}
		}

		BigDecimal totalAmount = subtotal.subtract(discount);

		Order order = new Order();
		order.setUser(user);
		order.setTotalAmount(totalAmount);
		order.setDiscountAmount(discount);
		order.setCouponCode(appliedCouponCode);
		order.setStatus("PENDING_PAYMENT");
		order.setOrderNumber("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

		order = orderRepository.save(order);

		List<OrderItem> orderItems = new ArrayList<>();
		for (CartItem cartItem : cart.getItems()) {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrder(order);
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(cartItem.getProduct().getPrice());
			orderItem
					.setSubtotal(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			orderItems.add(orderItem);

			int updated = productRepository.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
			if (updated == 0) {
				throw new InsufficientStockException(
						"Failed to update stock for product: " + cartItem.getProduct().getName());
			}
		}

		orderItemRepository.saveAll(orderItems);
		order.setItems(orderItems);

		cart.getItems().clear();
		cartRepository.save(cart);

		return orderRepository.save(order);
	}

	@Transactional
	public Order processPayment(PaymentRequest request) {
		Order order = orderRepository.findById(request.getOrderId())
				.orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + request.getOrderId()));

		if (!"PENDING_PAYMENT".equals(order.getStatus())) {
			throw new BusinessException("Order is not in pending payment state. Current status: " + order.getStatus());
		}

		if (request.isPaymentSuccess()) {
			order.setStatus("PAID");
		} else {
			order.setStatus("PAYMENT_FAILED");
			for (OrderItem item : order.getItems()) {
				productRepository.restoreStock(item.getProduct().getId(), item.getQuantity());
			}
		}

		return orderRepository.save(order);
	}

	public OrderResponse getOrderSummary(Long orderId) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order not found"));

		return mapToOrderResponse(order);
	}

	private OrderResponse mapToOrderResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setOrderId(order.getId());
		response.setOrderNumber(order.getOrderNumber());
		response.setUserId(order.getUser().getId());
		response.setUserName(order.getUser().getName());
		response.setUserEmail(order.getUser().getEmail());
		response.setTotalAmount(order.getTotalAmount());
		response.setDiscountAmount(order.getDiscountAmount() != null ? order.getDiscountAmount() : BigDecimal.ZERO);
		response.setCouponCode(order.getCouponCode());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());

		List<OrderResponse.OrderItemDTO> itemDTOs = new ArrayList<>();
		if (order.getItems() != null) {
			for (OrderItem item : order.getItems()) {
				OrderResponse.OrderItemDTO itemDTO = new OrderResponse.OrderItemDTO();
				itemDTO.setProductId(item.getProduct().getId());
				itemDTO.setProductName(item.getProduct().getName());
				itemDTO.setQuantity(item.getQuantity());
				itemDTO.setPrice(item.getPrice());
				itemDTO.setSubtotal(item.getSubtotal());
				itemDTOs.add(itemDTO);
			}
		}
		response.setItems(itemDTOs);

		return response;
	}
}