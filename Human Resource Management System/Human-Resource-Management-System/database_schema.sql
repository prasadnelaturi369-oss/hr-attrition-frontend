
-- Drop database if exists (for fresh setup)
DROP DATABASE IF EXISTS hrms_db;

-- Create database
CREATE DATABASE hrms_db;
USE hrms_db;

-- =====================================================
-- 1. DEPARTMENTS TABLE
-- =====================================================
CREATE TABLE departments (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_name (name)
);

-- =====================================================
-- 2. EMPLOYEES TABLE
-- =====================================================
CREATE TABLE employees (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    employee_code VARCHAR(20) NOT NULL UNIQUE,
    phone VARCHAR(15) NOT NULL,
    address TEXT,
    joining_date DATE NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    designation VARCHAR(100),
    department_id BIGINT,
    role VARCHAR(50) NOT NULL DEFAULT 'EMPLOYEE',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    INDEX idx_email (email),
    INDEX idx_employee_code (employee_code),
    INDEX idx_status (status),
    INDEX idx_department (department_id),
    INDEX idx_role (role)
);

-- =====================================================
-- 3. LEAVE REQUESTS TABLE
-- =====================================================
CREATE TABLE leave_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    leave_type VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    reason TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    approved_by BIGINT,
    rejection_reason TEXT,
    requested_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    approved_at TIMESTAMP NULL,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    FOREIGN KEY (approved_by) REFERENCES employees(id) ON DELETE SET NULL,
    INDEX idx_employee (employee_id),
    INDEX idx_status (status),
    INDEX idx_dates (start_date, end_date),
    INDEX idx_type (leave_type)
);

-- =====================================================
-- 4. ATTENDANCES TABLE
-- =====================================================
CREATE TABLE attendances (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    attendance_date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    check_in_time TIMESTAMP NULL,
    check_out_time TIMESTAMP NULL,
    working_hours DECIMAL(5,2),
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    UNIQUE KEY unique_employee_date (employee_id, attendance_date),
    INDEX idx_employee (employee_id),
    INDEX idx_date (attendance_date),
    INDEX idx_status (status)
);

-- =====================================================
-- 5. PAYROLLS TABLE
-- =====================================================
CREATE TABLE payrolls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    month VARCHAR(7) NOT NULL,
    basic_salary DECIMAL(10,2) NOT NULL,
    hra DECIMAL(10,2),
    da DECIMAL(10,2),
    ta DECIMAL(10,2),
    other_allowances DECIMAL(10,2),
    total_earnings DECIMAL(10,2),
    pf_deduction DECIMAL(10,2),
    professional_tax DECIMAL(10,2),
    income_tax DECIMAL(10,2),
    other_deductions DECIMAL(10,2),
    total_deductions DECIMAL(10,2),
    net_salary DECIMAL(10,2),
    total_present_days INT,
    total_absent_days INT,
    total_leaves_taken INT,
    status VARCHAR(20) NOT NULL DEFAULT 'GENERATED',
    generated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    UNIQUE KEY unique_employee_month (employee_id, month),
    INDEX idx_employee (employee_id),
    INDEX idx_month (month),
    INDEX idx_status (status)
);

