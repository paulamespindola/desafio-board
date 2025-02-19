--liquibase formatted sql
--changeset junior:202502190004
--comment: set unblock_reason nullable

ALTER TABLE BLOCKS ALTER COLUMN unblock_reason DROP NOT NULL;

--rollback ALTER TABLE BLOCKS ALTER COLUMN unblock_reason SET NOT NULL;
