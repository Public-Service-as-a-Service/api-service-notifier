INSERT INTO organization(company_id, parent_org_id, org_id, org_name, tree_level)
VALUES ('COMP', null, 'ORG-1', 'Org 1', 1);

INSERT INTO employee(employee_id, person_id, org_id, first_name, last_name, email,
                     work_mobile, work_phone, work_title, active_employee, manager_id, manager_code)
VALUES (1, 'p1', 'ORG-1', 'Test', 'One', 'test@sundsvall.se', NULL, NULL, 'Developer', TRUE, NULL, NULL),
       (2, 'p2', 'ORG-1', 'Test', 'Two', 'test2@sundsvall.se', NULL, NULL, 'Developer', TRUE, NULL, NULL),
       (3, 'p3', 'ORG-1', 'Test', 'Three', 'test3@sundsvall.se', NULL, NULL, 'Developer', TRUE, NULL, NULL);

INSERT INTO message(message_id, title, content, sender, group_id, created_at)
VALUES (1, 'Test titel', 'Test content', 'test@sundsvall.se', NULL, '2026-02-17 16:00:00.000000'),
       (2, 'Test titel2', 'Test content2', 'test2@sundsvall.se', NULL, '2026-02-17 16:00:00.000000'),
       (3, 'Test titel3', 'Test content3', 'test3@sundsvall.se', NULL, '2026-02-17 16:00:00.000000');
