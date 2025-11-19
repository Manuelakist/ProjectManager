package model;

import java.io.Serializable;

public enum TaskType implements Serializable {
    SIMPLE("Tarefa Simples"),
    DEADLINE("Tarefa com Prazo"),
    MILESTONE("Marco (Milestone)");

    private final String displayName;

    /**
     * Construtor privado do Enum.
     * @param displayName O nome a ser exibido na UI.
     */
    TaskType(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sobrescreve o método padrão toString() do Java.
     * Para retornar o nome "formatado"
     * </p>
     * @return O nome de exibição (ex: "Tarefa Simples").
     */
    @Override
    public String toString() {
        return this.displayName;
    }
}