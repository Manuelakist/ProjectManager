package model;

import java.time.LocalDate;

/**
 * Representa um Marco (Milestone) do projeto.
 * <p>
 * Um Marco é um tipo de {@link Task} que representa um ponto de verificação
 * ou uma entrega significativa, em vez de uma tarefa de trabalho.
 * Ele possui status próprios (PENDENTE, ATINGIDO).
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class Milestone extends Task {

    private LocalDate milestoneDate;

    /**
     * Construtor para um novo Marco.
     * <p>
     * Valida os parâmetros e define o status inicial padrão como {@code Status.PENDENTE}.
     * </p>
     *
     * @param description A descrição do marco.
     * @param priority A prioridade (1-5).
     * @param date A data do marco (não pode ser nula).
     * @throws IllegalArgumentException Se a descrição, prioridade ou data forem inválidas.
     */
    public Milestone(String description, int priority, LocalDate date)  throws IllegalArgumentException {
        super(description, priority);
        this.setMilestoneDate(date);
        this.setStatus(Status.PENDENTE);
    }

    // --- Getter ---

    /**
     * Obtém a data do marco.
     * @return O {@link LocalDate} do marco.
     */
    public LocalDate getMilestoneDate() {
        return milestoneDate;
    }

    // --- Setters ---

    /**
     * Define ou atualiza a data do marco, validando que não é nula.
     * @param milestoneDate A data (não pode ser nula).
     * @throws IllegalArgumentException Se a data for nula.
     */
    public void setMilestoneDate(LocalDate milestoneDate) throws IllegalArgumentException {
        if (milestoneDate == null) {
            throw new IllegalArgumentException("Data não deve ser nula");
        }
        this.milestoneDate = milestoneDate;
    }

    /**
     * Sobrescreve o {@code setStatus} da {@link Task} para garantir
     * que um Marco só possa ter os status PENDENTE ou ATINGIDO.
     * @param newStatus O novo status a ser definido.
     * @throws IllegalArgumentException Se o status não for PENDENTE ou ATINGIDO.
     */
    @Override
    public void setStatus(Status newStatus) throws IllegalArgumentException {
        if (newStatus == Status.PENDENTE || newStatus == Status.ATINGIDO) {
            this.status = newStatus;
        } else {
            throw new IllegalArgumentException(
                    "Status inválido para um Marco. Status permitidos: PENDENTE, ATINGIDO."
            );
        }
    }

    /**
     * Retorna uma String formatada com os detalhes do Marco (Milestone).
     * <p>
     * Formato: "[Marco] Descrição (Data: dd/MM/yyyy) (Prioridade: X) - Status: STATUS"
     * </p>
     * @return A String formatada com os detalhes completos do marco.
     */
    @Override
    public String getDisplayDetails() {
        String dataFormatada = AppUtils.formatarData(this.getMilestoneDate());

        return String.format(
                "[Marco] %s (Data: %s) (Prioridade: %d) - Status: %s",
                this.getDescription(),
                dataFormatada,
                this.getPriority(),
                this.getStatus().toString()
        );
    }
}
