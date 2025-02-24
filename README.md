# ecopoints-system
Sistema de gerenciamento e coleta de resíduos recicláveis

## .env template
```bash
PG_HOST=ecopointsdb
PG_PORT=[Coloque a porta do Banco de Dados]
PG_DATABASE=[Coloque o nome do Banco de Dados]
PG_USER=[Coloque o nome do seu usuário do Banco de Dados]
PG_PASSWORD=[Coloque a senha Banco de Dados]
MAIL_SENDER_HOST=smtp.gmail.com
MAIL_SENDER_PORT=[Porta do serviço de email no computador]
MAIL_SENDER_USERNAME=[Username do email]
MAIL_SENDER_PASSWORD=[senha do email]
MAIL_SENDER_PROTOCOL=smtp
MAIL_SENDER_DEBUG=true
MAIL_SENDER_AUTH=true
MAIL_SENDER_STARTTLS_ENABLE=true
JWT_KEY=[String aleatória]
H2_DB_USERNAME=adevan
H2_DB_PASSWORD=123
SPRING_PROFILES_ACTIVE=prd
```

## Rodar os comandos

```bash
docker compose build
```

```bash
docker compose up
``
