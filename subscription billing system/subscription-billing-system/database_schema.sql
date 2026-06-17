-- Drop database if exists (for fresh setup)
DROP DATABASE IF EXISTS billing_db;

-- Create database
CREATE DATABASE billing_db;
USE billing_db;

-- =====================================================
-- 1. USERS TABLE
-- =====================================================
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    phone VARCHAR(15),
    company_name VARCHAR(200),
    billing_address TEXT,
    role VARCHAR(50) NOT NULL DEFAULT 'CUSTOMER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_email (email),
    INDEX idx_role (role),
    INDEX idx_status (status)
);

-- =====================================================
-- 2. PLANS TABLE
-- =====================================================
CREATE TABLE plans (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    billing_cycle VARCHAR(20) NOT NULL,
    features TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_billing_cycle (billing_cycle)
);

-- =====================================================
-- 3. SUBSCRIPTIONS TABLE
-- =====================================================
CREATE TABLE subscriptions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    subscription_number VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    plan_id BIGINT NOT NULL,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    amount DECIMAL(10,2),
    billing_cycle VARCHAR(20),
    cancelled_at TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (plan_id) REFERENCES plans(id) ON DELETE CASCADE,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_end_date (end_date),
    INDEX idx_subscription_number (subscription_number)
);

-- =====================================================
-- 4. INVOICES TABLE
-- =====================================================
CREATE TABLE invoices (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    invoice_number VARCHAR(50) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    subscription_id BIGINT,
    amount DECIMAL(10,2) NOT NULL,
    tax_amount DECIMAL(10,2) DEFAULT 0,
    total_amount DECIMAL(10,2),
    due_date TIMESTAMP NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    description TEXT,
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    paid_at TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (subscription_id) REFERENCES subscriptions(id) ON DELETE SET NULL,
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_due_date (due_date),
    INDEX idx_invoice_number (invoice_number)
);

-- =====================================================
-- 5. PAYMENTS TABLE
-- =====================================================
CREATE TABLE payments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    transaction_id VARCHAR(50) NOT NULL UNIQUE,
    invoice_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SUCCESS',
    payment_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    remarks TEXT,
    FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_invoice (invoice_id),
    INDEX idx_user (user_id),
    INDEX idx_status (status),
    INDEX idx_payment_date (payment_date),
    INDEX idx_transaction_id (transaction_id)
);

