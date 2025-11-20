package view.gui;

import model.*; // Importa Task, Status, DeadlineTask, etc.
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Modelo de Tabela para a JTable que exibe as Tarefas.
 * É o "motor" que a JTable usa para entender a lista de Tasks.
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class TaskTableModel extends AbstractTableModel {

    private final ArrayList<Task> tasks;

    private final String[] columnNames = {"ID", "Descrição", "Tipo", "Prioridade", "Data", "Status"};

    public TaskTableModel(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return tasks.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Método que decide o que mostrar em cada célula, usando
     * 'instanceof' para checar o tipo da tarefa.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Task task = tasks.get(rowIndex);

        switch (columnIndex) {
            case 0: // Coluna "ID"
                return task.getId();

            case 1: // Coluna "Descrição"
                return task.getDescription();

            case 2: // Coluna "Tipo"
                if (task instanceof Milestone) return "Marco (Milestone)";
                if (task instanceof DeadlineTask) return "Tarefa Com Prazo";
                return "Tarefa Simples"; // (SimpleTask)

            case 3: // Coluna "Prioridade"
                return task.getPriority();

            case 4: // Coluna "Data"
                if (task instanceof DeadlineTask) {
                    return AppUtils.formatarData(((DeadlineTask) task).getTaskDeadline());
                }
                if (task instanceof Milestone) {
                    return AppUtils.formatarData(((Milestone) task).getMilestoneDate());
                }
                return "-";

            case 5: // Coluna "Status"
                return task.getStatus();

            default:
                return "??";
        }
    }

    /**
     * Método auxiliar para a View poder pegar o objeto Task
     * de uma linha específica (para editar ou excluir).
     */
    public Task getTaskAt(int row) {
        return tasks.get(row);
    }
}