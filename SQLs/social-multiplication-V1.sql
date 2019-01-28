CREATE DATABASE social_multiplication_db;
CREATE USER social_multiplication WITH ENCRYPTED PASSWORD 'social_multiplication';
GRANT ALL PRIVILEGES ON DATABASE social_multiplication_db TO social_multiplication;