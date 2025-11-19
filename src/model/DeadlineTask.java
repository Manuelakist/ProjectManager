package model;

import java.io.Serializable;
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
public class DeadlineTask extends Task implements Serializable {

    private LocalDate taskDeadline;

    /**
     * Construtor para criar uma nova Tarefa com Prazo.
     * @param id A identificação única da tarefa.
     * @param description A descrição da tarefa (não pode ser vazia).
     * @param priority A prioridade (1-5).
     * @param taskDeadline A data limite (não pode ser nula).
     * @throws IllegalArgumentException Se qualquer parâmetro for inválido.
     */
    public DeadlineTask(String id, String description, int priority, LocalDate taskDeadline) throws  IllegalArgumentException {
        super(id, description, priority);
        this.setTaskDeadline(taskDeadline);
    }

    // --- Getter ---

    /**
     * Obtém a data limite da tarefa.
     * @return O objeto {@link LocalDate} do prazo.
     */
    public LocalDate getTaskDeadline() {
        return taskDeadline;
    }

    // --- Setter ---

    /**
     * Define ou atualiza a data limite da tarefa, validando que não é nula
     * (pode ser uma data passada caso o usuário queira registrar tarefas anteriores
     * para manter um histórico).
     * @param taskDeadline A data limite (não pode ser nula).
     * @throws IllegalArgumentException Se a data for nula.
     */
    public void setTaskDeadline(LocalDate taskDeadline) throws IllegalArgumentException {
        if (taskDeadline == null) {
            throw new IllegalArgumentException("A data limite da tarefa não pode ser nula.");
        }
        this.taskDeadline = taskDeadline;
    }

    /**
     * Retorna uma String formatada com os detalhes da Tarefa com Prazo,
     * incluindo o responsável e a data limite formatada (ex: "dd/MM/yyyy").
     * <p>
     * Formato: "[Tarefa c/ Prazo] Descrição (Prazo: dd/MM/yyyy) (Prioridade: X) - Status: STATUS"
     * </p>
     * @return A String formatada com os detalhes completos da tarefa.
     */
    @Override
    public String getDisplayDetails() {
        String dataFormatada = AppUtils.formatarData(this.getTaskDeadline());

        return String.format(
                "[Tarefa c/ Prazo] %s (Prazo: %s) (Prioridade: %d) - Status: %s",
                this.getDescription(),
                dataFormatada,
                this.getPriority(),
                this.getStatus().toString()
        );
    }

    /**
     * Retorna os status válidos para uma Tarefa com Prazo.
     * @return Array de Status [A_FAZER, EM_PROGRESSO, CONCLUIDO]
     */
    @Override
    public Status[] getValidStatuses() {
        return new Status[] {
                Status.A_FAZER,
                Status.EM_PROGRESSO,
                Status.CONCLUIDO
        };
    }
}
