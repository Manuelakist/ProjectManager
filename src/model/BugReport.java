package model;

import java.time.LocalDate;

/**
 * Representa um Relatório de Bug (Bug Report).
 * <p>
 * Esta classe estende {@link Task} e adiciona atributos específicos
 * para rastreamento de bugs, como severidade e os passos para
 * reproduzir o erro.
 * </p>
 * <p>
 * Ela possui seu próprio conjunto de status (RELATADO, EM_CORRECAO, CORRIGIDO).
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class BugReport extends Task implements java.io.Serializable {

    private String stepsToReproduce;
    private String severity;

    /**
     * Construtor para um novo Bug Report.
     * <p>
     * Valida os parâmetros e define o status inicial padrão como {@code Status.RELATADO}.
     * </p>
     * @param id A identificação única da tarefa.
     * @param description A descrição do bug (o que está quebrado?).
     * @param priority A prioridade para corrigir (1-5).
     * @param severity A gravidade do bug (não pode ser vazia).
     * @param steps Os passos para reproduzir (não pode ser vazio).
     * @throws IllegalArgumentException Se qualquer parâmetro for inválido.
     */
    public BugReport(String id, String description, int priority, String steps, String severity) throws IllegalArgumentException {
        super(id, description, priority);
        this.setStepsToReproduce(steps);
        this.setSeverity(severity);
        this.setStatus(Status.RELATADO);
    }

    // --- Getters ---

    /**
     * Obtém as intruções passo a passo para fazer o bug acontecer.
     * @return A String com o passo a passo.
     */
    public String getStepsToReproduce() {
        return stepsToReproduce;
    }

    /**
     * Obtém a gravidade do bug.
     * @return A String com a gravidade (ex: "Baixa", "Média", "Crítica").
     */
    public String getSeverity() {
        return severity;
    }

    // --- Setters ---

    /**
     * Define os passos para reproduzir o bug.
     * @param steps O texto com os passos (não pode ser nulo ou vazio).
     * @throws IllegalArgumentException Se os passos forem nulos ou vazios.
     */
    public void setStepsToReproduce(String steps) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(steps)) {
            throw new IllegalArgumentException("\"Passos para reproduzir\" não pode ser nulo.");
        }
        this.stepsToReproduce = steps;
    }

    /**
     * Define a severidade do bug (ex: "Crítica", "Média", "Baixa").
     * @param severity A string de severidade (não pode ser nula ou vazia).
     * @throws IllegalArgumentException Se a severidade for nula ou vazia.
     */
    public void setSeverity(String severity) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(severity)) {
            throw new IllegalArgumentException("Gravidade não pode ser nula");
        }
        this.severity = severity;
    }

    /**
     * Sobrescreve o {@code setStatus} da {@link Task} para garantir
     * que um Bug Report só possa ter os status RELATADO, EM_CORRECAO, ou CORRIGIDO.
     * @param newStatus O novo status a ser definido.
     * @throws IllegalArgumentException Se o status não for um dos status de bug válidos.
     */
    @Override
    public void setStatus(Status newStatus) throws IllegalArgumentException {
        if (newStatus == Status.RELATADO ||
                newStatus == Status.EM_CORRECAO ||
                newStatus == Status.CORRIGIDO)
        {
            this.status = newStatus;
        } else {
            throw new IllegalArgumentException(
                    "Status inválido para um Bug Report. Status permitidos: RELATADO, EM_CORRECAO, CORRIGIDO."
            );
        }
    }

    /**
     * Retorna uma String formatada com os detalhes resumidos do Bug Report,
     * focando na descrição e na severidade do bug.
     * <p>
     * Formato: "[Bug Report] Descrição (Severidade: Alta) (Prioridade: X) - Status: STATUS"
     * </p>
     * @return A String formatada com o resumo do bug.
     */
    @Override
    public String getDisplayDetails() {
        return String.format(
                "[Bug Report] %s (Severidade: %s) (Prioridade: %d) - Status: %s",
                this.getDescription(),
                this.getSeverity(),
                this.getPriority(),
                this.getStatus().toString()
        );
    }

    /**
     * Retorna os status válidos para um Relatório de Bug.
     * @return Array de Status [RELATADO, EM_CORRECAO, CORRIGIDO]
     */
    @Override
    public Status[] getValidStatuses() {
        return new Status[] {
                Status.RELATADO,
                Status.EM_CORRECAO,
                Status.CORRIGIDO
        };
    }
}