-- =====================================================
-- 6. AUDIT LOGS TABLE
-- =====================================================
CREATE TABLE audit_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    entity_type VARCHAR(100) NOT NULL,
    entity_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    performed_by VARCHAR(150),
    ip_address VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_entity (entity_type, entity_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Plans
INSERT INTO plans (name, description, price, billing_cycle, features) VALUES
('Basic', 'Perfect for individuals and small projects', 9.99, 'MONTHLY', '5 projects, 10GB storage, Email support, Basic analytics'),
('Pro', 'Best for professionals and growing teams', 29.99, 'MONTHLY', 'Unlimited projects, 100GB storage, Priority support, API access, Advanced analytics'),
('Enterprise', 'For large organizations with advanced needs', 99.99, 'MONTHLY', 'Unlimited everything, Dedicated support, SLA guarantee, Custom integrations, 24/7 phone support'),
('Basic Yearly', 'Basic plan - yearly billing', 99.99, 'YEARLY', '5 projects, 10GB storage, Email support, Basic analytics (Save 16%)'),
('Pro Yearly', 'Pro plan - yearly billing', 299.99, 'YEARLY', 'Unlimited projects, 100GB storage, Priority support, API access, Advanced analytics (Save 16%)'),
('Enterprise Yearly', 'Enterprise plan - yearly billing', 999.99, 'YEARLY', 'Unlimited everything, Dedicated support, SLA guarantee, Custom integrations (Save 16%)');

-- Insert Users
INSERT INTO users (email, password, first_name, last_name, phone, company_name, billing_address, role) VALUES
('admin@billing.com', 'admin123', 'Admin', 'User', '9876543210', 'Billing System Inc', '123 Corporate Park, New York, NY 10001', 'ADMIN'),
('john.doe@example.com', 'password123', 'John', 'Doe', '9876543211', 'Tech Solutions Ltd', '456 Business Plaza, San Francisco, CA 94105', 'CUSTOMER'),
('jane.smith@example.com', 'password123', 'Jane', 'Smith', '9876543212', 'Creative Agency', '789 Innovation Hub, Austin, TX 73301', 'CUSTOMER'),
('mike.johnson@example.com', 'password123', 'Mike', 'Johnson', '9876543213', 'Startup Inc', '321 Founder Street, Seattle, WA 98101', 'MANAGER'),
('sarah.williams@example.com', 'password123', 'Sarah', 'Williams', '9876543214', 'Enterprise Corp', '555 Business Ave, Chicago, IL 60601', 'CUSTOMER');

-- Insert Subscriptions
INSERT INTO subscriptions (subscription_number, user_id, plan_id, start_date, end_date, status, amount, billing_cycle) VALUES
('SUB-2024001', 2, 2, '2024-01-01 00:00:00', '2024-12-31 23:59:59', 'ACTIVE', 29.99, 'MONTHLY'),
('SUB-2024002', 3, 1, '2024-02-01 00:00:00', '2024-07-31 23:59:59', 'ACTIVE', 9.99, 'MONTHLY'),
('SUB-2024003', 4, 3, '2024-01-15 00:00:00', '2024-04-15 23:59:59', 'ACTIVE', 99.99, 'MONTHLY'),
('SUB-2024004', 5, 4, '2024-01-01 00:00:00', '2025-01-01 23:59:59', 'ACTIVE', 99.99, 'YEARLY'),
('SUB-2024005', 2, 5, '2024-03-01 00:00:00', '2025-03-01 23:59:59', 'CANCELLED', 299.99, 'YEARLY');

-- Insert Invoices
INSERT INTO invoices (invoice_number, user_id, subscription_id, amount, tax_amount, total_amount, due_date, status, description) VALUES
('INV-2024001', 2, 1, 29.99, 5.40, 35.39, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'PAID', 'Monthly subscription - Pro Plan'),
('INV-2024002', 3, 2, 9.99, 1.80, 11.79, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'PENDING', 'Monthly subscription - Basic Plan'),
('INV-2024003', 4, 3, 99.99, 18.00, 117.99, DATE_ADD(CURDATE(), INTERVAL 15 DAY), 'OVERDUE', 'Monthly subscription - Enterprise Plan'),
('INV-2024004', 5, 4, 99.99, 18.00, 117.99, DATE_ADD(CURDATE(), INTERVAL 30 DAY), 'PENDING', 'Yearly subscription - Basic Yearly Plan'),
('INV-2024005', 2, 5, 299.99, 54.00, 353.99, DATE_SUB(CURDATE(), INTERVAL 10 DAY), 'OVERDUE', 'Yearly subscription - Pro Yearly Plan');

-- Insert Payments
INSERT INTO payments (transaction_id, invoice_id, user_id, amount, payment_method, status, remarks) VALUES
('TXN-2024001', 1, 2, 35.39, 'CREDIT_CARD', 'SUCCESS', 'Payment for invoice INV-2024001'),
('TXN-2024002', 2, 3, 11.79, 'UPI', 'PENDING', 'Payment for invoice INV-2024002'),
('TXN-2024003', 4, 5, 117.99, 'NET_BANKING', 'SUCCESS', 'Payment for invoice INV-2024004'),
('TXN-2024004', 5, 2, 353.99, 'CREDIT_CARD', 'FAILED', 'Payment failed - Insufficient funds');

-- Insert Audit Logs
INSERT INTO audit_logs (entity_type, entity_id, action, old_value, new_value, performed_by) VALUES
('User', 2, 'CREATED', NULL, 'john.doe@example.com', 'SYSTEM'),
('User', 3, 'CREATED', NULL, 'jane.smith@example.com', 'SYSTEM'),
('Subscription', 1, 'CREATED', NULL, 'SUB-2024001', 'john.doe@example.com'),
('Subscription', 2, 'CREATED', NULL, 'SUB-2024002', 'jane.smith@example.com'),
('Payment', 1, 'CREATED', NULL, 'TXN-2024001', 'john.doe@example.com');

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Show all tables
SHOW TABLES;

-- Count records
SELECT 'Users' AS table_name, COUNT(*) AS count FROM users
UNION ALL
SELECT 'Plans', COUNT(*) FROM plans
UNION ALL
SELECT 'Subscriptions', COUNT(*) FROM subscriptions
UNION ALL
SELECT 'Invoices', COUNT(*) FROM invoices
UNION ALL
SELECT 'Payments', COUNT(*) FROM payments
UNION ALL
SELECT 'Audit Logs', COUNT(*) FROM audit_logs;

-- Active subscriptions summary
SELECT 
    s.status,
    COUNT(*) AS count,
    SUM(s.amount) AS total_monthly_revenue
FROM subscriptions s
GROUP BY s.status;

-- Revenue by payment method
SELECT 
    payment_method,
    COUNT(*) AS count,
    SUM(amount) AS total_amount
FROM payments
WHERE status = 'SUCCESS'
GROUP BY payment_method;

-- Overdue invoices
SELECT 
    i.invoice_number,
    u.email,
    i.amount,
    i.due_date,
    DATEDIFF(CURDATE(), i.due_date) AS days_overdue
FROM invoices i
JOIN users u ON i.user_id = u.id
WHERE i.status = 'OVERDUE';

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedure to get user subscription details
DELIMITER //
CREATE PROCEDURE GetUserSubscriptions(IN p_user_id BIGINT)
BEGIN
    SELECT 
        s.subscription_number,
        p.name AS plan_name,
        s.start_date,
        s.end_date,
        s.status,
        s.amount,
        s.billing_cycle
    FROM subscriptions s
    JOIN plans p ON s.plan_id = p.id
    WHERE s.user_id = p_user_id
    ORDER BY s.created_at DESC;
END //
DELIMITER ;

-- Procedure to generate monthly revenue report
DELIMITER //
CREATE PROCEDURE GetMonthlyRevenueReport(IN p_year INT, IN p_month INT)
BEGIN
    SELECT 
        DATE_FORMAT(payment_date, '%Y-%m-%d') AS payment_date,
        COUNT(*) AS transaction_count,
        SUM(amount) AS total_amount
    FROM payments
    WHERE YEAR(payment_date) = p_year AND MONTH(payment_date) = p_month
        AND status = 'SUCCESS'
    GROUP BY DATE_FORMAT(payment_date, '%Y-%m-%d')
    ORDER BY payment_date;
END //
DELIMITER ;

-- =====================================================
-- VIEWS
-- =====================================================

-- View: Active subscriptions with user and plan details
CREATE VIEW v_active_subscriptions AS
SELECT 
    s.subscription_number,
    s.start_date,
    s.end_date,
    s.amount,
    s.billing_cycle,
    u.email,
    u.first_name,
    u.last_name,
    p.name AS plan_name,
    p.features
FROM subscriptions s
JOIN users u ON s.user_id = u.id
JOIN plans p ON s.plan_id = p.id
WHERE s.status = 'ACTIVE';

-- View: Revenue summary
CREATE VIEW v_revenue_summary AS
SELECT 
    DATE_FORMAT(payment_date, '%Y-%m') AS month,
    COUNT(*) AS total_transactions,
    SUM(amount) AS total_revenue,
    AVG(amount) AS avg_transaction_value
FROM payments
WHERE status = 'SUCCESS'
GROUP BY DATE_FORMAT(payment_date, '%Y-%m')
ORDER BY month DESC;

-- =====================================================
-- INDEXES FOR PERFORMANCE
-- =====================================================

-- Additional indexes for better query performance
CREATE INDEX idx_users_created_at ON users(created_at);
CREATE INDEX idx_subscriptions_created_at ON subscriptions(created_at);
CREATE INDEX idx_invoices_generated_at ON invoices(generated_at);
CREATE INDEX idx_payments_payment_date ON payments(payment_date);
CREATE INDEX idx_audit_logs_created_at ON audit_logs(created_at);

-- =====================================================
-- COMMIT AND VERIFY
-- =====================================================

COMMIT;

-- Final verification
SELECT 'Database schema created successfully!' AS status;
SELECT COUNT(*) AS total_users FROM users;
SELECT COUNT(*) AS total_plans FROM plans;
SELECT COUNT(*) AS total_subscriptions FROM subscriptions;
SELECT COUNT(*) AS total_invoices FROM invoices;
SELECT COUNT(*) AS total_payments FROM payments;
SELECT COUNT(*) AS total_audit_logs FROM audit_logs;