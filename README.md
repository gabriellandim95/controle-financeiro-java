# Controle Financeiro API-Rest Java (Em Desenvolvimento)

### Features:
- Cadastro e controle de contas.
- Verificação de contas vencidas ou que irão vencer alterando seus status automaticamente.
- Cadastro e controle de carteiras.
- Pagamentos.
- Historico de Pagamentos.
- Cotação de moedas.



### Tecnologias/Configurações utilizadas:
- Java 17
- Spring Boot 3.1.1
- MySql
- Envers (auditoria)
- Maven
- Autenticação JWT (Bearer) e encriptação Bcrypt.
- DDL-AUTO no modo "update", o gerenciamento das entidades serão realizadas pelo hibernate.
- Log de eventos (ex: usuário visualizou alguma tela, a api registra este evento no banco).
- Documentação: http://localhost:8080/api/controlefinanceiro/swagger-ui/index.html#/

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.1.2/maven-plugin/reference/html/#build-image)
