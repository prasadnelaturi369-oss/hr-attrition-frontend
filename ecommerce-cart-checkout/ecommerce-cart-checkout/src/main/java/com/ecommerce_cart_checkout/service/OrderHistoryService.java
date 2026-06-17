package com.ecommerce_cart_checkout.service;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ecommerce_cart_checkout.entity.Order;
import com.ecommerce_cart_checkout.entity.OrderItem;
import com.ecommerce_cart_checkout.entity.User;
import com.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.ecommerce_cart_checkout.payload.response.OrderResponse;
import com.ecommerce_cart_checkout.repository.OrderRepository;
import com.ecommerce_cart_checkout.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {

	private static final Logger log = LoggerFactory.getLogger(OrderHistoryService.class);

	private final OrderRepository orderRepository;
	private final UserRepository userRepository;

	public OrderHistoryService(OrderRepository orderRepository, UserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
	}

	public Page<OrderResponse> getUserOrders(Long userId, int page, int size) {
		log.info("Fetching orders for user: {}, page: {}, size: {}", userId, page, size);

		User user = userRepository.findById(userId)
				.orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

		Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
		Page<Order> orders = orderRepository.findByUser(user, pageable);

		return orders.map(this::convertToOrderResponse);
	}

	private OrderResponse convertToOrderResponse(Order order) {
		OrderResponse response = new OrderResponse();
		response.setOrderId(order.getId());
		response.setOrderNumber(order.getOrderNumber());
		response.setUserId(order.getUser().getId());
		response.setUserName(order.getUser().getName());
		response.setUserEmail(order.getUser().getEmail());
		response.setTotalAmount(order.getTotalAmount());
		response.setDiscountAmount(order.getDiscountAmount());
		response.setCouponCode(order.getCouponCode());
		response.setStatus(order.getStatus());
		response.setCreatedAt(order.getCreatedAt());

		if (order.getItems() != null && !order.getItems().isEmpty()) {
			response.setItems(order.getItems().stream().map(this::convertToOrderItem).collect(Collectors.toList()));
		}

		return response;
	}

	private OrderResponse.OrderItemDTO convertToOrderItem(OrderItem item) {
		OrderResponse.OrderItemDTO itemDTO = new OrderResponse.OrderItemDTO();
		itemDTO.setProductId(item.getProduct().getId());
		itemDTO.setProductName(item.getProduct().getName());
		itemDTO.setQuantity(item.getQuantity());
		itemDTO.setPrice(item.getPrice());
		itemDTO.setSubtotal(item.getPrice().multiply(java.math.BigDecimal.valueOf(item.getQuantity())));
		return itemDTO;
	}
}