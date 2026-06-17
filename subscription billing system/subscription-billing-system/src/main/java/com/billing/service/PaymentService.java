package com.billing.service;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.billing.entity.Invoice;
import com.billing.entity.Payment;
import com.billing.entity.User;
import com.billing.exception.BusinessException;
import com.billing.exception.ResourceNotFoundException;
import com.billing.payload.request.PaymentRequest;
import com.billing.payload.response.PaymentResponse;
import com.billing.repository.InvoiceRepository;
import com.billing.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

	private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

	private final PaymentRepository paymentRepository;
	private final InvoiceRepository invoiceRepository;
	private final AuditService auditService;

	public PaymentService(PaymentRepository paymentRepository, InvoiceRepository invoiceRepository,
			AuditService auditService) {

		this.paymentRepository = paymentRepository;
		this.invoiceRepository = invoiceRepository;
		this.auditService = auditService;
	}

	@Transactional
	public PaymentResponse processPayment(PaymentRequest request) {
		log.info("Processing payment for invoice: {}", request.getInvoiceId());

		Invoice invoice = invoiceRepository.findById(request.getInvoiceId())
				.orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

		if ("PAID".equals(invoice.getStatus())) {
			throw new BusinessException("Invoice is already paid");
		}

		if (invoice.getDueDate().isBefore(LocalDateTime.now())) {
			throw new BusinessException("Invoice is overdue. Please contact support.");
		}

		if (request.getAmount() < invoice.getTotalAmount()) {
			throw new BusinessException("Amount is less than invoice total");
		}

		User user = invoice.getUser();

		Payment payment = new Payment();
		payment.setInvoice(invoice);
		payment.setUser(user);
		payment.setAmount(request.getAmount());
		payment.setPaymentMethod(request.getPaymentMethod());
		payment.setRemarks(request.getRemarks());
		payment.setStatus("SUCCESS");

		Payment savedPayment = paymentRepository.save(payment);

		// Update invoice status
		invoice.setStatus("PAID");
		invoice.setPaidAt(LocalDateTime.now());
		invoiceRepository.save(invoice);

		auditService.log("Payment", savedPayment.getId(), "CREATED", null, savedPayment.getTransactionId(),
				"Payment processed for invoice: " + invoice.getInvoiceNumber());

		return mapToResponse(savedPayment);
	}

	public Page<PaymentResponse> getUserPayments(Long userId, Pageable pageable) {
		return paymentRepository.findByUserId(userId, pageable).map(this::mapToResponse);
	}

	public PaymentResponse getPaymentById(Long id) {
		Payment payment = paymentRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
		return mapToResponse(payment);
	}

	public Page<PaymentResponse> getAllPayments(Pageable pageable) {
		return paymentRepository.findAll(pageable).map(this::mapToResponse);
	}

	private PaymentResponse mapToResponse(Payment payment) {
		PaymentResponse response = new PaymentResponse();
		response.setId(payment.getId());
		response.setTransactionId(payment.getTransactionId());
		response.setInvoiceId(payment.getInvoice().getId());
		response.setInvoiceNumber(payment.getInvoice().getInvoiceNumber());
		response.setUserId(payment.getUser().getId());
		response.setUserName(payment.getUser().getFullName());
		response.setAmount(payment.getAmount());
		response.setPaymentMethod(payment.getPaymentMethod());
		response.setStatus(payment.getStatus());
		response.setPaymentDate(payment.getPaymentDate());
		response.setRemarks(payment.getRemarks());
		return response;
	}
}