# init-collation.sh
# shellcheck disable=SC1128
#!/bin/bash
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" <<-EOSQL
    ALTER DATABASE postgres REFRESH COLLATION VERSION;
    ALTER DATABASE shop REFRESH COLLATION VERSION;
EOSQL
