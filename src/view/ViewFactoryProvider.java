package view;

import view.gui.GuiViewFactory;
import view.textual.TextualViewFactory;

/**
 * Gerenciador estático (Provider/Singleton) da Fábrica Abstrata.
 * <p>
 * Esta classe é responsável por guardar a instância concreta
 * da fábrica (TextualViewFactory ou GuiViewFactory) que
 * está sendo usada pela aplicação.
 * </p>
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public final class ViewFactoryProvider {

    /** A única instância da fábrica concreta (Singleton). */
    private static IViewFactory instance;

    /** Construtor privado para impedir instanciação. */
    private ViewFactoryProvider() {}

    /**
     * Configura qual fábrica concreta será usada pela aplicação.
     * Esta método DEVE ser chamado pelo Main.java antes de qualquer outra coisa.
     * @param mode A string "textual" ou "gui".
     * @throws IllegalArgumentException Se o modo for desconhecido.
     */
    public static void configure(String mode) throws IllegalArgumentException {
        if ("textual".equalsIgnoreCase(mode)) {
            instance = new TextualViewFactory();
        } else if ("gui".equalsIgnoreCase(mode)) {
            instance = new GuiViewFactory();
        } else {
            throw new IllegalArgumentException("Modo de fábrica desconhecido: " + mode);
        }
    }

    /**
     * Obtém a instância da fábrica que foi configurada.
     * @return A instância de IViewFactory (TextualViewFactory ou GuiViewFactory).
     * @throws IllegalStateException Se configure() não foi chamado primeiro.
     */
    public static IViewFactory getFactory() {
        if (instance == null) {
            throw new IllegalStateException("IViewFactory não foi configurada. " +
                    "Chame ViewFactoryProvider.configure() primeiro.");
        }
        return instance;
    }
}