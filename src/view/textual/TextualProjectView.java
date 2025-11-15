package view.textual;

import model.*;
import view.IProjectView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import model.AppUtils;

/**
 * Implementação Concreta (Produto Concreto) da tela de detalhes do Projeto,
 * no modo Textual (console).
 * <p>
 * Implementa {@link IProjectView} e é responsável pelo "submenu" de
 * gerenciamento de tarefas de um projeto específico.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class TextualProjectView implements IProjectView {

    private final ProjectManager manager;
    private final Project project;
    private final Scanner scanner;
    private boolean running;

    /**
     * Construtor da tela de visualização do projeto.
     * @param manager O ProjectManager (para salvar).
     * @param project O Projeto específico a ser exibido.
     */
    public TextualProjectView(ProjectManager manager, Project project) {
        this.manager = manager;
        this.project = project;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    /**
     * Inicia o loop do "submenu" de gerenciamento do projeto.
     */
    @Override
    public void displayProjectDetails() {
        while (this.running) {
            showProjectMenu();
            String choice = scanner.nextLine();
            handleMenuChoice(choice);
        }
    }

    /**
     * Exibe o cabeçalho do projeto e as opções do submenu.
     */
    private void showProjectMenu() {
        System.out.println("\n--- Gerenciando Projeto: " + this.project.getName() + " ---");
        System.out.printf("Progresso: %.0f%% | Prazo: %s\n",
                this.project.getProgressPercentage(),
                AppUtils.formatarData(this.project.getGeneralDeadline())
        );
        System.out.println("----------------------------------------");
        System.out.println("1. Listar Tarefas");
        System.out.println("2. Adicionar Tarefa");
        System.out.println("3. Editar Status da Tarefa");
        System.out.println("4. Excluir Tarefa");
        System.out.println("0. Voltar ao Menu Principal (Salva automaticamente)");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Direciona a escolha do usuário no submenu.
     * @param choice A opção digitada.
     */
    private void handleMenuChoice(String choice) {
        switch (choice) {
            case "1":
                this.handleListTasks();
                break;
            case "2":
                this.handleAddTask();
                break;
            case "3":
                this.handleEditTaskStatus();
                break;
            case "4":
                this.handleDeleteTask();
                break;
            case "0":
                this.manager.saveData();
                this.running = false;
                break;
            default:
                System.out.println("ERRO: Opção inválida. Tente novamente.");
        }
    }

    /**
     * Lida com "1. Listar Tarefas".
     * Itera pela lista de tarefas do projeto e usa o polimorfismo
     * do método getDisplayDetails() de cada tarefa.
     */
    private void handleListTasks() {
        ArrayList<Task> tasks = this.project.getTasks();
        if (tasks.isEmpty()) {
            System.out.println("Este projeto ainda não possui tarefas.");
            return;
        }

        System.out.println("\n--- Tarefas do Projeto: " + this.project.getName() + " ---");
        for (Task task : tasks) {
            System.out.println("ID: " + task.getId() + " | " + task.getDisplayDetails());
        }
    }

    /**
     * Lida com "2. Adicionar Tarefa".
     * Pergunta qual o TIPO de tarefa e chama o helper correspondente.
     */
    private void handleAddTask() {
        System.out.println("\n--- Adicionar Nova Tarefa ---");
        System.out.println("Qual tipo de tarefa deseja adicionar?");
        System.out.println("1. Tarefa Simples");
        System.out.println("2. Tarefa com Prazo");
        System.out.println("3. Marco");
        System.out.println("4. Relatório de Bug");
        System.out.print("Escolha o tipo: ");
        String choice = scanner.nextLine();

        Map<String, Object> data = new HashMap<>();
        TaskType type;

        try {

            System.out.print("Digite a descrição: ");
            data.put("description", scanner.nextLine());
            System.out.print("Digite a prioridade (1-5): ");
            data.put("priority", Integer.parseInt(scanner.nextLine()));

            switch (choice) {
                case "1":
                    type = TaskType.SIMPLE;
                    break;
                case "2":
                    type = TaskType.DEADLINE;
                    System.out.print("Digite o nome do responsável (Assignee): ");
                    data.put("assignee", scanner.nextLine());
                    System.out.print("Digite o prazo (AAAA-MM-DD): ");
                    data.put("deadline", LocalDate.parse(scanner.nextLine()));
                    break;
                case "3":
                    type = TaskType.MILESTONE;
                    System.out.print("Digite a data do Marco (AAAA-MM-DD): ");
                    data.put("milestoneDate", LocalDate.parse(scanner.nextLine()));
                    break;
                case "4":
                    type = TaskType.BUG_REPORT;
                    System.out.print("Digite a severidade (ex: Baixa, Média, Crítica): ");
                    data.put("severity", scanner.nextLine());
                    System.out.print("Digite os passos para reproduzir o bug: ");
                    data.put("steps", scanner.nextLine());
                    break;
                default:
                    System.out.println("ERRO: Tipo de tarefa inválido.");
                    return;
            }

            this.manager.createTaskForProject(this.project.getId(), type, data);
            System.out.println("Tarefa criada com sucesso!");

        } catch (IllegalArgumentException | DateTimeParseException e) {
            System.out.println("ERRO AO CRIAR TAREFA: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO inesperado: " + e.getMessage());
        }
    }

    /**
     * Lida com "3. Editar Status da Tarefa".
     * Pede o ID da tarefa e exibe um menu FILTRADO de opções de status.
     */
    private void handleEditTaskStatus() {
        System.out.print("\nDigite o ID da tarefa que deseja atualizar: ");
        String taskId = scanner.nextLine();

        Task task = this.project.getTaskById(taskId);
        if (task == null) {
            System.out.println("ERRO: Tarefa não encontrada.");
            return;
        }

        System.out.println("Status atual: " + task.getStatus());
        System.out.println("Escolha o novo Status:");

        Status[] validStatuses = task.getValidStatuses();

        for (int i = 0; i < validStatuses.length; i++) {
            System.out.printf("%d. %s\n", (i + 1), validStatuses[i]);
        }
        System.out.print("Digite o NÚMERO do novo status: ");

        try {
            int choiceIndex = Integer.parseInt(scanner.nextLine());

            if (choiceIndex < 1 || choiceIndex > validStatuses.length) {
                System.out.println("ERRO: Número de opção inválido.");
                return;
            }

            Status newStatus = validStatuses[choiceIndex - 1];

            // O próprio objeto 'task' vai validar se esse 'newStatus' é permitido para ele.
            task.setStatus(newStatus);

            System.out.println("Status atualizado com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("ERRO: Você deve digitar um NÚMERO.");
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    /**
     * Lida com "4. Excluir Tarefa".
     * Pede o ID da tarefa e a remove do projeto.
     */
    private void handleDeleteTask() {
        System.out.print("\nDigite o ID da tarefa que deseja excluir: ");
        String taskId = scanner.nextLine();

        boolean success = this.project.removeTask(taskId);

        if (success) {
            System.out.println("Tarefa removida com sucesso.");
        } else {
            System.out.println("ERRO: Tarefa não encontrada.");
        }
    }

}