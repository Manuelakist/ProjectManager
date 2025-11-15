package view.gui;

import model.Project;
import model.ProjectManager;
import view.IProjectView;

import javax.swing.*;

/**
 * Implementação Gráfica (Swing) da tela de detalhes do projeto.
 * Esta classe é um JDialog (uma janela de diálogo) que bloqueia
 * a janela principal enquanto está aberta.
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class GuiProjectView extends JDialog implements IProjectView {


    /** O painel principal (root panel) desenhado no .form */
    private JPanel projectPanel;

    // TODO: Adicione aqui os outros componentes do seu .form
    // Ex: private JList taskList;
    //     private JButton addTaskButton;
    //     private JLabel projectNameLabel;

    // --- Atributos de Dados ---

    /** Referência ao "cérebro" (para salvar, etc.) */
    private ProjectManager manager;

    /** O projeto específico que esta tela está gerenciando */
    private Project project;

    /**
     * Construtor da tela de detalhes do projeto.
     *
     * @param manager A instância do ProjectManager.
     * @param project O Projeto específico que esta tela irá exibir.
     */
    public GuiProjectView(ProjectManager manager, Project project) {
        this.manager = manager;
        this.project = project;

        // --- Configuração Padrão do JDialog ---

        // 1. Define o painel do .form como o conteúdo da janela
        this.setContentPane(projectPanel);

        // 2. Define como "Modal": Isso bloqueia a janela principal
        //    (GuiMainMenuView) enquanto esta estiver aberta. É essencial!
        this.setModal(true);

        // 3. Define o título da janela
        this.setTitle("Gerenciando Projeto: " + project.getName());

        // 4. Define o tamanho (deixa o Swing calcular)
        this.pack();

        // 5. Centraliza a janela na tela
        this.setLocationRelativeTo(null);

        // 6. Define o que acontece ao clicar no "X" (apenas fecha esta janela)
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        // --- Carregar Dados Iniciais ---
        loadProjectData();

        // --- Configurar "Ouvintes" (Action Listeners) ---
        setupListeners();
    }

    /**
     * {@inheritDoc}
     * <p>
     * Para a GUI, o método display() simplesmente
     * torna a janela (JDialog) visível.
     * </p>
     */
    @Override
    public void displayProjectDetails() {
        // Isso "liga" a janela e a faz aparecer na tela
        this.setVisible(true);
    }

    /**
     * Método auxiliar privado para carregar os dados do
     * objeto 'project' e colocá-los nos componentes Swing (ex: JLabels, JList).
     */
    private void loadProjectData() {
        // TODO: Implementar a lógica de carregar dados
        // Ex:
        // projectNameLabel.setText(project.getName());
        // progressBar.setValue((int) project.getProgressPercentage());
        //
        // (A parte mais complexa será atualizar a JList com as tarefas)
    }

    /**
     * Método auxiliar privado para configurar todos os ActionListeners
     * (o que acontece quando os botões são clicados).
     */
    private void setupListeners() {
        // TODO: Implementar os listeners dos botões
        // Ex:
        // addTaskButton.addActionListener(e -> {
        //    handleAddTask();
        // });
        //
        // editTaskButton.addActionListener(e -> {
        //    handleEditTask();
        // });
    }

    // TODO: Criar os métodos auxiliares que são chamados pelos listeners
    // Ex: private void handleAddTask() { ... }
    // Ex: private void handleEditTask() { ... }
}