package model;

public enum Status implements java.io.Serializable {

    A_FAZER("A FAZER"),
    EM_PROGRESSO("EM PROGRESSO"),
    CONCLUIDO("CONCLUÍDO"),

    PENDENTE("PENDENTE"),
    ATINGIDO("ATINGIDO");

    private final String displayName;

    Status(String displayName) {
        this.displayName = displayName;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Sobrescreve o método padrão toString() do Java.
     * Para retornar o nome "formatado"
     * </p>
     * @return O nome de exibição (ex: "EM PROGRESSO").
     */
    @Override
    public String toString() {
        return this.displayName;
    }

}

