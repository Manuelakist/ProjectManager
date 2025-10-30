package model;

import java.util.UUID;

/**
 * Classe utilitária (utility class) final para o pacote Model.
 * * <p>Esta classe agrupa métodos estáticos que são usados em várias partes
 * do sistema, como geração de IDs e validações de regras de negócio.</p>
 * * <p>Ela não pode ser instanciada (possui um construtor privado)
 * e não deve ser herdada (é final).</p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public final class AppUtils {

    private AppUtils() {
        // impede a instanciação
    }

    /**
     * Gera um ID universalmente único (UUID).
     * A chance de colisão é considerada zero para todos os
     * propósitos práticos.
     * @return Uma String de 36 caracteres única (ex: "123e4567-e89b-12d3...").
     */
    public static String generateUniqueId() {
        return UUID.randomUUID().toString();
    }

    /**
     * Valida se a prioridade de uma tarefa está dentro do range permitido (1 a 5).
     * @param priority O número da prioridade a ser checado.
     * @return true se a prioridade for válida (entre 1 e 5), false caso contrário.
     */
    public static boolean isValidPriority(int priority) {
        return priority >= 1 && priority <= 5;
    }

}
