# Hospital Management API

Projeto desenvolvido para o **Tech Challenge – Fase 3 da Pós Tech FIAP**, com foco na construção de um backend modular para gerenciamento hospitalar.

O sistema permite o gerenciamento de usuários, pacientes, médicos e consultas, aplicando controle de acesso baseado em perfis, consultas flexíveis utilizando GraphQL e comunicação assíncrona entre serviços através do RabbitMQ.

## Objetivo

O objetivo do projeto é disponibilizar uma API backend segura e modular para gerenciamento de consultas hospitalares, permitindo:

- Cadastro de usuários, pacientes e médicos;
- Agendamento e alteração de consultas;
- Consulta do histórico de atendimentos;
- Controle de acesso de acordo com o perfil do usuário;
- Consulta de dados utilizando REST e GraphQL;
- Comunicação assíncrona para notificações relacionadas às consultas.

## Arquitetura

A aplicação foi dividida em dois serviços Spring Boot:

### agendamento-service

Serviço principal da aplicação, responsável por:

- Gerenciamento de usuários;
- Gerenciamento de pacientes;
- Gerenciamento de médicos;
- Criação e alteração de consultas;
- Autenticação e autorização;
- Persistência dos dados no PostgreSQL;
- Disponibilização da API REST;
- Consultas utilizando GraphQL;
- Publicação de eventos de consultas no RabbitMQ.

### notificacao-service

Serviço responsável por consumir de forma assíncrona os eventos publicados pelo serviço de agendamento.

Quando uma consulta é criada ou alterada, o `agendamento-service` publica um evento no RabbitMQ. O `notificacao-service` consome esse evento e simula o processamento de uma notificação através do log da aplicação.

## Diagrama da Arquitetura

