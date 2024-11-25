# DESTAXA-SERVER

DESCRIÇÃO

O DESTAXA-SERVER é o backend responsável por processar mensagens financeiras no formato ISO8583. Ele atua como um 
servidor socket TCP, escutando requisições do cliente e aplicando regras de negócio para aprovar, rejeitar ou simular o
timeout de transações.

# FUNCIONALIDADE

SERVIDOR SOCKET TCP:

Escuta conexões na porta configurada (por padrão, 5000).

Recebe e processa mensagens ISO8583 enviadas pelo cliente.

Envia respostas no formato ISO8583 de acordo com as regras de negócio.

REGRAS DE NEGÓCIO:

Transações com valor positivo ≤ R$ 1000 → Aprovadas (000).

Transações com valor negativo → Rejeitadas (051).

Transações com valor > R$ 1000 → Timeout Simulado (sem resposta enviada).

# TECNOLOGIAS UTILIZADAS

Java 17: Linguagem base do projeto.

ISO8583: Protocolo financeiro para mensagens de transações.

Maven: Gerenciador de dependências e build.

# PRÉ-REQUISITOS

Antes de executar o projeto, certifique-se de que você possui:
Java 17 ou superior e o 
Maven instalado.

# COMO CONFIGURAR E EXECUTAR

1. Clone o Repositório:
https://github.com/soldiereyes/destaxa-server

2. Entre na pasta do projeto: cd destaxa-server

3. Configure a Porta do Servidor (opcional):

    Por padrão, o servidor escuta na porta 5000.
    Para alterar a porta, edite o arquivo application.properties:

    server.port=5000

4. Build do Projeto: mvn clean package

5. Execute o Servidor: java -jar target/server-0.0.1-SNAPSHOT.jar
6. Verifique se o servidor está escutando na porta correta:
nc -zv localhost 5000