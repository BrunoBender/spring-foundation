# Base Project: Java + Spring Boot

Este repositório contém um modelo base de arquitetura para projetos em Java utilizando Spring Boot. O objetivo é oferecer uma estrutura inicial para desenvolvimento de APIs RESTful, seguindo boas práticas e padrões de mercado.

---

## Tecnologias e Recursos Implementados

### REST e RESTful

- Construção de APIs seguindo os princípios RESTful.
- Endpoints organizados e estruturados para manipulação de recursos.

### Custom JSON Serialization

- Configurações personalizadas para serialização e desserialização de JSON.
- Suporte a formatos personalizados de resposta utilizando o Jackson.

### Content Negotiation

- Implementação de negociação de conteúdo com suporte a diferentes formatos de mídia (JSON, XML, etc.).
- Configuração de media types padrão.

### HATEOAS

- Inclusão de links auto-descritivos em respostas, facilitando a navegação pelas APIs.
- Implementação com suporte ao Spring HATEOAS.

### Swagger (OpenAPI)

- Documentação interativa da API utilizando Swagger.
- Interface para teste e exploração dos endpoints.

### CORS (Cross-Origin Resource Sharing)

- Configuração para permitir ou restringir requisições de diferentes origens.
- Configurações flexíveis para ambientes de desenvolvimento e produção.

### JWT e Spring Security

- Implementação de autenticação e autorização baseada em JSON Web Tokens (JWT).
- Configuração de segurança com Spring Security para proteger os endpoints.

### Testes: Rest Assured, JUnit e Mockito

- **Rest Assured**: Testes de integração para validar os endpoints da API.
- **JUnit**: Testes unitários para métodos e serviços.
- **Mockito**: Mock de dependências para isolamento de testes.
