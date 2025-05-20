# 🎬 PUCFlix 2..0

PUCFlix é um projeto desenvolvido para o Trabalho Prático 2 da disciplina de **Algoritmos e Estruturas de Dados 3 (AEDS 3)** na **PUC Minas**. Esta versão expande o sistema desenvolvido no TP1, implementando o relacionamento **N:N entre Séries e Atores**, com o uso de **duas Árvores B+** para garantir consultas eficientes e consistência dos dados.

## 🚀 Funcionalidades

Nesta nova versão, o PUCFlix mantém as operações de **CRUD** para séries e episódios, e agora adiciona:

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
- **ArquivoAtores.java**: Gerencia o CRUD d atores, impedindo remoção de atores vinculados.
- **ArvoreBMais.java**: Implementação de árvore B+ para gerenciar os relacionamentos N:N.
- **HashExtensivel.java**: Índice direto para acesso rápido aos registros.

### Principal.java
- Interface principal do programa:
  - Menu inicial com Séries, Episódios e Atores.
  - Opções de visualização e vinculação entre entidades.

## 💡 Desafios de Desenvolvimento

A principal complexidade do TP2 foi a implementação e sincronização das **duas Árvores B+** para garantir que os dados entre séries e atores estivessem sempre consistentes. Além disso, foi necessário tratar a excluão e alteração de forma cuidadosa para manter a integridade dos dados. A lógica para impedir a exclusão de atores com vínculos também exigiu uma verificação cruzada entre estruturas.

## ✅ Checklist de Funcionalidades
- [x] As operações de inclusão, busca, alteração e exclusão de atores estão implementadas e funcionando corretamente? **SIM**
- [x] O relacionamento entre séries e atores foi implementado com árvores B+ e funciona corretamente, assegurando a consistência entre as duas entidades? **SIM**
- [x] É possível consultar quais são os atores de uma série? **SIM**
- [x] É possível consultar quais são as séries de um ator? **SIM**
- [x] A remoção de séries remove os seus vínculos de atores? **SIM**
- [x] A inclusão de um ator em uma série se limita aos atores existentes? **SIM**
- [x] A remoção de um ator checa se há alguma série vinculada a ele? **SIM**
- [x] O trabalho está funcionando corretamente? **SIM**
- [x] O trabalho está completo? **SIM**
- [x] O trabalho é original e não a cópia de um trabalho de outro grupo? **SIM, O trabalho é original!**

## 📝 Desafios Encontrados
- **Gabriel Henrique**: Adaptar a lógica da Hash para comportar novas entidades e relembrar conceitos de Java.
- **Vitor Leite**: Integrar o CRUD de atores mantendo a consistência do relacionamento com as séries.
- **Mateus Martins Parreiras**: Estender a implementação das árvores B+ para suportar múltiplos relacionamentos e indexações cruzadas.

## 📂 Repositório
[PUCFlix TP2 no GitHub](https://github.com/GabrielDev0001/aeds3TP_2)  
Projeto realizado para o Trabalho Prático 2 da disciplina de AEDS 3 — PUC Minas.
