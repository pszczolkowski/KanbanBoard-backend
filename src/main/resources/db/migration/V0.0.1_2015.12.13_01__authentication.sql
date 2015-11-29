CREATE TABLE persistent_audit_event (
	event_id BIGINT (20) NOT NULL AUTO_INCREMENT,
	principal VARCHAR(255) NOT NULL,
	event_date TIMESTAMP NULL DEFAULT NULL,
	event_type VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (event_id),
	KEY idx_persistent_audit_event(principal, event_date)
	);

CREATE TABLE persistent_audit_evt_data (
	event_id BIGINT (20) NOT NULL,
	NAME VARCHAR(255) NOT NULL,
	value VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (
		event_id,
		NAME
		),
	KEY idx_persistent_audit_evt_data(event_id)
	);

CREATE TABLE user (
	id BIGINT (20) NOT NULL AUTO_INCREMENT,
	LOGIN VARCHAR(50) NOT NULL,
	PASSWORD VARCHAR(60) NOT NULL,
	username VARCHAR(20) NOT NULL,
	PRIMARY KEY (id),
	UNIQUE KEY LOGIN (LOGIN),
	UNIQUE KEY idx_user_login(LOGIN)
	);

CREATE TABLE oauth_access_token (
	token_id VARCHAR(255) DEFAULT NULL,
	token blob,
	authentication_id VARCHAR(255) NOT NULL,
	user_name VARCHAR(255) DEFAULT NULL,
	client_id VARCHAR(255) DEFAULT NULL,
	authentication blob,
	refresh_token VARCHAR(255) DEFAULT NULL,
	PRIMARY KEY (authentication_id),
	KEY fk_oauth_access_tokn_user_name(user_name)
	);

CREATE TABLE oauth_approvals (
	userId VARCHAR(255) DEFAULT NULL,
	clientId VARCHAR(255) DEFAULT NULL,
	scope VARCHAR(255) DEFAULT NULL,
	STATUS VARCHAR(255) DEFAULT NULL,
	expiresAt TIMESTAMP NULL DEFAULT NULL,
	lastModifiedAt TIMESTAMP NULL DEFAULT NULL
	);

CREATE TABLE oauth_client_details (
	client_id VARCHAR(255) NOT NULL,
	resource_ids VARCHAR(255) DEFAULT NULL,
	client_secret VARCHAR(255) DEFAULT NULL,
	scope VARCHAR(255) DEFAULT NULL,
	authorized_grant_types VARCHAR(255) DEFAULT NULL,
	web_server_redirect_uri VARCHAR(255) DEFAULT NULL,
	authorities VARCHAR(255) DEFAULT NULL,
	access_token_validity INT (11) DEFAULT NULL,
	refresh_token_validity INT (11) DEFAULT NULL,
	additional_information VARCHAR(4096) DEFAULT NULL,
	autoapprove VARCHAR(4096) DEFAULT NULL,
	PRIMARY KEY (client_id)
	);

CREATE TABLE oauth_client_token (
	token_id VARCHAR(255) DEFAULT NULL,
	token blob,
	authentication_id VARCHAR(255) DEFAULT NULL,
	user_name VARCHAR(255) DEFAULT NULL,
	client_id VARCHAR(255) DEFAULT NULL,
	KEY fk_oauth_client_tokn_user_name(user_name)
	);

CREATE TABLE oauth_code (code VARCHAR(255) DEFAULT NULL);

CREATE TABLE oauth_refresh_token (
	token_id VARCHAR(255) DEFAULT NULL,
	token blob,
	authentication blob
	);

ALTER TABLE persistent_audit_evt_data ADD CONSTRAINT fk_evt_pers_audit_evt_data FOREIGN KEY (event_id) REFERENCES persistent_audit_event (event_id);

ALTER TABLE oauth_access_token ADD CONSTRAINT fk_oauth_access_tokn_user_name FOREIGN KEY (user_name) REFERENCES user (LOGIN);

ALTER TABLE oauth_client_token ADD CONSTRAINT fk_oauth_client_tokn_user_name FOREIGN KEY (user_name) REFERENCES user (LOGIN);

