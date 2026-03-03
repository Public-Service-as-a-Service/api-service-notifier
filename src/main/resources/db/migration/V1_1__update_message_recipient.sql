ALTER TABLE message_recipient
    DROP FOREIGN KEY fk_recipient_message;
ALTER TABLE message_recipient
    DROP FOREIGN KEY fk_recipient_employee;

ALTER TABLE message_recipient
    DROP PRIMARY KEY;

ALTER TABLE message_recipient
    ADD COLUMN id BIGINT AUTO_INCREMENT PRIMARY KEY FIRST;

ALTER TABLE message_recipient
    ADD CONSTRAINT fk_recipient_message
        FOREIGN KEY (message_id) REFERENCES message (message_id) ON DELETE CASCADE;
ALTER TABLE message_recipient
    ADD CONSTRAINT fk_recipient_employee
        FOREIGN KEY (employee_id) REFERENCES employee (employee_id) ON DELETE RESTRICT;