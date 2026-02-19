INSERT INTO organization(company_id, parent_org_id, org_id, org_name, tree_level)
VALUES ('COMP', null, 'ORG-1', 'Org 1', 1),
       ('COMP', null, 'ORG-2', 'Org 2', 1);

INSERT INTO employee(employee_id, person_id, org_id, first_name, last_name, email,
                     work_mobile, work_phone, work_title, active_employee, manager_id, manager_code)
VALUES (1, 'p1', 'ORG-1', 'John', 'One', 'test@sundsvall.se', '0701234567', '0701234567', 'Developer', TRUE, NULL, NULL),
       (2, 'p2', 'ORG-1', 'Jane', 'Two', 'test2@sundsvall.se', '0701234567', '0701234567', 'Developer', TRUE, NULL, NULL),
       (3, 'p3', 'ORG-2', 'Kalle', 'Three', 'test3@sundsvall.se', '0701234567', '0701234567', 'Developer', TRUE, NULL, 'A');
