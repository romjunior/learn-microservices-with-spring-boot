CREATE DATABASE gamification_db;
CREATE USER gamification WITH ENCRYPTED PASSWORD 'gamification';
GRANT ALL PRIVILEGES ON DATABASE gamification_db TO gamification;

CREATE DATABASE gamification_db_test;
GRANT ALL PRIVILEGES ON DATABASE gamification_db_test TO gamification;