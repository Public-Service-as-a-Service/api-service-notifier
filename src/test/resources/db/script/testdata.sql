INSERT INTO organization(company_id, parent_org_id, org_id, org_name, tree_level)
VALUES ('COMP', NULL, 'ORG-1', 'Org 1', 1),
       ('COMP', 'ORG-1', 'ORG-2', 'Org 2', 2),
       ('COMP', 'ORG-1', 'ORG-3', 'Org 3', 2);

INSERT INTO employee(employee_id, person_id, org_id, first_name, last_name, email,
                     work_mobile, work_phone, work_title, active_employee, manager_id, manager_code)
VALUES (1, 'p1', 'ORG-1', 'Test', 'One', 'test1@sundsvall.se', NULL, NULL, 'Developer', TRUE, NULL, 'A_'),
       (2, 'p2', 'ORG-2', 'Test', 'Two', 'test2@sundsvall.se', NULL, NULL, 'Developer', TRUE, 'p1', 'B_'),
       (3, 'p3', 'ORG-3', 'Test', 'Three', 'test3@sundsvall.se', NULL, NULL, 'Manager', TRUE, 'p1', 'C_');

INSERT INTO user_group(group_id, group_name, description, creator_id, created_at)
VALUES (1, 'G1', 'Group 1', 'creator-123', '2026-02-17 16:00:00.000000'),
       (2, 'G2', 'Group 2', 'creator-123', '2026-02-17 16:01:00.000000'),
       (3, 'G3', 'Group 3', 'creator-999', '2026-02-17 16:02:00.000000');

INSERT INTO employee_user_group(employee_id, group_id)
VALUES (1, 1),
       (2, 1),
       (3, 2);

INSERT INTO message(message_id, title, content, sender, group_id, message_type, created_at)
VALUES (1, 'Test title', 'Test content', 'test@sundsvall.se', 1, 'SMS', '2026-02-17 16:00:00.000000'),
       (2, 'Test title2', 'Test content2', 'test2@sundsvall.se', NULL, 'SMS', '2026-02-17 16:01:00.000000'),
       (3, 'Test title3', 'Test content3', 'test3@sundsvall.se', 2, 'TEAMS', '2026-02-17 16:02:00.000000');

INSERT INTO message_recipient(message_id, employee_id, org_id, work_title, received_at, delivery_status)
VALUES (2, 1, 'ORG-1', 'Developer', '2026-02-17 16:00:10.000000', 'DELIVERED'),
       (2, 2, 'ORG-2', 'Developer', '2026-02-17 16:00:20.000000', 'DELIVERED'),
       (3, 3, 'ORG-3', 'Manager', '2026-02-17 16:02:10.000000', 'FAILED');