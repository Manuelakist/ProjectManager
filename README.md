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
    O projeto é dividido em dois pacotes principais: `model` (com toda a lógica de negócio, sem NENHUMA interação com o usuário) e `view` (com as telas). 

2.  **Padrão Abstract Factory (Fábrica Abstrata):**
    O sistema pode rodar de dois jeitos: com uma **interface gráfica (Swing)** ou com uma **interface textual (console)**. Para fazer essa troca de forma limpa, foi usado o padrão Abstract Factory (`IViewFactory`). O `Main.java` só precisa mudar uma linha na `ViewFactoryProvider` para trocar a aplicação inteira.

3.  **Padrão DAO e Strategy (Estratégia):**
    A lógica de salvar e carregar os dados foi separada com uma interface (`IPersistenceDAO`). Isso desacopla o `ProjectManager` (a lógica de negócio) do "como" os dados são salvos (Padrão Strategy). A implementação atual (`JsonProjectDAO`) usa JSON, mas a interface permite que o `ProjectManager` não precise saber desse detalhe.

4.  **Herança e Polimorfismo:**
    Este é o principal conceito de POO aplicado. Existe uma classe abstrata `Task` que serve de base para **3 tipos** diferentes de tarefas:
    * `SimpleTask` (uma tarefa básica)
    * `DeadlineTask` (tem data de entrega)
    * `Milestone` (um marco importante do projeto)

    O `Project` simplesmente armazena um `ArrayList<Task>`, e graças ao polimorfismo, ele consegue lidar com todos os tipos de tarefa (por exemplo, no método `getProgressPercentage()`).

5.  **Padrão Factory (Fábrica):**
    Para não "sujar" o `ProjectManager` com a lógica de criação de *quais* tarefas existem (`new SimpleTask`, `new DeadlineTask`...), foi usada uma `TaskFactory`. A `view` (interface) só precisa dizer o `TaskType` (um enum), e a fábrica cuida da construção.

6.  **GUI com TableModel (Padrão Adapter):**
    A interface gráfica (Swing) usa `JTable`s para exibir os dados. A lógica de "adaptar" os objetos `Project` e `Task` para a tabela é feita pelas classes `ProjectTableModel` e `TaskTableModel`, mantendo a `view.gui` limpa.

## Bibliotecas Externas

* **Gson (Google):** Usada pelo `JsonProjectDAO` para serializar (salvar) e deserializar (carregar) os projetos e tarefas no formato JSON.
* **FlatLaf:** Usada para o "Look and Feel" (aparência) da interface Swing (Dark Purple).

## Diagrama UML

Abaixo está o diagrama de classes que mostra a estrutura do projeto.

---

![Diagrama de Classes](docs/diagramaUML.png)

---

## Como Executar

1.  Clone o repositório.
2.  Abra o projeto na sua IDE (ex: IntelliJ). As bibliotecas (`.jar`) já estão na pasta `lib` e configuradas no `.iml`.
3.  Para escolher a interface, vá em `Main.java` e mude a string de configuração:
    * `ViewFactoryProvider.configure("gui");` (para a interface gráfica Swing)
    * `ViewFactoryProvider.configure("textual");` (para a interface de console)
4.  Execute o `Main.java`.