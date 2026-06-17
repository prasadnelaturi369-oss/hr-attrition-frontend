package com.billing.service;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.billing.payload.response.DashboardResponse;
import com.billing.repository.InvoiceRepository;
import com.billing.repository.PaymentRepository;
import com.billing.repository.SubscriptionRepository;
import com.billing.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

	private static final Logger log = LoggerFactory.getLogger(DashboardService.class);

	private final UserRepository userRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final InvoiceRepository invoiceRepository;
	private final PaymentRepository paymentRepository;

	public DashboardService(UserRepository userRepository, SubscriptionRepository subscriptionRepository,
			InvoiceRepository invoiceRepository, PaymentRepository paymentRepository) {
		this.userRepository = userRepository;
		this.subscriptionRepository = subscriptionRepository;
		this.invoiceRepository = invoiceRepository;
		this.paymentRepository = paymentRepository;
	}

	public DashboardResponse getDashboardStats() {
		log.info("Fetching dashboard statistics");

		DashboardResponse response = new DashboardResponse();

		response.setTotalUsers(userRepository.count());
		response.setActiveUsers(userRepository.countByStatus("ACTIVE"));

		response.setActiveSubscriptions(subscriptionRepository.countByStatus("ACTIVE"));
		response.setExpiredSubscriptions(subscriptionRepository.countByStatus("EXPIRED"));
		response.setCancelledSubscriptions(subscriptionRepository.countByStatus("CANCELLED"));

		response.setTotalInvoices(invoiceRepository.count());
		response.setPaidInvoices(invoiceRepository.countByStatus("PAID"));
		response.setPendingInvoices(invoiceRepository.countByStatus("PENDING"));
		response.setOverdueInvoices(invoiceRepository.countByStatus("OVERDUE"));

		response.setTotalRevenue(paymentRepository.getTotalRevenue());
		response.setMonthlyRevenue(paymentRepository.getMonthlyRevenue(YearMonth.now().toString()));

		Map<String, Double> revenueByMonth = new HashMap<>();
		for (int i = 5; i >= 0; i--) {
			YearMonth month = YearMonth.now().minusMonths(i);
			Double revenue = paymentRepository.getMonthlyRevenue(month.toString());
			revenueByMonth.put(month.toString(), revenue != null ? revenue : 0.0);
		}
		response.setRevenueByMonth(revenueByMonth);

		return response;
	}
}