package model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe utilitária (utility class) estática final para o pacote Model.
 * <p>Esta classe agrupa métodos estáticos que são usados em várias partes
 * do sistema, como validações de regras de negócio.</p>
 * <p>Ela não pode ser instanciada (possui um construtor privado)
 * e não deve ser herdada (é final).</p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public final class AppUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private AppUtils() {
        // impede a instanciação
    }

    /**
     * Valida se a prioridade de uma tarefa está dentro do range permitido (1 a 5).
     * @param priority O número da prioridade a ser checado.
     * @return true se a prioridade for válida (entre 1 e 5), false caso contrário.
     */
    public static boolean isValidPriority(int priority) {
        return priority >= 1 && priority <= 5;
    }

    /**
     * Valida se uma String é nula, vazia ou contém apenas espaços em branco.
     * @param str A String a ser checada.
     * @return true se a String for nula ou vazia, false caso contrário.
     */
    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Formata um objeto LocalDate para o padrão brasileiro (dd/MM/AAAA).
     * Retorna "Data Inválida" se a data for nula.
     * @param data O objeto LocalDate a ser formatado.
     * @return A data formatada como String.
     */
    public static String formatarData(LocalDate data) {
        if (data == null) {
            return "N/A";
        }
        return data.format(DATE_FORMATTER);
    }

}
