## Banco de dados

### Extenção nescessaria

Para começar crie o banco de dados notification e adiciona a extenção hstore:

```roomsql
CREATE EXTENSION hstore SCHEMA "public";
```

### Schema general

```roomsql
CREATE SCHEMA "general" AUTHORIZATION postgres;;
```

### Tabela de recurso

```roomsql
CREATE TABLE "general".email (
	"type" varchar(20) NOT NULL,
	body varchar NOT NULL,
	enterprise varchar(100) NOT NULL,
	CONSTRAINT email_pk PRIMARY KEY (type, enterprise)
);
```