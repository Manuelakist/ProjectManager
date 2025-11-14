package model;

import java.time.LocalDate;

/**
 * Representa uma tarefa que possui um prazo (data limite) e um responsável (assignee).
 * <p>
 * Esta classe estende {@link Task} e adiciona atributos específicos
 * para gerenciamento de prazos e responsabilidades.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class DeadlineTask extends Task implements java.io.Serializable {

    private LocalDate taskDeadline;
    private String assignee;

    /**
     * Construtor para criar uma nova Tarefa com Prazo.
     * @param id A identificação única da tarefa.
     * @param description A descrição da tarefa (não pode ser vazia).
     * @param priority A prioridade (1-5).
     * @param taskDeadline A data limite (não pode ser nula).
     * @param assignee O nome do responsável (não pode ser vazio).
     * @throws IllegalArgumentException Se qualquer parâmetro for inválido.
     */
    public DeadlineTask(String id, String description, int priority, LocalDate taskDeadline, String assignee) throws  IllegalArgumentException {
        super(id, description, priority);
        this.setTaskDeadline(taskDeadline);
        this.setAssignee(assignee);
    }

    // --- Getters ---

    /**
     * Obtém a data limite da tarefa.
     * @return O objeto {@link LocalDate} do prazo.
     */
    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    /**
     * Obtém o nome do responsável pela tarefa.
     * @return A String com o nome do responsável (assignee).
     */
    public String getAssignee() {
        return assignee;
    }

    // --- Setters ---

    /**
     * Define ou atualiza a data limite da tarefa, validando que não é nula.
     * @param taskDeadline A data limite (não pode ser nula).
     * @throws IllegalArgumentException Se a data for nula.
     */
    public void setTaskDeadline(LocalDate taskDeadline) throws IllegalArgumentException {
        if (taskDeadline == null) {
            throw new IllegalArgumentException("A data limite da tarefa não pode ser nula");
        }
        this.taskDeadline = taskDeadline;
    }

    /**
     * Define ou atualiza o responsável pela tarefa, validando que não é nulo ou vazio.
     * @param assignee O nome do responsável (não pode ser nulo ou vazio).
     * @throws IllegalArgumentException Se o nome for nulo ou vazio.
     */
    public void setAssignee(String assignee) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(assignee)) {
            throw new IllegalArgumentException("O nome do responsável (assignee) não pode ser nulo ou vazio.");
        }
        this.assignee = assignee;
    }

    /**
     * Retorna uma String formatada com os detalhes da Tarefa com Prazo,
     * incluindo o responsável e a data limite formatada (ex: "dd/MM/yyyy").
     * <p>
     * Formato: "[Tarefa c/ Prazo] Descrição (Resp: Nome, Prazo: dd/MM/yyyy) (Prioridade: X) - Status: STATUS"
     * </p>
     * @return A String formatada com os detalhes completos da tarefa.
     */
    @Override
    public String getDisplayDetails() {
        String dataFormatada = AppUtils.formatarData(this.getTaskDeadline());

        return String.format(
                "[Tarefa c/ Prazo] %s (Resp: %s, Prazo: %s) (Prioridade: %d) - Status: %s",
                this.getDescription(),
                this.getAssignee(),
                dataFormatada,
                this.getPriority(),
                this.getStatus().toString()
        );
    }
}
