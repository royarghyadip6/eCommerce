-- 1. Create the Database Schema if it doesn't exist
CREATE DATABASE IF NOT EXISTS ProductDB;
CREATE DATABASE IF NOT EXISTS OrderDB;
CREATE DATABASE IF NOT EXISTS UserDB;

-- ========================================================================
-- RUNTIME INITIALIZATION SCRIPT FOR MYSQL CONTAINER
-- Includes unified global property configurations and build property vectors.
-- ========================================================================

CREATE DATABASE IF NOT EXISTS ConfigDB;
USE ConfigDB;

DROP TABLE IF EXISTS config_properties;

CREATE TABLE config_properties (
                                   id          BIGINT AUTO_INCREMENT PRIMARY KEY,
                                   application VARCHAR(128) NOT NULL,
                                   profile     VARCHAR(128) NOT NULL,
                                   label       VARCHAR(128) NOT NULL,
                                   prop_key    VARCHAR(128) NOT NULL,
                                   prop_value  TEXT,
                                   created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   CONSTRAINT uc_app_profile_label_key UNIQUE (application, profile, label, prop_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE INDEX idx_config_fetch ON config_properties (application, profile, label);

-- ========================================================================
-- 1. PROFILE: default (Global Application Fallbacks)
-- ========================================================================
INSERT INTO config_properties (application, profile, label, prop_key, prop_value) VALUES
                                                                                      ('config', 'default', 'master', 'server.port', '8080'),
                                                                                      ('config', 'default', 'master', 'management.endpoints.web.exposure.include', 'health,info,refresh'),
-- Spring Boot Application Build @Value Fallback Defaults
                                                                                      ('config', 'default', 'master', 'build.id', 'default-id'),
                                                                                      ('config', 'default', 'master', 'build.owner', 'default-owner'),
                                                                                      ('config', 'default', 'master', 'build.name', 'default-name'),
                                                                                      ('config', 'default', 'master', 'build.version', 'default-version');

-- ========================================================================
-- 2. PROFILE: dev (Local Development Context)
-- ========================================================================
INSERT INTO config_properties (application, profile, label, prop_key, prop_value) VALUES
                                                                                      ('config', 'dev', 'master', 'server.port', '8084'), -- Updated to run on 8084 as requested
                                                                                      ('config', 'dev', 'master', 'spring.datasource.url', 'jdbc:mysql://localhost:3306/ConfigDB?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true'),
                                                                                      ('config', 'dev', 'master', 'spring.datasource.username', 'root'),
                                                                                      ('config', 'dev', 'master', 'spring.datasource.password', 'rootpassword'),
                                                                                      ('config', 'dev', 'master', 'spring.jpa.hibernate.ddl-auto', 'update'),
-- Build Profile Details for Dev
                                                                                      ('config', 'dev', 'master', 'build.id', 'BUILD-DEV-0412'),
                                                                                      ('config', 'dev', 'master', 'build.owner', 'developer-team-local'),
                                                                                      ('config', 'dev', 'master', 'build.name', 'ecommerce-user-service-dev'),
                                                                                      ('config', 'dev', 'master', 'build.version', '1.0.0-SNAPSHOT');

-- ========================================================================
-- 3. PROFILE: test (Continuous Integration / QA Environment)
-- ========================================================================
INSERT INTO config_properties (application, profile, label, prop_key, prop_value) VALUES
                                                                                      ('config', 'test', 'master', 'server.port', '8081'),
                                                                                      ('config', 'test', 'master', 'spring.datasource.url', 'jdbc:mysql://test-db-cluster:3306/UserDB_Test?useSSL=false'),
-- Build Profile Details for Test (CI/CD Automated System Builds)
                                                                                      ('config', 'test', 'master', 'build.id', 'JENKINS-JOB-884'),
                                                                                      ('config', 'test', 'master', 'build.owner', 'automation-ci'),
                                                                                      ('config', 'test', 'master', 'build.name', 'ecommerce-user-service-test'),
                                                                                      ('config', 'test', 'master', 'build.version', '1.0.0-RC1');

-- ========================================================================
-- 4. PROFILE: prod (Production Environment)
-- ========================================================================
INSERT INTO config_properties (application, profile, label, prop_key, prop_value) VALUES
                                                                                      ('config', 'prod', 'master', 'server.port', '80'),
                                                                                      ('config', 'prod', 'master', 'spring.datasource.url', 'jdbc:mysql://prod-cluster.internal:3306/UserDB_Prod'),
                                                                                      ('config', 'prod', 'master', 'spring.jpa.hibernate.ddl-auto', 'none'),
-- Build Profile Details for Production
                                                                                      ('config', 'prod', 'master', 'build.id', 'RELEASE-2026-V1'),
                                                                                      ('config', 'prod', 'master', 'build.owner', 'release-operations-manager'),
                                                                                      ('config', 'prod', 'master', 'build.name', 'ecommerce-user-service-prod'),
                                                                                      ('config', 'prod', 'master', 'build.version', '1.0.0-RELEASE');

-- ========================================================================
-- 5. PERMISSIONS MAPPING
-- ========================================================================
-- Ensures background connection applications are allowed to query without root blocks
-- Uncomment the line below if you re-enable app_user inside your docker-compose environment variables
-- GRANT ALL PRIVILEGES ON ConfigDB.* TO 'app_user'@'%';
FLUSH PRIVILEGES;