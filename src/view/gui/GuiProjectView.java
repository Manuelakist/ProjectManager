package view.gui;

import model.*;
import view.IProjectView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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

    private ProjectManager manager;
    private Project project;

    private JPanel titlePanel;
    private JScrollPane taskScrollPane;
    private JTable taskTable;
    private JPanel buttonsPanel;
    private JButton addTaskButton;
    private JButton backButton;
    private JButton editTaskButton;
    private JButton deleteTaskButton;
    private JLabel titleLabel;
    private JLabel deadlineLabel;
    private JProgressBar progressBar;

    /**
     * Construtor da tela de detalhes do projeto.
     *
     * @param manager A instância do ProjectManager.
     * @param project O Projeto específico que esta tela irá exibir.
     */
    public GuiProjectView(ProjectManager manager, Project project) {
        this.manager = manager;
        this.project = project;

        this.setContentPane(projectPanel);
        this.setModal(true);
        this.setTitle("Gerenciando Projeto: " + project.getName());
        this.setSize(800, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        taskTable.setTableHeader(null);

        loadProjectData();
        loadTaskList();
        setupListeners();
    }

    /**
     * Para a GUI, o método display() simplesmente
     * torna a janela (JDialog) visível.
     */
    @Override
    public void displayProjectDetails() {
        this.setVisible(true);
    }

    /**
     * Método auxiliar privado para carregar os dados do
     * Projeto (nome, prazo, progresso) nos
     * componentes do cabeçalho (JLabels, JProgressBar).
     */
    private void loadProjectData() {
        titleLabel.setText(this.project.getName());
        deadlineLabel.setText(AppUtils.formatarData(this.project.getGeneralDeadline()));
        progressBar.setValue((int) this.project.getProgressPercentage());
    }

    /**
     * Método auxiliar privado que carrega (ou recarrega) a lista
     * de tarefas usando o TaskTableModel.
     */
    private void loadTaskList() {

        ArrayList<Task> tasks = this.project.getTasks();

        TaskTableModel tableModel = new TaskTableModel(tasks);
        taskTable.setModel(tableModel);

        TableColumnModel columnModel = taskTable.getColumnModel();
        columnModel.getColumn(0).setMinWidth(0);
        columnModel.getColumn(0).setPreferredWidth(0);
        columnModel.getColumn(0).setMaxWidth(0);
        columnModel.getColumn(2).setPreferredWidth(200);
        columnModel.getColumn(2).setMaxWidth(200);
        columnModel.getColumn(3).setPreferredWidth(40);
        columnModel.getColumn(3).setMaxWidth(60);
        columnModel.getColumn(4).setPreferredWidth(100);
        columnModel.getColumn(4).setMaxWidth(100);
        columnModel.getColumn(5).setPreferredWidth(150);
        columnModel.getColumn(5).setMaxWidth(150);

        TaskTableRenderer renderer = new TaskTableRenderer();

        taskTable.getColumnModel().getColumn(0).setCellRenderer(renderer);
        taskTable.getColumnModel().getColumn(1).setCellRenderer(renderer);
        taskTable.getColumnModel().getColumn(2).setCellRenderer(renderer);
        taskTable.getColumnModel().getColumn(3).setCellRenderer(renderer);
        taskTable.getColumnModel().getColumn(4).setCellRenderer(renderer);
        taskTable.getColumnModel().getColumn(5).setCellRenderer(renderer);

        progressBar.setValue((int) this.project.getProgressPercentage());

    }

    /**
     * Classe interna privada que atua como o "Renderizador" da JTable.
     * <p>
     * O trabalho desta classe é customizar a aparência de CADA CÉLULA
     * da tabela, aplicando alinhamento e fontes diferentes.
     * </p>
     */
    private class TaskTableRenderer extends DefaultTableCellRenderer {

        private final Font descriptionFont = getFont().deriveFont(Font.PLAIN, 18);
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

                case 1: // Coluna "Descrição"
                    setHorizontalAlignment(JLabel.LEFT);
                    setFont(descriptionFont);
                    break;

                case 2: // Coluna "Tipo"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                case 3: // Coluna "Prioridade"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                case 4: // Coluna "Data"
                    setHorizontalAlignment(JLabel.CENTER);
                    setFont(normalFont);
                    break;

                case 5: // Coluna "Status"
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

        addTaskButton.addActionListener(e -> {
            handleAddTask();
        });

        editTaskButton.addActionListener(e -> {
            handleEditTask();
        });

        deleteTaskButton.addActionListener(e -> {
            handleDeleteTask();
        });

        backButton.addActionListener(e -> {
            this.manager.saveData();
            this.dispose();
        });
    }

    /**
     * Lida com o clique de "Adicionar Tarefa".
     * Pede os dados ao usuário usando pop-ups (JOptionPane).
     */
    private void handleAddTask() {
        try {
            TaskType[] taskTypes = TaskType.values();

            TaskType chosenType = (TaskType) JOptionPane.showInputDialog(
                    this,
                    "Qual tipo de tarefa deseja criar?",
                    "Adicionar Nova Tarefa",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    taskTypes,
                    taskTypes[0]
            );

            if (chosenType == null) {
                return;
            }

            Map<String, Object> data = new HashMap<>();

            String description = JOptionPane.showInputDialog(
                    this, "Digite a descrição da tarefa:",
                    "Adicionar Tarefa", JOptionPane.PLAIN_MESSAGE);

            if (model.AppUtils.isStringNullOrEmpty(description)) {
                throw new IllegalArgumentException("A descrição não pode ser vazia.");
            }
            data.put("description", description);

            Object[] priorityOptions = {1, 2, 3, 4, 5};

            Object priority = JOptionPane.showInputDialog(
                    this, "Selecione a prioridade da tarefa:",
                    "Adicionar Tarefa", JOptionPane.QUESTION_MESSAGE, null,
                    priorityOptions, priorityOptions[0]);

            if (priority == null) {
                return;
            }

            int priorityInt = (int) priority;
            data.put("priority", priorityInt);

            switch (chosenType) {
                case DEADLINE:
                    LocalDate deadline = LocalDate.parse(JOptionPane.showInputDialog(this,
                            "Digite o prazo (AAAA-MM-DD):", "Adicionar Tarefa",
                            JOptionPane.PLAIN_MESSAGE));
                    data.put("deadline", deadline);
                    break;
                case MILESTONE:
                    LocalDate date = LocalDate.parse(JOptionPane.showInputDialog(this,
                            "Digite a data do Marco (AAAA-MM-DD):", "Adicionar Tarefa",
                            JOptionPane.PLAIN_MESSAGE));
                    data.put("milestoneDate", date);
                    break;
                case SIMPLE:
                    // Não precisa de dados extras
                    break;
            }

            this.manager.createTaskForProject(this.project.getId(), chosenType, data);
            JOptionPane.showMessageDialog(this, "Tarefa criada com sucesso!");
            loadTaskList();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.",
                    "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro: " + e.getMessage(),
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Um erro inesperado ocorreu: " + e.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    /**
     * Lida com o clique de "Editar Tarefa".
     * Pede os novos dados
     */
    private void handleEditTask() {
        Task selectedTask = getSelectedTaskFromTable();

        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione uma tarefa na lista primeiro.",
                    "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String newDescription = (String) JOptionPane.showInputDialog(this,
                    "Digite a NOVA descrição:", "Editar Tarefa", JOptionPane.PLAIN_MESSAGE,
                    null, null, selectedTask.getDescription());

            if (model.AppUtils.isStringNullOrEmpty(newDescription)) {
                return;
            }
            selectedTask.setDescription(newDescription);

            Object[] priorityOptions = {1, 2, 3, 4, 5};

            Object priority = JOptionPane.showInputDialog(
                    this, "Selecione a NOVA prioridade:",
                    "Editar Tarefa", JOptionPane.QUESTION_MESSAGE, null,
                    priorityOptions, selectedTask.getPriority());

            if (priority == null) {
                return;
            }
            selectedTask.setPriority((int) priority);

            Status newStatus = (Status) JOptionPane.showInputDialog(this, "Selecione o NOVO status",
                    "Editar Tarefa", JOptionPane.QUESTION_MESSAGE, null, selectedTask.getValidStatuses(),
                    selectedTask.getStatus());

            if (newStatus == null) {
                return;
            }
            selectedTask.setStatus(newStatus);

            if (selectedTask instanceof DeadlineTask) {
                String newDateStr = (String) JOptionPane.showInputDialog(this,
                        "Digite o NOVO prazo (AAAA-MM-DD):",  "Editar Tarefa",
                        JOptionPane.PLAIN_MESSAGE, null, null,
                        ((DeadlineTask) selectedTask).getTaskDeadline().toString());

                if (model.AppUtils.isStringNullOrEmpty(newDateStr)) {
                    return;
                }
                LocalDate newDeadline = LocalDate.parse(newDateStr);
                ((DeadlineTask) selectedTask).setTaskDeadline(newDeadline);
            } else if (selectedTask instanceof Milestone) {
                String newDateStr = (String) JOptionPane.showInputDialog(this,
                        "Digite a NOVA data do Marco (AAAA-MM-DD):",  "Editar Tarefa",
                        JOptionPane.PLAIN_MESSAGE, null, null,
                        ((Milestone) selectedTask).getMilestoneDate().toString());

                if (model.AppUtils.isStringNullOrEmpty(newDateStr)) {
                    return;
                }
                LocalDate newMilestoneDate = LocalDate.parse(newDateStr);
                ((Milestone) selectedTask).setMilestoneDate(newMilestoneDate);
            }

            JOptionPane.showMessageDialog(this, "Tarefa atualizada com sucesso!",
                    "Sucesso", JOptionPane.INFORMATION_MESSAGE);

            loadTaskList();

        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de data inválido. Use AAAA-MM-DD.",
                    "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, "Erro ao atualizar: " + e.getMessage(),
                    "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Lida com o clique no botão "Excluir Tarefa".
     * Pede a confirmação.
     */
    private void handleDeleteTask() {

        Task selectedTask = getSelectedTaskFromTable();

        if (selectedTask == null) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, selecione uma tarefa na lista primeiro.",
                    "Nenhuma Tarefa Selecionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir a tarefa '" + selectedTask.getDescription() +
                        "'?", "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = project.removeTask(selectedTask.getId());

            if (success) {
                JOptionPane.showMessageDialog(this, "Tarefa excluída com sucesso.",
                        "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                loadTaskList();
            } else {
                JOptionPane.showMessageDialog(this, "Erro: Falha ao excluir a tarefa.",
                        "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Método auxiliar para pegar o objeto Task da linha selecionada na JTable.
     * @return O objeto Task selecionado, ou null se ninguém for selecionado.
     */
    private Task getSelectedTaskFromTable() {
        int selectedRow = taskTable.getSelectedRow();

        if (selectedRow == -1) {
            return null;
        }

        TaskTableModel model = (TaskTableModel) taskTable.getModel();

        return model.getTaskAt(selectedRow);
    }
}