package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Representa um Projeto, que é o contêiner principal para um conjunto de Tarefas.
 * <p>
 * Esta é uma das classes de negócio centrais do sistema. Ela gerencia
 * uma lista polimórfica de objetos {@link Task} e é capaz de calcular
 * seu próprio progresso com base no status dessas tarefas.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class Project {

    private String id;
    private String name;
    private LocalDate generalDeadline;
    private ArrayList<Task> tasks;

    /**
     * Construtor para criar um novo Projeto.
     * <p>
     * Valida os parâmetros de entrada usando os setters, gera um ID único
     * e inicializa a lista de tarefas como vazia.
     * </p>
     * @param name O nome do projeto (não pode ser nulo ou vazio).
     * @param generalDeadline O prazo final para o projeto (não pode ser nulo).
     * @throws IllegalArgumentException Se o nome ou o prazo forem inválidos.
     */
    public Project(String name, LocalDate generalDeadline) throws IllegalArgumentException {
        this.id = AppUtils.generateUniqueId();

        this.setName(name);
        this.setGeneralDeadline(generalDeadline);

        this.tasks = new ArrayList<>();
    }

    // --- Métodos de Gerenciamento de Tarefas (CRUD) ---

    /**
     * Adiciona uma nova tarefa à lista de tarefas do projeto.
     * @param task A tarefa a ser adicionada (não pode ser nula).
     * @throws IllegalArgumentException Se a tarefa for nula.
     */
    public void addTask(Task task) throws IllegalArgumentException {
        if (task == null) {
            throw new IllegalArgumentException("A tarefa não pode ser nula.");
        }
        this.tasks.add(task);
    }

    /**
     * Remove uma tarefa da lista com base no seu ID.
     * @param taskId O ID da tarefa a ser removida.
     * @return true se a tarefa foi encontrada e removida, false caso contrário.
     */
    public boolean removeTask(String taskId) {
        if (AppUtils.isStringNullOrEmpty(taskId)) {
            return false;
        }
        return this.tasks.removeIf(task -> task.getId().equals(taskId));
    }

    /**
     * Busca e retorna uma tarefa específica da lista com base no seu ID.
     * @param taskId O ID da tarefa a ser encontrada.
     * @return O objeto {@link Task} se encontrado, ou {@code null} se não encontrado.
     */
    public Task getTaskById(String taskId) {
        if (AppUtils.isStringNullOrEmpty(taskId)) {
            return null;
        }

        for (Task task : this.tasks) {
            if (task.getId().equals(taskId)) {
                return task;
            }
        }
        return null;
    }

    // --- Lógica de Negócio ---

    /**
     * Calcula o percentual de conclusão do projeto.
     * <p>
     * A lógica se baseia em quantas tarefas estão em um estado "finalizado"
     * ({@code Status.CONCLUIDO} ou {@code Status.ATINGIDO} ou {@code Status.CORRIGIDO}).
     * </p>
     * @return Um double entre 0.0 e 100.0 representando a porcentagem de conclusão.
     */
    public double getProgressPercentage() {
        if (tasks.isEmpty()) {
            return 0.0;
        }

        double concluidas = 0;
        for (Task task : tasks) {
            Status status = task.getStatus();
            if (status == Status.CONCLUIDO ||
                    status == Status.ATINGIDO ||
                    status == Status.CORRIGIDO)
            {
                concluidas++;
            }
        }

        return (concluidas / this.tasks.size()) * 100.0;
    }

    // --- GETTERS ---

    /**
     * Obtém o ID único do projeto.
     * @return A String do ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Obtém o nome do projeto.
     * @return A String do nome.
     */
    public String getName() {
        return name;
    }

    /**
     * Obtém a data limite do projeto.
     * @return O objeto {@link LocalDate} da data limite.
     */
    public LocalDate getGeneralDeadline() {
        return generalDeadline;
    }

    /**
     * Obtém a lista inteira de tarefas.
     * @return A {@link ArrayList} de {@link Task}s.
     */
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    // --- SETTERS ---

    /**
     * Define o nome do projeto, validando que não é nulo ou vazio.
     * @param name O novo nome para o projeto.
     * @throws IllegalArgumentException Se o nome for nulo ou vazio.
     */
    public void setName(String name) throws IllegalArgumentException {
        if (AppUtils.isStringNullOrEmpty(name)) {
            throw new IllegalArgumentException("O nome do projeto não pode ser nulo ou vazio.");
        }
        this.name = name;
    }

    /**
     * Define a data limite geral do projeto, validando que não é nula.
     * @param generalDeadline A nova data limite.
     * @throws IllegalArgumentException Se a data for nula.
     */
    public void setGeneralDeadline(LocalDate generalDeadline) throws IllegalArgumentException {
        if (generalDeadline == null) {
            throw new IllegalArgumentException("A data limite geral do projeto não pode ser nula.");
        }
        this.generalDeadline = generalDeadline;
    }

}
