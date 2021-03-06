START TRANSACTION;

DROP DATABASE IF EXISTS biometrie;
CREATE DATABASE biometrie;
USE biometrie;

CREATE TABLE users (
  id                INTEGER NOT NULL AUTO_INCREMENT,
  firstName         VARCHAR(50),
  lastName          VARCHAR(50),
  email             VARCHAR(50),
  is_admin          BOOLEAN,
  x                 INTEGER,
  y                 INTEGER,
  password          VARCHAR(100),
  biometric_data    VARCHAR(1000),

  PRIMARY KEY (id)
);

ALTER TABLE users AUTO_INCREMENT = 100001;

COMMIT;
