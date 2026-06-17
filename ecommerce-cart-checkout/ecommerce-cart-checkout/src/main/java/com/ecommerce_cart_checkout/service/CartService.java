package com.ecommerce_cart_checkout.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ecommerce_cart_checkout.entity.Cart;
import com.ecommerce_cart_checkout.entity.CartItem;
import com.ecommerce_cart_checkout.entity.Product;
import com.ecommerce_cart_checkout.entity.User;
import com.ecommerce_cart_checkout.exception.InsufficientStockException;
import com.ecommerce_cart_checkout.exception.ResourceNotFoundException;
import com.ecommerce_cart_checkout.payload.request.CartRequest;
import com.ecommerce_cart_checkout.payload.response.CartResponse;
import com.ecommerce_cart_checkout.repository.CartItemRepository;
import com.ecommerce_cart_checkout.repository.CartRepository;
import com.ecommerce_cart_checkout.repository.ProductRepository;
import com.ecommerce_cart_checkout.repository.UserRepository;

@Service
public class CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final UserRepository userRepository;
	private final ProductRepository productRepository;

	public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
			UserRepository userRepository, ProductRepository productRepository) {
		this.cartRepository = cartRepository;
		this.cartItemRepository = cartItemRepository;
		this.userRepository = userRepository;
		this.productRepository = productRepository;
	}

	@Transactional
	public CartResponse addToCart(CartRequest request) {
		User user = userRepository.findById(request.getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Product product = productRepository.findById(request.getProductId())
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		Cart cart = cartRepository.findByUser(user).orElseGet(() -> {
			Cart newCart = new Cart();
			newCart.setUser(user);
			return cartRepository.save(newCart);
		});

		// Check stock availability
		if (product.getStockQuantity() < request.getQuantity()) {
			throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
		}

		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product).orElse(null);

		if (cartItem == null) {
			cartItem = new CartItem(cart, product, request.getQuantity());
			cart.getItems().add(cartItem);
		} else {
			cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
		}

		cartItemRepository.save(cartItem);
		cartRepository.save(cart);

		return mapToCartResponse(cart);
	}

	@Transactional
	public CartResponse updateCartItemQuantity(Long userId, Long productId, Integer quantity) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

		if (quantity <= 0) {
			cartItemRepository.delete(cartItem);
			cart.getItems().remove(cartItem);
		} else {
			if (product.getStockQuantity() < quantity) {
				throw new InsufficientStockException("Insufficient stock for product: " + product.getName());
			}
			cartItem.setQuantity(quantity);
			cartItemRepository.save(cartItem);
		}

		cartRepository.save(cart);
		return mapToCartResponse(cart);
	}

	@Transactional
	public void removeFromCart(Long userId, Long productId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart not found"));

		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResourceNotFoundException("Product not found"));

		CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
				.orElseThrow(() -> new ResourceNotFoundException("Item not found in cart"));

		cartItemRepository.delete(cartItem);
		cart.getItems().remove(cartItem);
		cartRepository.save(cart);
	}

	public CartResponse getCart(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));

		Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Cart is empty"));

		return mapToCartResponse(cart);
	}

	private CartResponse mapToCartResponse(Cart cart) {
		CartResponse response = new CartResponse();
		response.setCartId(cart.getId());
		response.setUserId(cart.getUser().getId());
		response.setUserName(cart.getUser().getName());
		response.setSubtotal(cart.getTotalPrice());
		response.setTotalAmount(cart.getTotalPrice());
		response.setDiscountAmount(BigDecimal.ZERO);

		List<CartResponse.CartItemDTO> itemDTOs = new ArrayList<>();
		for (CartItem cartItem : cart.getItems()) {
			CartResponse.CartItemDTO itemDTO = new CartResponse.CartItemDTO();
			itemDTO.setProductId(cartItem.getProduct().getId());
			itemDTO.setProductName(cartItem.getProduct().getName());
			itemDTO.setProductPrice(cartItem.getProduct().getPrice());
			itemDTO.setQuantity(cartItem.getQuantity());
			itemDTO.setItemTotal(cartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity())));
			itemDTOs.add(itemDTO);
		}
		response.setItems(itemDTOs);

		return response;
	}
}