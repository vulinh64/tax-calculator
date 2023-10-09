## 1. Initialize your database
* Default database name: `myspringsecurity`
* Default username: `myspringsecurity`
* Default password: `123456`

Run the application for the first time, and if you provided correct database connection properties, the liquibase will generate the tables and data for you.

### Database generation script:
```sql
CREATE ROLE myspringsecurity WITH
	LOGIN
	NOSUPERUSER
	NOCREATEDB
	NOCREATEROLE
	INHERIT
	NOREPLICATION
	CONNECTION LIMIT -1
	PASSWORD '123456';
	
CREATE DATABASE myspringsecurity
    WITH
    OWNER = notification
    ENCODING = 'UTF8'
    CONNECTION LIMIT = -1
    IS_TEMPLATE = False;
```

## 2. Environment variables

| Variable name | Detail                       | Remark                       |
|---------------|------------------------------|------------------------------|
| `DB_HOST`     | The host name for database   | Default: `localhost`         |
| `DB_PORT`     | The port number for database | Default: `5432` (PostgreSQL) |
| `DB_NAME`     | The database name            | Default: `myspringsecurity`  |
| `USERNAME`    | The database's username      | Default: `myspringsecurity`  |
| `PASSWORD`    | The database's password      | Default: `123456`            |
| `PUBLIC_KEY`  | RS256 public key             |                              |
| `PRIVATE_KEY` | RS256 private key            |                              |

Copy/paste this line to quick set the environment variables if you aim to use the above default value:
```
PRIVATE_KEY=your-rs256-private-key;PUBLIC_KEY=your-rs256-public-key;DB_HOST=localhost;DB_PORT=5432;DB_NAME=myspringsecurity;USERNAME=myspringsecurity;PASSWORD=123456
```