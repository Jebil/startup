CREATE DATABASE "startup-system";

CREATE USER "pg-system-user" WITH PASSWORD 'pgpassword';

GRANT ALL PRIVILEGES ON DATABASE "startup-system" TO "pg-system-user";
