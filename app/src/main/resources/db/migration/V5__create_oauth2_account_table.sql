-- V5__create_oauth2_account_table.sql
-- Création de la table OAUTH2_ACCOUNT pour lier provider -> user

CREATE TABLE OAUTH2_ACCOUNT
(
    ID          BIGSERIAL PRIMARY KEY,
    PROVIDER    VARCHAR(100) NOT NULL,
    PROVIDER_ID VARCHAR(255) NOT NULL,
    USER_ID     UUID         NOT NULL,
    CONSTRAINT fk_oauth2_user FOREIGN KEY (USER_ID) REFERENCES APP_USER (ID) ON DELETE CASCADE,
    CONSTRAINT uq_provider_providerid UNIQUE (PROVIDER, PROVIDER_ID)
);

CREATE INDEX idx_oauth2_account_user_id ON OAUTH2_ACCOUNT (user_id);
