-- Create isolated databases for each microservice
-- Double quotes preserve the exact CamelCase capitalization in PostgreSQL

-- 1. Create the databases securely
CREATE DATABASE "ProductDB";
CREATE DATABASE "OrderDB";
CREATE DATABASE "UserDB";

-- 2. Force immediate connection permissions for your admin user
GRANT ALL PRIVILEGES ON DATABASE "ProductDB" TO postgres_admin;
GRANT ALL PRIVILEGES ON DATABASE "OrderDB" TO postgres_admin;
GRANT ALL PRIVILEGES ON DATABASE "UserDB" TO postgres_admin;