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
     * Formata um objeto {@link LocalDate} para o padrão brasileiro (dd/MM/AAAA).
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

    /**
     * Valida se um objeto {@link LocalDate} representa uma data futura e é não nula.
     * <p>O setter do objeto {@link Project} utiliza esse método
     * para garantir que não sejam definidos projetos para o passado.</p>
     * @param date O objeto LocalDate a ser checado
     * @return true se a data for no passado, false caso contrário
     */
    public static boolean isDateInPast(LocalDate date) {
        return date == null || date.isBefore(LocalDate.now());
    }

    /**
     * Valida se uma String é menor que um tamanho máximo recebido
     * e chama {@code IsStringNullOrEmpty()} para garantir que ela não é nula ou vazia.
     * @param str A String a ser checada
     * @param max o comprimento máximo da String
     * @return true se a String é menor que o tamanho máximo (e não nula ou vazia), false caso contrário
     */
    public static boolean isInvalidLength(String str, int max) {
        return isStringNullOrEmpty(str) || str.length() > max;
    }

}
