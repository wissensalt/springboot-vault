#!/usr/bin/env bash

while ! curl http://localhost:8200/v1/sys/health | grep "\"initialized\":true" --silent &> /dev/null ; do
  echo "Waiting for vault connection..."
  sleep 1
done

# Login into Vault
docker-compose exec vault /bin/vault login default-root-token

while ! docker exec mysql mysqladmin --user=user --password=password --host mysql ping --silent &> /dev/null ; do
  echo "Waiting for database connection..."
  sleep 1
done

# Enable Database Secret Engine
docker-compose exec vault /bin/vault secrets enable database

# Create Database Mysql Config
docker-compose exec vault /bin/vault write database/config/mysql \
plugin_name=mysql-database-plugin \
connection_url="{{username}}:{{password}}@tcp(mysql:3306)/employee_db" \
allowed_roles="app-role" \
username="root" \
password="root"

# Create Role
docker-compose exec vault /bin/vault write database/roles/app-role \
db_name=mysql \
creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}';GRANT ALL PRIVILEGES ON *.* TO '{{name}}'@'%';" \
default_ttl="1h" \
max_ttl="24h"

# Generate Credential
docker-compose exec vault /bin/vault read database/creds/app-role

# Setup Key Value Secret
docker-compose exec vault /bin/vault kv put secret/springboot-vault-app \
thirdparty.id="123456" \
thirdparty.name="Example Third Party Application" \
thirdparty.secret="Example Secret"

# Enable Transit Secret Engine
docker-compose exec vault /bin/vault secrets enable transit

# Create Encryption Key Ring
docker-compose exec vault /bin/vault write -f transit/keys/app

# Create a policy springboot-vault
docker-compose exec -T vault /bin/vault policy write app-encryption -<<EOF
path "transit/encrypt/app" {
   capabilities = [ "update" ]
}
path "transit/decrypt/app" {
   capabilities = [ "update" ]
}
EOF

# Install Json Formatter Inside Vault Container
docker-compose exec vault apk --no-cache add jq

# Create Token with policy springboot-vault
VAULT_TOKEN=$(docker-compose exec vault /bin/vault token create -format=json -policy=app-encryption | jq -r ".auth.client_token")

echo $VAULT_TOKEN