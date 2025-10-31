package model;

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
public class SimpleTask extends Task {

    /**
     * Construtor para criar uma nova Tarefa Simples.
     * @param description A descrição da tarefa (não pode ser nula ou vazia).
     * @param priority A prioridade da tarefa (deve ser entre 1-5).
     * @throws IllegalArgumentException Se a descrição ou prioridade forem inválidas (lançada pelo construtor da superclasse).
     */
    public SimpleTask(String description, int priority) throws IllegalArgumentException {
        super(description, priority);
    }

    /**
     * Retorna uma String formatada com os detalhes específicos da Tarefa Simples.
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

}
