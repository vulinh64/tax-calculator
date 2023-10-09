--liquibase formatted sql
--changeset linh.nguyen:20231009-0003
CREATE TABLE role_mapping (
	user_id varchar(36) NOT NULL,
	user_role_id varchar(50) NOT NULL,
	CONSTRAINT role_mapping_pk PRIMARY KEY (user_id,user_role_id),
	CONSTRAINT role_mapping_fk FOREIGN KEY (user_id) REFERENCES "user" (id) ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT role_mapping_fk_1 FOREIGN KEY (user_role_id) REFERENCES user_role (id) ON DELETE CASCADE ON UPDATE CASCADE
);