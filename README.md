# springboot-vault

vault secrets enable -path=secret/ kv
vault kv put secret/springboot-vault-app db.username=user db.password=password
vault kv put secret/springboot-vault-app db.password=password

vault login default-root-token

vault secrets enable database

vault write database/config/mysql \
plugin_name=mysql-database-plugin \
connection_url="{{username}}:{{password}}@tcp(mysql:3306)/employee_db" \
allowed_roles="app-role" \
username="root" \
password="root"

vault write database/roles/app-role \
db_name=mysql \
creation_statements="CREATE USER '{{name}}'@'%' IDENTIFIED BY '{{password}}';GRANT ALL PRIVILEGES ON *.* TO '{{name}}'@'%';" \
default_ttl="1h" \
max_ttl="24h"

vault read database/creds/app-role