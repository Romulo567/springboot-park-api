# ParkSystem API 🚗

![Java](https://img.shields.io/badge/Java-17%2B-blue?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x.x-green?style=for-the-badge&logo=spring)
![Security](https://img.shields.io/badge/Security-Spring%20%26%20JWT-red?style=for-the-badge)
![Status](https://img.shields.io/badge/Status-Concluído-brightgreen?style=for-the-badge)

> API RESTful completa para gerenciamento de estacionamentos, desenvolvida com Java e o ecossistema Spring.

Este projeto foi criado como uma demonstração prática das minhas habilidades em desenvolvimento back-end, focando em criar uma aplicação robusta, segura e com boas práticas de mercado, pronta para um ambiente de produção.

---

## ✨ Features Principais

-   **Autenticação e Autorização:** Sistema seguro utilizando Spring Security e tokens JWT para proteger os endpoints.
-   **Controle de Acesso por Perfil:** Endpoints com acesso restrito para diferentes tipos de usuários (`ROLE_ADMIN`, `ROLE_CLIENTE`).
-   **Operações de Estacionamento:** Funcionalidades completas para `check-in` e `check-out` de veículos.
-   **Gestão de Entidades:** CRUDs completos para Usuários, Clientes e Vagas.
-   **Validação de Dados:** Utilização do Bean Validation para garantir a integridade dos dados de entrada.
-   **Documentação Interativa:** Geração automática da documentação da API com SpringDoc (OpenAPI 3) e Swagger UI.
-   **Geração de Relatórios:** Endpoint para emissão de relatórios em formato PDF com JasperReports.
-   **Testes End-to-End:** Testes automatizados para garantir a estabilidade e o correto funcionamento da API.

---

## 💻 Tecnologias Utilizadas

Abaixo estão as principais tecnologias e ferramentas usadas no desenvolvimento da ParkSystem API:

| Categoria     | Tecnologia/Ferramenta                                  |
|---------------|--------------------------------------------------------|
| **Back-end** | Java 17, Spring Boot 3                                 |
| **Segurança** | Spring Security, JWT (JSON Web Token)                  |
| **Dados** | Spring Data JPA, H2 Database (para testes), MySql |
| **Validação** | Jakarta Bean Validation                                |
| **Build** | Apache Maven                                           |
| **Documentação**| SpringDoc (OpenAPI 3) & Swagger UI                     |
| **Testes** | JUnit 5, WebTestClient                                 |
| **Relatórios**| JasperReports                                          |

---

## 🚀 Como Executar o Projeto

Siga os passos abaixo para ter uma instância do projeto rodando localmente na sua máquina.

### **Pré-requisitos**

Antes de começar, você vai precisar ter instalado em sua máquina:
-   [JDK (Java Development Kit)](https://www.oracle.com/java/technologies/downloads/) - Versão 17 ou superior.
-   [Apache Maven](https://maven.apache.org/download.cgi) - Gerenciador de dependências.
-   Um cliente de API como [Postman](https://www.postman.com/) ou [Insomnia](https://insomnia.rest/).

### **Passo a Passo**

1.  **Clone o repositório:**
    ```sh
    git clone [https://github.com/Romulo567/springboot-park-api.git]
    ```

2.  **Acesse a pasta do projeto:**
    ```sh
    cd springboot-park-api
    ```

3.  **Configure o banco de dados (se necessário):**
    O projeto está configurado para usar um banco de dados MySql por padrão. Se quiser usar um banco de dados como o PostgreSQL, ajuste as configurações no arquivo `src/main/resources/application.properties`.

4.  **Execute a aplicação:**
    ```sh
    mvn spring-boot:run
    ```

5.  **Pronto!** A API estará disponível em `http://localhost:8080`.

---

## 📚 Endpoints e Uso da API

Após iniciar a aplicação, você pode acessar a documentação completa e interativa do Swagger para testar todos os endpoints:

➡️ **[http://localhost:8080/docs-park.html]

### **Exemplos de Rotas:**

| Método | Endpoint                             | Descrição                            | Acesso          |
|--------|--------------------------------------|--------------------------------------|-----------------|
| `POST` | `/api/v1/usuarios`                   | Cria um novo usuário.                | `ROLE_ADMIN`    |
| `POST` | `/api/v1/auth`                       | Autentica um usuário e retorna um JWT. | Público         |
| `POST` | `/api/v1/clientes`                   | Cria um novo cliente.                | `ROLE_ADMIN`    |
| `POST` | `/api/v1/estacionamento/check-in`    | Realiza o check-in de um veículo.    | `ROLE_CLIENTE`  |
| `GET`  | `/api/v1/estacionamento/{recibo}`    | Consulta os dados de um check-in.    | `ROLE_ADMIN`    |
