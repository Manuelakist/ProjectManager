package view.gui;

import model.AppUtils;
import model.Project;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;

/**
 * Modelo de Tabela para a JTable que exibe os Projetos.
 * <p>
 * Motor que a JTable usa para saber:
 * 1. Quantas colunas ela tem.
 * 2. Quais os nomes das colunas.
 * 3. Como "renderizar" cada célula.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class ProjectTableModel extends AbstractTableModel {

    private final ArrayList<Project> projects;
    private final String[] columnNames = {"ID", "Nome do Projeto", "Prazo", "Progresso"};

    /**
     * Construtor que recebe a lista de projetos do ProjectManager.
     * @param projects A lista de projetos a ser exibida.
     */
    public ProjectTableModel(ArrayList<Project> projects) {
        this.projects = projects;
    }

    // --- Métodos Obrigatórios do AbstractTableModel ---

    /**
     * {@inheritDoc}
     * Retorna o NOME da coluna.
     */
    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    /**
     * {@inheritDoc}
     * Retorna o NÚMERO de linhas (o tamanho da sua lista).
     */
    @Override
    public int getRowCount() {
        return projects.size();
    }

    /**
     * {@inheritDoc}
     * Retorna o NÚMERO de colunas.
     */
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    /**
     * Método chamado pela JTable para buscar o VALOR de cada célula.
     * @param rowIndex A linha (ex: 0, 1, 2...)
     * @param columnIndex A coluna (ex: 0, 1, 2, 3)
     * @return O valor (Object) a ser exibido.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        Project project = projects.get(rowIndex);

        return switch (columnIndex) {
            case 0 -> // Coluna "ID"
                    project.getId();
            case 1 -> // Coluna "Nome do Projeto"
                    project.getName();
            case 2 -> // Coluna "Prazo"
                    AppUtils.formatarData(project.getGeneralDeadline());
            case 3 -> // Coluna "Progresso"
                    String.format("%.0f%%", project.getProgressPercentage());
            default -> "??"; // Nunca deve acontecer
        };
    }

    /**
     * Método auxiliar para a View poder pegar o objeto Project
     * de uma linha específica.
     * @param row O índice da linha.
     * @return O objeto Project daquela linha.
     */
    public Project getProjectAt(int row) {
        return projects.get(row);
    }

}
