package view;

import model.AppUtils;
import model.Project;
import model.ProjectManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Implementação Concreta (Produto Concreto) da tela principal
 * no modo Textual (console).
 * <p>
 * Implementa {@link IMainMenuView} e é responsável pelo loop
 * principal da aplicação.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class TextualMainMenuView implements IMainMenuView {

    private final ProjectManager manager;
    private final Scanner scanner;
    private boolean running;

    /**
     * Construtor da tela principal de texto.
     * @param manager A instância do ProjectManager (o Model) vinda do Main.
     */
    public TextualMainMenuView(ProjectManager manager) {
        this.manager = manager;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    /**
     * Inicia o loop principal do menu textual. O programa
     * ficará preso neste loop até que o usuário escolha "Sair".
     */
    @Override
    public void display() {
        System.out.println("Bem-vindo ao Gerenciador de Projetos!");

        while (this.running) {
            showMenuOptions();
            String choice = scanner.nextLine();
            handleMenuChoice(choice);
        }

        System.out.println("\nSalvando dados antes de sair...");
        this.handleSaveData();
        System.out.println("Programa finalizado. Adeus!");
        this.scanner.close();
    }

    /**
     * Método auxiliar privado que apenas exibe as opções do menu.
     */
    private void showMenuOptions() {
        System.out.println("\n--- MENU PRINCIPAL ---");
        System.out.println("1. Listar todos os projetos");
        System.out.println("2. Criar novo projeto");
        System.out.println("3. Selecionar um projeto (para ver/add tarefas)");
        System.out.println("4. Salvar dados agora");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }

    /**
     * Método auxiliar que direciona a escolha do usuário.
     * @param choice A opção que o usuário digitou.
     */
    private void handleMenuChoice(String choice) {
        switch (choice) {
            case "1":
                this.handleListProjects();
                break;
            case "2":
                this.handleCreateProject();
                break;
            case "3":
                this.handleSelectProject();
                break;
            case "4":
                this.handleSaveData();
                break;
            case "0":
                this.running = false;
                break;
            default:
                System.out.println("ERRO: Opção inválida. Tente novamente.");
        }
    }

    /**
     * Lida com a opção "1. Listar todos os projetos".
     * Busca os projetos no manager e os exibe no console.
     */
    private void handleListProjects() {
        ArrayList<Project> projects = this.manager.getProjects();

        if (projects.isEmpty()) {
            System.out.println("Nenhum projeto cadastrado.");
            return;
        }

        System.out.println("\n--- Seus Projetos ---");
        for (Project p : projects) {
            System.out.printf(
                    "ID: %s (%.0f%%) | %s (Prazo: %s)\n",
                    p.getId(),
                    p.getProgressPercentage(),
                    p.getName(),
                    AppUtils.formatarData(p.getGeneralDeadline())
            );
        }
    }

    /**
     * Lida com a opção "2. Criar novo projeto".
     * Pede os dados ao usuário e chama o manager para criar o projeto.
     */
    private void handleCreateProject() {
        try {
            System.out.println("\n--- Novo Projeto ---");
            System.out.print("Digite o nome do projeto: ");
            String name = scanner.nextLine();

            System.out.print("Digite o prazo final (ex: AAAA-MM-DD): ");
            String dateStr = scanner.nextLine();

            LocalDate deadline = LocalDate.parse(dateStr);

            this.manager.createProject(name, deadline);

            System.out.println("Projeto '" + name + "' criado com sucesso!");

        } catch (DateTimeParseException e) {
            System.out.println("ERRO: Formato de data inválido. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO AO CRIAR PROJETO: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO inesperado: " + e.getMessage());
        }
    }

    /**
     * Lida com a opção "3. Selecionar um projeto".
     * Pede um ID, busca o projeto e, se encontrar,
     * usa a FÁBRICA ABSTRATA para criar e exibir a tela de detalhes.
     */
    private void handleSelectProject() {
        System.out.print("\nDigite o ID do projeto que deseja gerenciar: ");
        String projectId = scanner.nextLine();

        Project project = this.manager.getProjectById(projectId);

        if (project == null) {
            System.out.println("ERRO: Projeto com ID '" + projectId + "' não encontrado.");
            return;
        }

        IViewFactory factory = ViewFactoryProvider.getFactory();

        IProjectView projectView = factory.createProjectView(this.manager, project);

        System.out.println("\nEntrando no projeto: " + project.getName() + "...");
        projectView.displayProjectDetails();
        System.out.println("\nSaindo do projeto '" + project.getName() + "'. Voltando ao Menu Principal...");
    }

    /**
     * Lida com a opção "4. Salvar dados".
     * Chama o método de salvar do manager.
     */
    private void handleSaveData() {
        System.out.println("Salvando dados...");
        this.manager.saveData();
    }
}