```mermaid
flowchart LR
    C[Cliente / Postman] -->|REST / GraphQL + HTTP Basic| A[agendamento-service]
    A -->|JPA| P[(PostgreSQL)]
    A -->|AppointmentEvent| R[RabbitMQ]
    R -->|Consumo assíncrono| N[notificacao-service]


## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- Spring GraphQL
- Spring AMQP
- PostgreSQL
- RabbitMQ
- Docker / Docker Compose
- Maven
- Bean Validation
- Lombok
- Postman

Modelo de Domínio
A aplicação possui quatro entidades principais:
User
Representa a identidade utilizada para autenticação no sistema.
Principais atributos:
- id
- name
- email
- password
- role
Patient
Representa um paciente do hospital e possui relacionamento 1:1 com User.
Doctor
Representa um médico e possui relacionamento 1:1 com User.
Appointment
Representa uma consulta médica.
Cada consulta possui relacionamento com:
- um Patient;
- um Doctor.
Principais informações:
- paciente;
- médico;
- data e horário;
- status;
- observações.
Status das consultas
Os status disponíveis são:
- SCHEDULED
- COMPLETED
- CANCELED
Segurança
A aplicação utiliza Spring Security com HTTP Basic Authentication.
Os usuários são classificados através dos seguintes perfis:
Perfil	Descrição
DOCTOR	Médico
NURSE	Enfermeiro(a)
PATIENT	Paciente


As senhas são armazenadas utilizando BCrypt.
Regras de acesso
Médicos e enfermeiros podem criar e alterar consultas.
Pacientes podem consultar somente suas próprias consultas.
Tentativas de acesso de um paciente às consultas pertencentes a outro paciente são bloqueadas pela aplicação.
API REST
URL base:
http://localhost:8080
Usuários
POST /users
Realiza o cadastro de um novo usuário.
Pacientes
POST /patients
Cria o perfil de paciente associado a um usuário com perfil PATIENT.
Médicos
POST /doctors
Cria o perfil de médico associado a um usuário com perfil DOCTOR.
Consultas
Método	Endpoint	Descrição
POST	/appointments	Cria uma consulta
GET	/appointments	Lista consultas de acordo com o usuário autenticado
GET	/appointments/{id}	Busca uma consulta pelo ID
PUT	/appointments/{id}	Atualiza uma consulta


A criação e alteração de consultas são permitidas para os perfis DOCTOR e NURSE.
Quando o usuário autenticado possui o perfil PATIENT, a listagem é limitada às consultas pertencentes ao próprio paciente.
GraphQL
Endpoint:
POST /graphql
O GraphQL permite consultas flexíveis sobre os agendamentos.
Listar consultas
query {
  appointments {
    id
    patientId
    doctorId
    appointmentDateTime
    status
    notes
  }
}
Consultas de um paciente
query {
  appointmentsByPatient(patientId: 1) {
    id
    patientId
    doctorId
    appointmentDateTime
    status
    notes
  }
}
Consultas futuras de um paciente
query {
  futureAppointmentsByPatient(patientId: 1) {
    id
    patientId
    doctorId
    appointmentDateTime
    status
    notes
  }
}
As consultas GraphQL também respeitam as regras de autorização. Um usuário com perfil PATIENT não pode consultar os agendamentos pertencentes a outro paciente.
Comunicação Assíncrona
A comunicação entre os serviços é realizada através do RabbitMQ.
Fluxo:
1. Uma consulta é criada ou alterada no agendamento-service;
2. O serviço publica um AppointmentEvent;
3. O RabbitMQ recebe a mensagem;
4. O notificacao-service consome a mensagem;
5. O serviço de notificação simula o envio do lembrete através do log da aplicação.
Configuração RabbitMQ
Exchange:
appointment.exchange
Routing Key:
appointment.notification
Queue:
appointment.notification.queue
Eventos publicados:
- APPOINTMENT_CREATED
- APPOINTMENT_UPDATED
Executando o Projeto
Pré-requisitos
Antes de iniciar, é necessário possuir:
- Java 21;
- Docker Desktop;
- Docker Compose;
- Git.
O projeto utiliza Maven Wrapper, portanto não é obrigatório possuir o Maven instalado globalmente.
1. Iniciar a infraestrutura
Na raiz do projeto:
docker compose up -d
Verifique os containers:
docker ps
A infraestrutura disponibiliza:
Serviço	Porta
PostgreSQL	5432
RabbitMQ	5672
RabbitMQ Management	15672


O painel do RabbitMQ pode ser acessado em:
http://localhost:15672
Credenciais locais:
- usuário: guest
- senha: guest
2. Executar o agendamento-service
cd agendamento-service
./mvnw spring-boot:run
O serviço será disponibilizado em:
http://localhost:8080
3. Executar o notificacao-service
Em outro terminal:
cd notificacao-service
./mvnw spring-boot:run
O serviço ficará aguardando mensagens da fila do RabbitMQ.
PostgreSQL
Configuração utilizada no ambiente local:
- Database: agendamento_db
- Host: localhost
- Port: 5432
- Username: postgres
- Password: postgres
As tabelas são gerenciadas pelo Hibernate/JPA.
Credenciais para Testes
O ambiente utilizado durante o desenvolvimento possui usuários de demonstração:
Perfil	E-mail	Senha
Doctor	marcos@hospital.com	123456
Nurse	maria@hospital.com	123456
Patient	joao@email.com	123456


Essas credenciais são exclusivamente para demonstração e execução local do projeto.

Postman
A Collection do Postman está disponível no diretório:
postman/
Ela contém exemplos para:
- criação de usuários;
- criação de pacientes;
- criação de médicos;
- criação e atualização de consultas;
- listagem de consultas;
- acesso do paciente às próprias consultas;
- consultas GraphQL;
- teste de tentativa de acesso de um paciente aos dados de outro paciente.
Após importar a Collection no Postman, utilize as Collection Variables configuradas para executar os requests.
Estrutura do Projeto
hospitalapi-fase3-fiap/
├── agendamento-service/
│   └── src/
├── notificacao-service/
│   └── src/
├── postman/
├── docker-compose.yml
└── README.md

Build
Para validar o agendamento-service:
cd agendamento-service
./mvnw clean package
Para validar o notificacao-service:
cd notificacao-service
./mvnw clean package
Os dois serviços devem finalizar com:
BUILD SUCCESS
Autor
Projeto desenvolvido por Ricardo Freitas para o Tech Challenge da Pós Tech FIAP.