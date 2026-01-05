🚗 Sistema de Gestão de Veículos e Motores

Aplicação Android desenvolvida para o gerenciamento de veículos e motores, permitindo o cadastro, edição, produção e visualização de carros a partir de um fluxo simples e organizado.

📌 Funcionalidades

🔐 Autenticação de usuário (Login)

⚙️ Cadastro de motores

🚗 Cadastro de veículos

🏭 Edição e confirmação da produção dos carros

✅ Visualização de veículos concluídos

🔄 Fluxo da Aplicação

Login do usuário

Cadastro de motores

Cadastro de carros vinculados aos motores

Processo de produção (edição e finalização)

Listagem de veículos concluídos

🧱 Estrutura do Projeto

O projeto segue uma arquitetura organizada, separando responsabilidades para facilitar manutenção e escalabilidade:

Model → Representação das entidades e regras de negócio

View → Interfaces e telas da aplicação

ViewModel → Comunicação entre View e Model

🗄️ Estrutura do Banco de Dados
📋 Tabela carros
Campo	Descrição
id	Identificador do veículo
marca	Marca do carro
modelo	Modelo do carro
ano	Ano de fabricação
num_portas	Número de portas
cor	Cor do veículo
motor	Motor associado
status	Status do veículo
opcionais	Campos adicionais
⚙️ Tabela motores
Campo	Descrição
modelo	Identificador do motor
marca	Marca do motor
cilindrada	Cilindrada
potencia	Potência
torque	Torque
combustivel	Tipo de combustível
status	Status do motor
👤 Responsável pelo Projeto

José Vitor Gonçalves
Responsável por todo o desenvolvimento do sistema, incluindo:

Arquitetura do projeto

Implementação das camadas Model, View e ViewModel

Regras de negócio

Integração com banco de dados

🛠️ Tecnologias Utilizadas

Android Studio

Java / Kotlin

Banco de dados relacional

📄 Observações

O usuário deve estar autenticado para acessar as funcionalidades.

O sistema pode ser expandido futuramente com relatórios, histórico de produção e melhorias na interface.
