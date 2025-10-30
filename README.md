# Gerenciador de Projetos Pessoal

Este é um Gerenciador de Projetos Pessoal feito em Java, desenvolvido como trabalho final para a disciplina de Programação Orientada a Objetos.

O objetivo principal era construir um sistema completo do zero, aplicando de forma correta os conceitos de POO, arquitetura de software e padrões de projeto.

## O que ele faz?

O sistema é um gerenciador de tarefas básico, onde você pode:
* Criar, editar e excluir **Projetos**.
* Dentro de um projeto, adicionar, editar e excluir **Tarefas**.
* Acompanhar o progresso de um projeto (calculado em % de tarefas concluídas).
* Salvar e carregar todos os seus projetos e tarefas em um arquivo.

## Arquitetura e Padrões de Projeto

O mais importante do trabalho é a *forma* como ele foi construído.

1.  **Separação Model-View:**
    O projeto é dividido em dois pacotes principais: `model` (com toda a lógica de negócio, sem NENHUMA interação com o usuário) e `view` (com as telas). Isso deixa o código muito mais limpo e fácil de manter.

2.  **Padrão Abstract Factory (Fábrica Abstrata):**
    O sistema pode rodar de dois jeitos: com uma **interface gráfica (Swing)** ou com uma **interface textual (console)**. Para fazer essa troca de forma limpa, foi usado o padrão Abstract Factory. O `Main.java` só precisa mudar uma linha na `ViewFactoryProvider` para trocar a aplicação inteira de "gráfica" para "texto".

3.  **Padrão DAO (Data Access Object):**
    A lógica de salvar e carregar os dados foi separada com uma interface (`IPersistenceDAO`). Isso permite que o sistema salve em diferentes formatos (como Serialização Java ou JSON) sem que o `ProjectManager` (a classe principal do `model`) precise saber *como* os dados estão sendo salvos.

4.  **Herança e Polimorfismo (O "Coração" da POO):**
    Este é o principal conceito de POO aplicado. Existe uma classe abstrata `Task` que serve de base para 4 tipos diferentes de tarefas.
    * `SimpleTask`
    * `DeadlineTask` (tem data de entrega e responsável)
    * `Milestone` (um marco importante do projeto)
    * `BugReport` (para rastrear bugs, com severidade e passos)

    O `Project` simplesmente armazena um `ArrayList<Task>`, e graças ao polimorfismo, ele consegue lidar com todos os tipos de tarefa sem saber os detalhes de cada uma.

## Diagrama UML

Abaixo está o diagrama de classes que mostra a estrutura do projeto.

---

![Diagrama de Classes](docs/diagrama_classes.png)

---

## Como Executar

1.  Clone o repositório.
2.  Abra o projeto na sua IDE (ex: IntelliJ).
3.  Para escolher a interface, vá em `Main.java` e mude a string de configuração:
    * `ViewFactoryProvider.configure("gui");` (para a interface gráfica Swing)
    * `ViewFactoryProvider.configure("textual");` (para a interface de console)
4.  Execute o `Main.java`.