package view.textual;

import model.AppUtils;
import model.Project;
import model.ProjectManager;
import view.IMainMenuView;
import view.IProjectView;
import view.IViewFactory;
import view.ViewFactoryProvider;

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
        System.out.println("5. Editar um projeto");
        System.out.println("6. Excluir um projeto");
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
            case "5":
                this.handleUpdateProject();
                break;
            case "6":
                this.handleDeleteProject();
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
        Project project = this.askAndFindProjectById();

        if (project == null) {
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

    /**
     * Lida com a opção "5. Editar um projeto".
     * Pede o ID e os novos dados do projeto.
     */
    private void handleUpdateProject() {
        Project project = this.askAndFindProjectById();

        if (project == null) {
            return;
        }

        try {
            System.out.println("Editando projeto: " + project.getName());
            System.out.print("Digite o NOVO nome: ");
            String newName = scanner.nextLine();

            System.out.print("Digite o NOVO prazo final (AAAA-MM-DD): ");
            String dateStr = scanner.nextLine();
            LocalDate newDeadline = LocalDate.parse(dateStr);

            boolean success = this.manager.updateProject(project.getId(), newName, newDeadline);

            if (success) {
                System.out.println("Projeto atualizado com sucesso!");
            }

        } catch (DateTimeParseException e) {
            System.out.println("ERRO: Formato de data inválido. Use AAAA-MM-DD.");
        } catch (IllegalArgumentException e) {
            System.out.println("ERRO AO ATUALIZAR: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("ERRO inesperado: " + e.getMessage());
        }
    }

    /**
     * Lida com a opção "6. Excluir um projeto".
     * Pede o ID e a confirmação do usuário.
     */
    private void handleDeleteProject() {
        Project project = this.askAndFindProjectById(); // <-- Chama o helper

        if (project == null) {
            return;
        }

        System.out.println("Tem certeza que deseja excluir o projeto '" + project.getName() + "'?");
        System.out.println("Isso excluirá TODAS as tarefas contidas nele.");
        System.out.print("Digite 'SIM' para confirmar: ");
        String confirm = scanner.nextLine().trim().toUpperCase();

        if ("SIM".equals(confirm)) {
            boolean success = this.manager.deleteProject(project.getId());
            if (success) {
                System.out.println("Projeto excluído com sucesso.");
            } else {
                System.out.println("ERRO: Falha ao excluir o projeto.");
            }
        } else {
            System.out.println("Exclusão cancelada.");
        }
    }

    /**
     * Método auxiliar para pedir um ID e buscar um Projeto.
     * Encapsula a lógica de "pedir, buscar, checar se é nulo e imprimir erro"
     * @return O objeto Project se for encontrado, ou null se não for.
     */
    private Project askAndFindProjectById() {
        System.out.print("\nDigite o ID do projeto: ");
        String projectId = scanner.nextLine();

        if (AppUtils.isStringNullOrEmpty(projectId)) {
            System.out.println("ERRO: ID não pode ser vazio.");
            return null;
        }

        Project project = this.manager.getProjectById(projectId);

        if (project == null) {
            System.out.println("ERRO: Projeto com ID '" + projectId + "' não encontrado.");
            return null;
        }

        return project;
    }
}