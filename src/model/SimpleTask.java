package model;

import java.io.Serializable;

/**
 * Representa uma tarefa simples, a implementação mais básica de uma {@link Task}.
 * <p>
 * Esta classe não adiciona novos atributos. Ela apenas fornece uma
 * implementação concreta para {@code getDisplayDetails} e herda toda a
 * lógica de validação (incluindo o {@code setStatus}) diretamente da
 * sua superclasse {@link Task}.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class SimpleTask extends Task implements Serializable {

    /**
     * Construtor para criar uma nova Tarefa Simples.
     * @param id A identificação única da tarefa.
     * @param description A descrição da tarefa (não pode ser nula ou vazia).
     * @param priority A prioridade da tarefa (deve ser entre 1-5).
     * @throws IllegalArgumentException Se a descrição ou prioridade forem inválidas (lançada pelo construtor da superclasse).
     */
    public SimpleTask(String id, String description, int priority) throws IllegalArgumentException {
        super(id, description, priority);
    }

    /**
     * Retorna uma String formatada com os detalhes específicos da Tarefa Simples.
     * <p>
     * Formato: "[Tarefa Simples] Descrição (Prioridade: X) - Status: STATUS"
     * </p>
     * @return A String formatada com os detalhes da tarefa.
     */
    @Override
    public String getDisplayDetails() {
        return String.format(
                "[Tarefa Simples] %s (Prioridade: %d) - Status: %s",
                this.getDescription(),
                this.getPriority(),
                this.getStatus().toString()
        );
    }

    /**
     * Retorna os status válidos para uma Tarefa Simples.
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