-- =====================================================
-- 6. EMPLOYEE HISTORY TABLE (Audit Log)
-- =====================================================
CREATE TABLE employee_history (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    employee_id BIGINT NOT NULL,
    action VARCHAR(50) NOT NULL,
    field_changed VARCHAR(100),
    old_value TEXT,
    new_value TEXT,
    remarks TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    INDEX idx_employee (employee_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
);

-- =====================================================
-- INSERT SAMPLE DATA
-- =====================================================

-- Insert Departments
INSERT INTO departments (name, description) VALUES
('Engineering', 'Software development, testing, and technical operations'),
('Human Resources', 'Recruitment, employee relations, and payroll management'),
('Sales', 'Sales and business development operations'),
('Finance', 'Financial planning, accounting, and budgeting'),
('Marketing', 'Digital marketing, branding, and communications'),
('Operations', 'Business operations and process management'),
('Legal', 'Legal compliance and contract management'),
('Customer Support', 'Customer service and support operations');

-- Insert Employees
INSERT INTO employees (first_name, last_name, email, employee_code, phone, address, joining_date, basic_salary, designation, department_id, role) VALUES
-- Admin/CEO
('John', 'Doe', 'john.doe@hrms.com', 'EMP000001', '9876543210', '123 Main St, New York, NY 10001', '2020-01-15', 150000.00, 'CEO', 1, 'ADMIN'),
('Jane', 'Smith', 'jane.smith@hrms.com', 'EMP000002', '9876543211', '456 Park Ave, New York, NY 10002', '2020-03-20', 120000.00, 'CTO', 1, 'ADMIN'),

-- Engineering Department
('Mike', 'Johnson', 'mike.johnson@hrms.com', 'EMP000003', '9876543212', '789 Tech Blvd, San Francisco, CA 94105', '2021-06-10', 85000.00, 'Senior Software Engineer', 1, 'MANAGER'),
('Sarah', 'Williams', 'sarah.williams@hrms.com', 'EMP000004', '9876543213', '321 Code Lane, San Francisco, CA 94105', '2021-08-15', 75000.00, 'Software Engineer', 1, 'EMPLOYEE'),
('David', 'Brown', 'david.brown@hrms.com', 'EMP000005', '9876543214', '555 Developer Rd, San Francisco, CA 94105', '2022-01-10', 65000.00, 'Junior Developer', 1, 'EMPLOYEE'),

-- HR Department
('Emily', 'Davis', 'emily.davis@hrms.com', 'EMP000006', '9876543215', '777 HR Street, Chicago, IL 60601', '2021-04-05', 70000.00, 'HR Manager', 2, 'MANAGER'),
('Lisa', 'Wilson', 'lisa.wilson@hrms.com', 'EMP000007', '9876543216', '888 People Ave, Chicago, IL 60601', '2022-02-20', 55000.00, 'HR Executive', 2, 'EMPLOYEE'),

-- Sales Department
('Robert', 'Taylor', 'robert.taylor@hrms.com', 'EMP000008', '9876543217', '111 Sales Blvd, Dallas, TX 75201', '2021-09-12', 80000.00, 'Sales Manager', 3, 'MANAGER'),
('Jennifer', 'Anderson', 'jennifer.anderson@hrms.com', 'EMP000009', '9876543218', '222 Market St, Dallas, TX 75201', '2022-03-18', 60000.00, 'Sales Executive', 3, 'EMPLOYEE'),
('Thomas', 'Martinez', 'thomas.martinez@hrms.com', 'EMP000010', '9876543219', '333 Trade Rd, Dallas, TX 75201', '2022-06-22', 58000.00, 'Sales Associate', 3, 'EMPLOYEE'),

-- Finance Department
('Patricia', 'Garcia', 'patricia.garcia@hrms.com', 'EMP000011', '9876543220', '444 Money St, Boston, MA 02101', '2021-11-01', 72000.00, 'Finance Manager', 4, 'MANAGER'),
('Christopher', 'Rodriguez', 'chris.rodriguez@hrms.com', 'EMP000012', '9876543221', '555 Ledger Ave, Boston, MA 02101', '2022-08-14', 62000.00, 'Accountant', 4, 'EMPLOYEE'),

-- Marketing Department
('Daniel', 'Lee', 'daniel.lee@hrms.com', 'EMP000013', '9876543222', '666 Creative Blvd, Seattle, WA 98101', '2022-01-25', 68000.00, 'Marketing Manager', 5, 'MANAGER'),
('Maria', 'Walker', 'maria.walker@hrms.com', 'EMP000014', '9876543223', '777 Brand St, Seattle, WA 98101', '2022-07-30', 52000.00, 'Marketing Specialist', 5, 'EMPLOYEE');

-- Insert Sample Leave Requests
INSERT INTO leave_requests (employee_id, leave_type, start_date, end_date, reason, status) VALUES
(3, 'ANNUAL', DATE_ADD(CURDATE(), INTERVAL 5 DAY), DATE_ADD(CURDATE(), INTERVAL 10 DAY), 'Family vacation', 'PENDING'),
(4, 'SICK', DATE_ADD(CURDATE(), INTERVAL 1 DAY), DATE_ADD(CURDATE(), INTERVAL 2 DAY), 'Fever and cold', 'PENDING'),
(5, 'CASUAL', DATE_ADD(CURDATE(), INTERVAL 15 DAY), DATE_ADD(CURDATE(), INTERVAL 16 DAY), 'Personal work', 'APPROVED'),
(8, 'EMERGENCY', CURDATE(), DATE_ADD(CURDATE(), INTERVAL 1 DAY), 'Family emergency', 'APPROVED'),
(9, 'ANNUAL', DATE_ADD(CURDATE(), INTERVAL 20 DAY), DATE_ADD(CURDATE(), INTERVAL 25 DAY), 'Vacation', 'PENDING');

-- Insert Sample Attendance for Current Month
INSERT INTO attendances (employee_id, attendance_date, status, check_in_time, check_out_time, working_hours) VALUES
-- Today's attendance
(1, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:00:00'), TIMESTAMP(CURDATE(), '18:00:00'), 8.0),
(2, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:15:00'), TIMESTAMP(CURDATE(), '18:15:00'), 8.0),
(3, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:30:00'), TIMESTAMP(CURDATE(), '18:30:00'), 8.0),
(4, CURDATE(), 'LATE', TIMESTAMP(CURDATE(), '10:00:00'), TIMESTAMP(CURDATE(), '18:30:00'), 7.5),
(5, CURDATE(), 'ABSENT', NULL, NULL, 0),
(6, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:00:00'), TIMESTAMP(CURDATE(), '18:00:00'), 8.0),
(7, CURDATE(), 'HALF_DAY', TIMESTAMP(CURDATE(), '09:00:00'), TIMESTAMP(CURDATE(), '13:00:00'), 4.0),
(8, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:00:00'), TIMESTAMP(CURDATE(), '18:00:00'), 8.0),
(9, CURDATE(), 'PRESENT', TIMESTAMP(CURDATE(), '09:15:00'), TIMESTAMP(CURDATE(), '18:15:00'), 8.0),
(10, CURDATE(), 'ABSENT', NULL, NULL, 0),

-- Yesterday's attendance
(1, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 8.0),
(2, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 8.0),
(3, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 8.0),
(4, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'ABSENT', NULL, NULL, 0),
(5, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:30:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:30:00'), 8.0),
(6, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 8.0),
(7, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:15:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:15:00'), 8.0),
(8, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'LATE', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '10:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:30:00'), 7.5),
(9, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'PRESENT', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '18:00:00'), 8.0),
(10, DATE_SUB(CURDATE(), INTERVAL 1 DAY), 'HALF_DAY', TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '09:00:00'), TIMESTAMP(DATE_SUB(CURDATE(), INTERVAL 1 DAY), '13:00:00'), 4.0);

-- Insert Sample Payroll for Current Month
INSERT INTO payrolls (employee_id, month, basic_salary, hra, da, ta, total_earnings, pf_deduction, professional_tax, income_tax, total_deductions, net_salary, total_present_days, total_absent_days, total_leaves_taken, status) VALUES
(1, DATE_FORMAT(CURDATE(), '%Y-%m'), 150000, 30000, 15000, 7500, 202500, 18000, 200, 15000, 33200, 169300, 22, 0, 0, 'PROCESSED'),
(2, DATE_FORMAT(CURDATE(), '%Y-%m'), 120000, 24000, 12000, 6000, 162000, 14400, 200, 10000, 24600, 137400, 22, 0, 0, 'PROCESSED'),
(3, DATE_FORMAT(CURDATE(), '%Y-%m'), 85000, 17000, 8500, 4250, 114750, 10200, 200, 5000, 15400, 99350, 21, 1, 0, 'PROCESSED'),
(4, DATE_FORMAT(CURDATE(), '%Y-%m'), 75000, 15000, 7500, 3750, 101250, 9000, 200, 3000, 12200, 89050, 20, 2, 0, 'GENERATED'),
(5, DATE_FORMAT(CURDATE(), '%Y-%m'), 65000, 13000, 6500, 3250, 87750, 7800, 200, 1500, 9500, 78250, 19, 3, 0, 'GENERATED'),
(6, DATE_FORMAT(CURDATE(), '%Y-%m'), 70000, 14000, 7000, 3500, 94500, 8400, 200, 2000, 10600, 83900, 22, 0, 0, 'PROCESSED'),
(7, DATE_FORMAT(CURDATE(), '%Y-%m'), 55000, 11000, 5500, 2750, 74250, 6600, 200, 1000, 7800, 66450, 21, 1, 0, 'GENERATED');

-- Insert Employee History (Audit Log)
INSERT INTO employee_history (employee_id, action, field_changed, old_value, new_value, remarks) VALUES
(1, 'CREATED', NULL, NULL, NULL, 'Employee onboarded as CEO'),
(2, 'CREATED', NULL, NULL, NULL, 'Employee onboarded as CTO'),
(3, 'PROMOTED', 'designation', 'Software Engineer', 'Senior Software Engineer', 'Promotion effective June 2023'),
(4, 'CREATED', NULL, NULL, NULL, 'Employee onboarded'),
(6, 'CREATED', NULL, NULL, NULL, 'Employee onboarded as HR Manager');

-- =====================================================
-- VERIFICATION QUERIES
-- =====================================================

-- Show all tables
SHOW TABLES;

-- Count records
SELECT 'Departments' AS table_name, COUNT(*) AS count FROM departments
UNION ALL
SELECT 'Employees', COUNT(*) FROM employees
UNION ALL
SELECT 'Leave Requests', COUNT(*) FROM leave_requests
UNION ALL
SELECT 'Attendances', COUNT(*) FROM attendances
UNION ALL
SELECT 'Payrolls', COUNT(*) FROM payrolls
UNION ALL
SELECT 'Employee History', COUNT(*) FROM employee_history;

-- Department employee count
SELECT d.name AS department, COUNT(e.id) AS employee_count
FROM departments d
LEFT JOIN employees e ON d.id = e.department_id AND e.status = 'ACTIVE'
GROUP BY d.id, d.name;

-- Leave status summary
SELECT status, COUNT(*) AS count, SUM(DATEDIFF(end_date, start_date) + 1) AS total_days
FROM leave_requests
GROUP BY status;

-- Today's attendance summary
SELECT status, COUNT(*) AS count
FROM attendances
WHERE attendance_date = CURDATE()
GROUP BY status;

-- Payroll summary for current month
SELECT 
    COUNT(*) AS total_employees,
    FORMAT(SUM(net_salary), 2) AS total_payout,
    FORMAT(AVG(net_salary), 2) AS average_salary
FROM payrolls
WHERE month = DATE_FORMAT(CURDATE(), '%Y-%m');

-- =====================================================
-- STORED PROCEDURES
-- =====================================================

-- Procedure to get employee attendance for a date range
DELIMITER //
CREATE PROCEDURE GetEmployeeAttendance(IN emp_id BIGINT, IN start_date DATE, IN end_date DATE)
BEGIN
    SELECT 
        attendance_date,
        status,
        check_in_time,
        check_out_time,
        working_hours
    FROM attendances
    WHERE employee_id = emp_id
        AND attendance_date BETWEEN start_date AND end_date
    ORDER BY attendance_date;
END //
DELIMITER ;

-- Procedure to generate monthly payroll for all employees
DELIMITER //
CREATE PROCEDURE GenerateMonthlyPayroll(IN payroll_month VARCHAR(7))
BEGIN
    INSERT INTO payrolls (employee_id, month, basic_salary, total_present_days, status)
    SELECT 
        e.id,
        payroll_month,
        e.basic_salary,
        COALESCE((
            SELECT COUNT(*)
            FROM attendances a
            WHERE a.employee_id = e.id
                AND DATE_FORMAT(a.attendance_date, '%Y-%m') = payroll_month
                AND a.status = 'PRESENT'
        ), 0),
        'GENERATED'
    FROM employees e
    WHERE e.status = 'ACTIVE'
        AND NOT EXISTS (
            SELECT 1 FROM payrolls p 
            WHERE p.employee_id = e.id AND p.month = payroll_month
        );
END //
DELIMITER ;

-- =====================================================
-- VIEWS
-- =====================================================

-- View: Active employees with department
CREATE VIEW v_active_employees AS
SELECT 
    e.id,
    e.employee_code,
    e.first_name,
    e.last_name,
    CONCAT(e.first_name, ' ', e.last_name) AS full_name,
    e.email,
    e.phone,
    e.designation,
    d.name AS department,
    e.joining_date,
    e.basic_salary,
    e.role
FROM employees e
LEFT JOIN departments d ON e.department_id = d.id
WHERE e.status = 'ACTIVE';

-- View: Leave balance summary
CREATE VIEW v_leave_balance AS
SELECT 
    e.id AS employee_id,
    e.first_name,
    e.last_name,
    COALESCE(SUM(CASE WHEN l.leave_type = 'ANNUAL' AND l.status = 'APPROVED' THEN DATEDIFF(l.end_date, l.start_date) + 1 ELSE 0 END), 0) AS annual_taken,
    COALESCE(SUM(CASE WHEN l.leave_type = 'SICK' AND l.status = 'APPROVED' THEN DATEDIFF(l.end_date, l.start_date) + 1 ELSE 0 END), 0) AS sick_taken,
    COALESCE(SUM(CASE WHEN l.leave_type = 'CASUAL' AND l.status = 'APPROVED' THEN DATEDIFF(l.end_date, l.start_date) + 1 ELSE 0 END), 0) AS casual_taken
FROM employees e
LEFT JOIN leave_requests l ON e.id = l.employee_id
WHERE e.status = 'ACTIVE'
GROUP BY e.id, e.first_name, e.last_name;

-- View: Monthly payroll summary
CREATE VIEW v_payroll_summary AS
SELECT 
    p.month,
    COUNT(DISTINCT p.employee_id) AS employee_count,
    SUM(p.net_salary) AS total_salary_paid,
    AVG(p.net_salary) AS average_salary,
    SUM(p.total_present_days) AS total_present_days,
    SUM(p.total_absent_days) AS total_absent_days
FROM payrolls p
GROUP BY p.month
ORDER BY p.month DESC;

-- =====================================================
-- COMMIT AND VERIFY
-- =====================================================

COMMIT;

-- Final verification
SELECT 'Database schema created successfully!' AS status;
SELECT COUNT(*) AS total_departments FROM departments;
SELECT COUNT(*) AS total_employees FROM employees;
SELECT COUNT(*) AS total_leave_requests FROM leave_requests;
SELECT COUNT(*) AS total_attendance_records FROM attendances;
SELECT COUNT(*) AS total_payroll_records FROM payrolls;
SELECT COUNT(*) AS total_history_records FROM employee_history;