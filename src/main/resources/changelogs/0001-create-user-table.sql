--liquibase formatted sql
--changeset linh.nguyen:20231009-0001
CREATE TABLE "user" (
	id varchar(36) NOT NULL,
	username varchar(50) NOT NULL,
	"password" varchar(60) NOT NULL,
	is_disabled boolean NULL DEFAULT false,
	description text NULL,
	CONSTRAINT user_pk PRIMARY KEY (id),
	CONSTRAINT user_un UNIQUE (username)
);

COMMENT ON COLUMN "user".id IS 'An UUID string represents user ID';
COMMENT ON COLUMN "user"."password" IS 'A 60-characters hashed password from the original one';

INSERT INTO "user"
(id, username, "password", description)
VALUES
('2664e016-979c-47f4-9306-2c1dfae00914','admin','$2a$12$u5ubM2QlS7jie9GfWBNT1.G/zNRGysr/.F3vVwGBoSA0RmCTeChPu','admin default user with password 123456'),
('3e9f2192-c0fa-48c9-a1be-dcb5e26e70be','power_user','$2a$12$sFTH8Pbf7TOEYc0p75gdCelLwIlSZRr.0sfgohuPstgOvEZYTIL0m','power user that can manage other users'),
('be4dcefc-a2b4-4daf-a45e-9bf3c3738cd2','user','$2a$12$vCeyZ8wZ1SFLHFynIyWEF.2Sh/i.RAYM9uOwm/9XYMSx3F6IJAocW','normal user with limited privilege');
