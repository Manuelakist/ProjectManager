// Em src/model/TaskFactory.java
package model;

import java.time.LocalDate;
import java.util.Map;

/**
 * Fábrica (Factory) responsável por criar todos os tipos de Tasks.
 *
 * @author Manuela Skrsypcsak Kist
 */
public class TaskFactory {

    /**
     * Cria uma instância de uma subclasse de Task.
     * @param id O ID único (gerado pelo ProjectManager).
     * @param type O tipo de tarefa a ser criada (SIMPLE, DEADLINE, etc.).
     * @param data Um "mapa" contendo todos os dados necessários.
     * @return A instância da Task concreta.
     * @throws IllegalArgumentException Se dados essenciais estiverem faltando ou forem inválidos.
     */
    public Task createTask(String id, TaskType type, Map<String, Object> data) {

        String description = (String) data.get("description");
        int priority = (int) data.get("priority");

        switch (type) {
            case SIMPLE:
                return new SimpleTask(id, description, priority);

            case DEADLINE:
                LocalDate deadline = (LocalDate) data.get("deadline");
                return new DeadlineTask(id, description, priority, deadline);

            case MILESTONE:
                LocalDate date = (LocalDate) data.get("milestoneDate");
                return new Milestone(id, description, priority, date);

            default:
                throw new IllegalArgumentException("Tipo de tarefa desconhecido: " + type);
        }
    }
}