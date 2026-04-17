CREATE TABLE bank_accounts (
    id          UUID        NOT NULL PRIMARY KEY,
    user_id     UUID        NOT NULL REFERENCES users(id),
    iban        VARCHAR(34) NOT NULL,
    provider    VARCHAR(100) NOT NULL,
    consent_id  VARCHAR(255) NOT NULL,
    is_active   BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL
);

CREATE INDEX idx_bank_accounts_user_id ON bank_accounts (user_id);
