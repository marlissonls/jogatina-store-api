# Jogatina Store

Projeto de exemplo com Spring Boot, JPA e MySQL.

## Descrição
Aplicação backend para gerenciar dados de uma loja de jogos.  
Inclui endpoints REST e persistência em banco MySQL.

## Tecnologias
- Java 17+
- Spring Boot 3.4.0
- Spring Data JPA
- MySQL
- Maven

## Como rodar
1. Clone o repositório:
    ```bash
    git clone <url-do-repo>
    ```
2. Configure o banco de dados em <b><i>resources.application</i></b>:
    ```bash
    spring
      datasource
        url=jdbc:mysql://localhost:3306/jogatina_store
        username=root
        password=senha
    ```
3. Rode a aplicação:
    ```bash
    mvn spring-boot:run
    ```
4. Endpoints principais
- GET /api/users/v1 → lista usuários
- POST /api/users/v1 → cria usuário
