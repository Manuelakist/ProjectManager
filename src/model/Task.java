package model;

/**
 * Classe abstrata que representa uma unidade de trabalho genérica (Tarefa).
 * <p>
 * Define os atributos e comportamentos comuns a todas as tarefas do sistema,
 * como ID, descrição, prioridade e status. Ela deve ser estendida por
 * classes concretas (como SimpleTask, DeadlineTask, etc.) que implementarão
 * seus comportamentos polimórficos.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public abstract class Task implements java.io.Serializable {

    private final String id;
    private String description;
    private int priority;
    protected Status status;

    /**
     * Construtor para uma nova Task.
     * Valida os parâmetros de entrada usando os setters da própria classe.
     * O ID é gerado automaticamente e o status é inicializado como A_FAZER.
     * @param id A identificação única da tarefa.
     * @param description A descrição da tarefa.
     * @param priority A prioridade (deve ser entre 1-5, senão assume 1).
     * @throws IllegalArgumentException Se a descrição ou prioridade forem inválidas.
     */
    public Task(String id, String description, int priority) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(id)) {
            throw new IllegalArgumentException("O ID não pode ser nulo ou vazio.");
        }
        this.id = id;
        this.setDescription(description);
        this.setPriority(priority);
        this.status = Status.A_FAZER;
    }

    // --- GETTERS ---

    /**
     * Obtém o ID único da tarefa.
     * @return A String do ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém a descrição da tarefa.
     * @return A String da descrição.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Obtém o nível de prioridade da tarefa.
     * @return Um inteiro (normalmente 1-5).
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Obtém o status atual da tarefa.
     * @return O valor do enum {@link Status} (ex: Status.EM_PROGRESSO).
     */
    public Status getStatus() {
        return status;
    }

    // --- SETTERS ---

    /**
     * Atualiza a descrição da tarefa após validar que não é nula ou vazia.
     * @param description A nova descrição.
     * @throws IllegalArgumentException Se a descrição for inválida.
     */
    public void setDescription(String description) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(description)) {
            throw new IllegalArgumentException("A descrição da tarefa não pode ser nula ou vazia.");
        }
        this.description = description;
    }

    /**
     * Define a prioridade da tarefa, validando-a antes de atribuir.
     * @param priority Um valor entre 1 e 5.
     * @throws IllegalArgumentException Se a prioridade for inválida.
     */
    public void setPriority(int priority) throws IllegalArgumentException {
        if (!AppUtils.isValidPriority(priority)) {
            throw new IllegalArgumentException("Prioridade inválida. Deve ser entre 1 e 5. Recebido: " + priority);
        }
        this.priority = priority;
    }

    /**
     * Define o status da tarefa.
     * Esta é a implementação padrão de validação, usada por
     * {@link SimpleTask} e {@link DeadlineTask}.
     * Ela só permite os status básicos de fluxo de trabalho.
     * @param status O novo status a ser definido.
     * @throws IllegalArgumentException Se o status não for A_FAZER,
     * EM_PROGRESSO, ou CONCLUIDO.
     */
    public void setStatus(Status status) throws IllegalArgumentException {
        if (status == Status.A_FAZER ||
                status == Status.EM_PROGRESSO ||
                status == Status.CONCLUIDO)
        {
            this.status = status;
        } else {
            throw new IllegalArgumentException(
                    "Status inválido para este tipo de tarefa. Status permitidos: A_FAZER, EM_PROGRESSO, CONCLUIDO."
            );
        }
    }

    // --- MÉTODOS ABSTRATOS ---

    /**
     * Retorna uma String formatada com os detalhes completos da tarefa.
     * @return Uma String com os detalhes formatados para exibição.
     */
    public abstract String getDisplayDetails();

    /**
     * Retorna um array de Status que são válidos para ESTE tipo de tarefa.
     * A View usará este método para filtrar o menu de opções
     * e mostrar ao usuário apenas os status relevantes.
     * @return Um array de Status válidos (ex: [A_FAZER, EM_PROGRESSO, CONCLUIDO]).
     */
    public abstract Status[] getValidStatuses();
}
