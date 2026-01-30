create table shedlock
(
    name       varchar(64)  not null,
    lock_until timestamp(3) not null,
    locked_at  timestamp(3) not null default current_timestamp(3),
    locked_by  varchar(255) not null,
    primary key (name)
);

CREATE TABLE organization(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id VARCHAR(64),
    parent_org_id VARCHAR(64),
    org_id VARCHAR(64),
    org_name VARCHAR(255) NOT NULL,
    tree_level INT NOT NULL,

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uk_organization_org_id UNIQUE (org_id),
    CONSTRAINT fk_org_parent FOREIGN KEY(parent_org_id)
                         REFERENCES organization(org_id)
                         ON DELETE SET NULL
);

CREATE TABLE employee(
    employee_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    person_id VARCHAR(36) NOT NULL,
    org_id VARCHAR (64),
    first_name VARCHAR(100),
    last_name VARCHAR(100),
    email VARCHAR(255),
    work_mobile VARCHAR(50),
    work_phone VARCHAR(50),
    work_title VARCHAR(100),
    active_employee BOOLEAN NOT NULL DEFAULT TRUE,
    manager_id VARCHAR(36),
    manager_code VARCHAR(10),

    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT uq_pid_org_title UNIQUE(person_id, org_id, work_title),

    CONSTRAINT fk_employee_org FOREIGN KEY (org_id)
        REFERENCES organization(org_id)
        ON DELETE RESTRICT
);

CREATE TABLE user_group(
    group_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    group_name VARCHAR(255) NOT NULL,
    description TEXT,
    creator_employee_id BIGINT NOT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_group_creator FOREIGN KEY (creator_employee_id)
                       REFERENCES employee(employee_id)
                       ON DELETE RESTRICT
);

CREATE TABLE member(
    employee_id BIGINT NOT NULL,
    group_id BIGINT NOT NULL,
    joined_at DATETIME,
    PRIMARY KEY (employee_id, group_id),
    CONSTRAINT fk_member_employee FOREIGN KEY (employee_id)
                   REFERENCES employee(employee_id)
                   ON DELETE CASCADE,
    CONSTRAINT fk_member_group FOREIGN KEY (group_id)
                   REFERENCES user_group(group_id)
                   ON DELETE CASCADE
);

CREATE TABLE message(
    message_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT NOT NULL,
    sender_employee_id BIGINT NOT NULL,
    group_id BIGINT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_sender FOREIGN KEY (sender_employee_id)
                    REFERENCES employee(employee_id)
                    ON DELETE RESTRICT,
    CONSTRAINT fk_message_group FOREIGN KEY (group_id)
                    REFERENCES user_group(group_id)
                    ON DELETE SET NULL
);

CREATE TABLE message_recipient (
        message_id BIGINT NOT NULL,
        employee_id BIGINT NOT NULL,

        org_id BIGINT NOT NULL,
        work_title VARCHAR(100),

        received_at DATETIME DEFAULT CURRENT_TIMESTAMP,
        delivery_status ENUM('DELIVERED', 'FAILED')
        NOT NULL DEFAULT 'DELIVERED',

        PRIMARY KEY (message_id, employee_id),

        CONSTRAINT fk_recipient_message FOREIGN KEY (message_id)
                    REFERENCES message(message_id)
                    ON DELETE CASCADE,

        CONSTRAINT fk_recipient_employee FOREIGN KEY (employee_id)
                    REFERENCES employee(employee_id)
                    ON DELETE RESTRICT
);

