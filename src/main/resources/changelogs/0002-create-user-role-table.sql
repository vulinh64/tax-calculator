--liquibase formatted sql
--changeset linh.nguyen:20231009-0002
CREATE TABLE public.user_role (
	id varchar(50) NOT NULL,
	description text NULL,
	CONSTRAINT user_role_pk PRIMARY KEY (id)
);

INSERT INTO user_role
(id, description)
VALUES
('ADMIN', 'The administrator have full control over the system and access to all APIs'),
('USER', 'Normal user with limited privilege');
