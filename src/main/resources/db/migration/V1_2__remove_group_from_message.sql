ALTER TABLE message
    DROP FOREIGN KEY fk_message_group;
ALTER TABLE message
    DROP COLUMN group_id;