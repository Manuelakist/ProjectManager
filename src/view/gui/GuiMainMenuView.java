package view.gui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import model.AppUtils;
import model.Project;
import model.ProjectManager;
import view.IMainMenuView;
import view.IProjectView;
import view.IViewFactory;
import view.ViewFactoryProvider;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.FontUIResource;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Locale;

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
     *
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

    /**
     * Carrega (ou recarrega) a lista de projetos do ProjectManager
     * e a exibe no componente visual JTable.
     */
    private void loadProjectList() {

        ArrayList<Project> projects = manager.getProjects();

        ProjectTableModel tableModel = new ProjectTableModel(projects);

        projectTable.setModel(tableModel);

        TableColumnModel columnModel = projectTable.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
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

        private final Font nameFont = getFont().deriveFont(Font.PLAIN, 20);
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
                    setFont(nameFont);
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

        /**
         * Adiciona um "ouvinte" de mouse na tabela para:
         * 1. Abrir o projeto com clique duplo.
         * 2. Mudar o cursor para a "mãozinha" (HAND_CURSOR)
         * quando o mouse estiver sobre a tabela.
         */
        projectTable.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    handleOpenProjectDetails();
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
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

            if (AppUtils.isStringNullOrEmpty(name)) {
                return;
            }

            String dateStr = JOptionPane.showInputDialog(this,
                    "Digite o prazo final (AAAA-MM-DD):",
                    "Novo Projeto",
                    JOptionPane.PLAIN_MESSAGE);

            if (AppUtils.isStringNullOrEmpty(dateStr)) {
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
     * Lida com o clique no botão "Editar Projeto".
     * Pede os novos dados.
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

            if (AppUtils.isStringNullOrEmpty(newName)) {
                return;
            }

            String newDateStr = (String) JOptionPane.showInputDialog(this,
                    "Digite o NOVO prazo (AAAA-MM-DD):",
                    "Editar Projeto",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    null,
                    selectedProject.getGeneralDeadline().toString());

            if (AppUtils.isStringNullOrEmpty(newDateStr)) {
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
     * Lida com o clique no botão "Excluir Projeto".
     * Pede a confirmação.
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
     * Lida com o clique duplo na tabela.
     * Abre a tela de detalhes (GuiProjectView) para o projeto selecionado.
     */
    private void handleOpenProjectDetails() {

        Project selectedProject = getSelectedProjectFromTable();

        if (selectedProject == null) {
            return;
        }

        IViewFactory factory = ViewFactoryProvider.getFactory();
        IProjectView projectView = factory.createProjectView(this.manager, selectedProject);
        projectView.displayProjectDetails();
        loadProjectList();
    }

    /**
     * Método auxiliar para pegar o objeto Project da linha selecionada na JTable.
     *
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.setAutoscrolls(true);
        mainPanel.setEnabled(true);
        mainPanel.setPreferredSize(new Dimension(-1, -1));
        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayoutManager(1, 4, new Insets(20, 20, 20, 20), -1, -1));
        mainPanel.add(buttonsPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_SOUTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 80), new Dimension(-1, 80), 0, false));
        buttonSave = new JButton();
        buttonSave.setText("Salvar");
        buttonsPanel.add(buttonSave, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 80), null, 0, false));
        buttonEdit = new JButton();
        buttonEdit.setText("Editar");
        buttonsPanel.add(buttonEdit, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 80), null, 0, false));
        buttonRemove = new JButton();
        buttonRemove.setText("Remover");
        buttonsPanel.add(buttonRemove, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 80), null, 0, false));
        buttonCreate = new JButton();
        buttonCreate.setText("Criar");
        buttonsPanel.add(buttonCreate, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 80), null, 0, false));
        titlePanel = new JPanel();
        titlePanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        mainPanel.add(titlePanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, new Dimension(-1, 60), new Dimension(-1, 80), 0, false));
        labelTitle = new JLabel();
        labelTitle.setAlignmentY(0.5f);
        Font labelTitleFont = this.$$$getFont$$$("JetBrains Mono", Font.BOLD, 26, labelTitle.getFont());
        if (labelTitleFont != null) labelTitle.setFont(labelTitleFont);
        labelTitle.setHorizontalAlignment(0);
        labelTitle.setHorizontalTextPosition(0);
        labelTitle.setText("Meus Projetos");
        titlePanel.add(labelTitle, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        tablePane = new JScrollPane();
        tablePane.setAutoscrolls(true);
        tablePane.setFocusable(false);
        tablePane.setInheritsPopupMenu(false);
        tablePane.setPreferredSize(new Dimension(-1, -1));
        tablePane.setVerticalScrollBarPolicy(20);
        tablePane.setWheelScrollingEnabled(true);
        mainPanel.add(tablePane, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        tablePane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(40, 40, 40, 30), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        projectTable = new JTable();
        projectTable.setAutoResizeMode(2);
        projectTable.setAutoscrolls(true);
        projectTable.setDragEnabled(false);
        projectTable.setEnabled(true);
        projectTable.setFillsViewportHeight(false);
        projectTable.setFocusable(false);
        Font projectTableFont = this.$$$getFont$$$(null, -1, -1, projectTable.getFont());
        if (projectTableFont != null) projectTable.setFont(projectTableFont);
        projectTable.setIntercellSpacing(new Dimension(0, 0));
        projectTable.setMaximumSize(new Dimension(-1, -1));
        projectTable.setMinimumSize(new Dimension(-1, -1));
        projectTable.setRequestFocusEnabled(false);
        projectTable.setRowHeight(40);
        projectTable.setShowHorizontalLines(false);
        projectTable.setShowVerticalLines(false);
        tablePane.setViewportView(projectTable);
    }

    /**
     * @noinspection ALL
     */
    private Font $$$getFont$$$(String fontName, int style, int size, Font currentFont) {
        if (currentFont == null) return null;
        String resultName;
        if (fontName == null) {
            resultName = currentFont.getName();
        } else {
            Font testFont = new Font(fontName, Font.PLAIN, 10);
            if (testFont.canDisplay('a') && testFont.canDisplay('1')) {
                resultName = fontName;
            } else {
                resultName = currentFont.getName();
            }
        }
        Font font = new Font(resultName, style >= 0 ? style : currentFont.getStyle(), size >= 0 ? size : currentFont.getSize());
        boolean isMac = System.getProperty("os.name", "").toLowerCase(Locale.ENGLISH).startsWith("mac");
        Font fontWithFallback = isMac ? new Font(font.getFamily(), font.getStyle(), font.getSize()) : new StyleContext().getFont(font.getFamily(), font.getStyle(), font.getSize());
        return fontWithFallback instanceof FontUIResource ? fontWithFallback : new FontUIResource(fontWithFallback);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }


}