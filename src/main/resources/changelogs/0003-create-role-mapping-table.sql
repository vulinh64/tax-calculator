--liquibase formatted sql
--changeset linh.nguyen:20231009-0003
CREATE TABLE role_mapping (
	user_id varchar(36) NOT NULL,
	user_role_id varchar(50) NOT NULL,
	CONSTRAINT role_mapping_pk PRIMARY KEY (user_id,user_role_id),
	CONSTRAINT role_mapping_fk FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT role_mapping_fk_1 FOREIGN KEY (user_role_id) REFERENCES user_role (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO role_mapping
(user_id, user_role_id)
VALUES
('2664e016-979c-47f4-9306-2c1dfae00914', 'ADMIN'),
('3e9f2192-c0fa-48c9-a1be-dcb5e26e70be', 'POWER_USER'),
('be4dcefc-a2b4-4daf-a45e-9bf3c3738cd2', 'USER');
