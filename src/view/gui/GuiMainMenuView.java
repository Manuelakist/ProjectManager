package view.gui;

import model.Project;
import model.ProjectManager;
import view.IMainMenuView;
import view.ProjectTableModel;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

/**
 * Implementação Gráfica (Swing) da tela principal.
 * Esta classe é um JFrame (uma janela).
 */
public class GuiMainMenuView extends JFrame implements IMainMenuView {

    private JPanel mainPanel;
    private JPanel buttonsPanel;
    private JButton buttonCreate;
    private JButton buttonSave;
    private JButton buttonEdit;
    private JButton buttonRemove;
    private JLabel labelTitle;
    private JPanel titlePanel;
    private JScrollPane tablePane;
    private JTable projectTable;

    private final ProjectManager manager;

    /**
     * Construtor da tela principal gráfica.
     * @param manager A instância do ProjectManager (o Model).
     */
    public GuiMainMenuView(ProjectManager manager) {
        this.manager = manager;
        this.setContentPane(mainPanel);
        this.setTitle("Gerenciador de Projetos");
        this.setSize(800, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        projectTable.setTableHeader(null);

        loadProjectList();
        setupListeners();
    }

    /**
     * inicia e exibe a tela.
     * <p>
     * Para a GUI, o método display() simplesmente
     * torna a janela visível.
     * </p>
     */
    @Override
    public void display() {
        this.setVisible(true);
    }

    private void createUIComponents() {
    }

    /**
     * Carrega (ou recarrega) a lista de projetos do ProjectManager
     * e a exibe no componente visual JTable.
     */
    private void loadProjectList() {

        ArrayList<Project> projects = manager.getProjects();

        ProjectTableModel tableModel = new ProjectTableModel(projects);

        projectTable.setModel(tableModel);

        TableColumnModel columnModel = projectTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(40);
        columnModel.getColumn(0).setMaxWidth(60);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(2).setMaxWidth(150);
        columnModel.getColumn(3).setPreferredWidth(80);
        columnModel.getColumn(3).setMaxWidth(80);

        ProjectTableRenderer renderer = new ProjectTableRenderer();

        projectTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        projectTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
        projectTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
        projectTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
    }

    /**
     * Classe interna privada que atua como o "Renderizador" da JTable.
     * <p>
     * O trabalho desta classe é customizar a aparência de CADA CÉLULA
     * da tabela, aplicando alinhamento e fontes diferentes.
     * </p>
     */
    private class ProjectTableRenderer extends DefaultTableCellRenderer {

        private final Font boldFont = getFont().deriveFont(Font.BOLD, 18);
        private final Font normalFont = getFont().deriveFont(Font.PLAIN, 16);

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {

            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            setText(value.toString());


            switch (column) {
                case 0: // Coluna "ID"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                case 1: // Coluna "Nome do Projeto"
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(boldFont);
                    break;

                case 2: // Coluna "Prazo"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                case 3: // Coluna "Progresso"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                default: // Padrão (nunca deve acontecer)
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(normalFont);
            }

            return this;
        }
    }

    /**
     * Método auxiliar privado para configurar todos os ActionListeners
     * (o que acontece quando os botões são clicados).
     */
    private void setupListeners() {

        buttonCreate.addActionListener(e -> {
            handleCreateProject();
        });

        buttonEdit.addActionListener(e -> {
            handleUpdateProject();
        });

        buttonRemove.addActionListener(e -> {
            handleDeleteProject();
        });

        buttonSave.addActionListener(e -> {
            handleSaveData();
        });
    }

    /**
     * Lida com o clique no botão "Criar Projeto".
     * Pede os dados ao usuário usando pop-ups (JOptionPane).
     */
    private void handleCreateProject() {
        try {
            String name = JOptionPane.showInputDialog(this,
                    "Digite o nome do novo projeto:",
                    "Novo Projeto",
                    JOptionPane.PLAIN_MESSAGE);

            if (model.AppUtils.isStringNullOrEmpty(name)) { //
                return;
            }

            String dateStr = JOptionPane.showInputDialog(this,
                    "Digite o prazo final (AAAA-MM-DD):",
                    "Novo Projeto",
                    JOptionPane.PLAIN_MESSAGE);

            if (model.AppUtils.isStringNullOrEmpty(dateStr)) {
                return;
            }

            LocalDate deadline = LocalDate.parse(dateStr);

            manager.createProject(name, deadline);

            JOptionPane.showMessageDialog(this,
                    "Projeto '" + name + "' criado com sucesso!",
                    "Sucesso",
                    JOptionPane.INFORMATION_MESSAGE);

            loadProjectList();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de data inválido. Use AAAA-MM-DD.",
                    "Erro de Formato",
                    JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this,
                    "Erro ao criar projeto: " + e.getMessage(),
                    "Erro de Validação",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lida com o clique no botão "Excluir Projeto".
     * Pede o ID e a confirmação.
     */
    private void handleDeleteProject() {

        Project selectedProject = getSelectedProjectFromTable();

        if (selectedProject == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um projeto na lista primeiro.",
                    "Nenhum Projeto Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String projectId = selectedProject.getId();
        String projectName = selectedProject.getName();

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o projeto '" + projectName +
                        "'?\nIsso excluirá TODAS as tarefas contidas nele.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = manager.deleteProject(projectId);

            if (success) {
                JOptionPane.showMessageDialog(this, "Projeto excluído com sucesso.",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadProjectList();
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Falha ao excluir o projeto.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Lida com o clique no botão "Editar Projeto".
     * Pede o ID e os novos dados.
     */
    private void handleUpdateProject() {

        Project selectedProject = getSelectedProjectFromTable();

        if (selectedProject == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione um projeto na lista primeiro.",
                    "Nenhum Projeto Selecionado", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String newName = (String) JOptionPane.showInputDialog(this,
                    "Digite o NOVO nome:",
                    "Editar Projeto",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    selectedProject.getName());

            if (model.AppUtils.isStringNullOrEmpty(newName)) {
                return;
            }

            String newDateStr = (String) JOptionPane.showInputDialog(this,
                    "Digite o NOVO prazo (AAAA-MM-DD):",
                    "Editar Projeto",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    selectedProject.getGeneralDeadline().toString());

            if (model.AppUtils.isStringNullOrEmpty(newDateStr)) {
                return;
            }

            LocalDate newDeadline = LocalDate.parse(newDateStr);

            manager.updateProject(selectedProject.getId(), newName, newDeadline);

            JOptionPane.showMessageDialog(this, "Projeto atualizado com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            loadProjectList();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.",
                    "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage(),
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lida com o clique no botão "Salvar Dados".
     * Apenas chama o manager e mostra uma confirmação.
     */
    private void handleSaveData() {
        try {
            manager.saveData(); //
            JOptionPane.showMessageDialog(this,
                    "Dados salvos com sucesso!",
                    "Salvar",
                    JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Falha ao salvar dados: " + e.getMessage(),
                    "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método auxiliar para pegar o objeto Project da linha selecionada na JTable.
     * @return O objeto Project selecionado, ou null se ninguém for selecionado.
     */
    private Project getSelectedProjectFromTable() {
        int selectedRow = projectTable.getSelectedRow();

        if (selectedRow == -1) {
            return null;
        }

        ProjectTableModel model = (ProjectTableModel) projectTable.getModel();

        return model.getProjectAt(selectedRow);
    }


}