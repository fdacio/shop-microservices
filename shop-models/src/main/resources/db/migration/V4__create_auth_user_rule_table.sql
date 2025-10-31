-- auth.user_rule definition

-- Drop table

-- DROP TABLE auth.user_rule;

CREATE TABLE IF NOT EXISTS auth.user_rule (
	id bigserial NOT NULL,
	user_id int8 NOT NULL,
	rule_id int8 NOT NULL,
	CONSTRAINT user_rule_pkey PRIMARY KEY (id)
);


-- auth.user_rule foreign keys

ALTER TABLE auth.user_rule ADD CONSTRAINT user_rule_rule_id_fkey FOREIGN KEY (rule_id) REFERENCES auth."rule"(id);
ALTER TABLE auth.user_rule ADD CONSTRAINT user_rule_user_id_fkey FOREIGN KEY (user_id) REFERENCES auth."user"(id);