# 🎬 PUCFlix 3..0

PUCFlix é um projeto desenvolvido para o Trabalho Prático 3 da disciplina de **Algoritmos e Estruturas de Dados 3 (AEDS 3)** na **PUC Minas**. Esta versão expande o sistema desenvolvido no TP1 e TP2, implementando o **Índice invertido**, com o uso de **Árvores Invertidas** para garantir consultas eficientes e consistência dos dados.

## 🚀 Funcionalidades

Nesta nova versão, o PUCFlix mantém as operações de **CRUD** para séries, episódios, e atores, além de adicionar:

### Relacionamento N:N: Séries ↔ Atores
- Um ou mais atores podem participar de uma ou mais séries.
- Atores são entidades independentes, com ID e nome.
- Atores vinculados a séries não podem ser excluídos.
- Séries excluídas também removem seus vínculos com os atores.
- Implementação de duas **Árvores B+** par indexar:
  - (idSerie; idAtor)
  - (idAtor; idSerie)

### Novos Menus e Integrações
- Novo menu de **Atores**: permite o CRUD completo e exibe séries vinculadas.
- No menu de **Séries**, é possível vincular e visualizar os atores participantes.
- A estrutura do projeto segue o padrão **MVC (Model-View-Controller)**.

## 👥 Equipe de Desenvolvimento
- **Vitor Leite Setragni**
- **Gabriel Henrique de Morais**
- **Mateus Martins Parreiras**

## � Estrutura do Projeto

### 📁 Modelos (Entidade)
- **Serie.java**: Define a série com seus dados principais.
- **Episodio.java**: Define os episódios vinculados a séries.
- **Ator.java**: Define um ator com ID e nome.
- **ArquivoSeries.java**: Estrutura para armazenar os vínculos entre séries e atores, incluindo o ID da ligação entre ambas as partes.

### 📁 Visão (Menus)
- **MenuSeries.java**: Exibição e leitura de dados de séries e seus atores.
- **MenuEpisodios.java**: Exibição e leitura de episódios.
- **MenuAtores.java**: Exibição e leitura de atores e suas séries.

### 📁 Controle (Arquivos)
- **ArquivoSeries.java**: Gerencia o CRUD de séries e seus vínculos com atores.
- **ArquivoEpisodios.java**: Gerencia o CRUD de episódios, mantendo vínculo com séries.
- **ArquivoAtores.java**: Gerencia o CRUD de atores, impedindo remoção de atores vinculados.
- **ArvoreBMais.java**: Implementação de árvore B+ para gerenciar os relacionamentos N:N.
- **HashExtensivel.java**: Índice direto para acesso rápido aos registros.
- **ListaInvertida.java**: Implementação do índice invertido para séries, episódios e atores.

### Principal.java
- Interface principal do programa:
  - Menu inicial com Séries, Episódios e Atores.
  - Opções de visualização e vinculação entre entidades.

## 💡 Desafios de Desenvolvimento

A principal complexidade do TP3 foi a implementação e sincronização do índice invertido para garantir que as buscas por palavras nos títulos das séries, episódios e atores fossem eficientes. Além disso, foi necessário implementar a lógica do TFxIDF para ordenar as buscas com relevância, e garantir que as atualizações no índice invertido ocorressem sempre que uma entidade fosse modificada.

## ✅ Checklist de Funcionalidades
- [x] O índice invertido com os termos dos títulos das séries foi criado usando a classe ListaInvertida? **SIM**
- [x] O índice invertido com os termos dos títulos dos episódios foi criado usando a classe ListaInvertida? **SIM**
- [x] O índice invertido com os termos dos nomes dos atores foi criado usando a classe ListaInvertida? **SIM**
- [x] É possível buscar séries por palavras usando o índice invertido? **SIM**
- [x] É possível buscar episódios por palavras usando o índice invertido? **SIM**
- [x] É possível buscar atores por palavras usando o índice invertido? **SIM**
- [x] O trabalho está completo? **SIM**
- [x] O trabalho é original e não a cópia de um trabalho de um colega? **SIM, o trabalho é original.**

## 📝 Desafios Encontrados
- **Gabriel Henrique**: Implementar o índice invertido e garantir a ordem correta das buscas com TFxIDF.
- **Vitor Leite**: Ajustar a estrutura de dados para garantir consistência entre as entidades e o índice invertido.
- **Mateus Martins Parreiras**: Adicionar a funcionalidade de busca no menu, integrando a consulta com o índice invertido.

## 📂 Repositório
[PUCFlix TP2 no GitHub](https://github.com/GabrielDev0001/ti2_tp3)  
Projeto realizado para o Trabalho Prático 3 da disciplina de AEDS 3 — PUC Minas.
