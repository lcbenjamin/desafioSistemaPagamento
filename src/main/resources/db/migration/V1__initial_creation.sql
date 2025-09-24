CREATE TABLE wallets (
  id BIGSERIAL PRIMARY KEY,
  balance NUMERIC(18,2) NOT NULL DEFAULT 0,
  version BIGINT
);

CREATE TABLE wallet_holders (
  id BIGSERIAL PRIMARY KEY,
  full_name TEXT NOT NULL,
  document VARCHAR(14) NOT NULL UNIQUE,
  email TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  kind VARCHAR(20) NOT NULL,
  document_type VARCHAR(20) NOT NULL,
  wallet_id BIGINT UNIQUE,
  CONSTRAINT fk_wallet FOREIGN KEY (wallet_id) REFERENCES wallets(id)
);

CREATE TABLE transfers (
  id BIGSERIAL PRIMARY KEY,
  payer_id BIGINT NOT NULL REFERENCES wallet_holders(id),
  payee_id BIGINT NOT NULL REFERENCES wallet_holders(id),
  amount NUMERIC(18,2) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMPTZ DEFAULT now(),
  finished_at TIMESTAMPTZ,
  failure_reason TEXT
);
