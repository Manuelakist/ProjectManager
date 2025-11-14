package view;

/**
 * Interface (Produto Abstrato) para a tela/menu principal.
 * <p>
 * Define o comportamento essencial que qualquer menu principal
 * (textual ou gráfico) deve ter.
 * </p>
 */
public interface IMainMenuView {

    /**
     * Inicia e exibe a tela.
     * <p>
     * Em uma view textual, isso iniciará o loop principal do menu.
     * Em uma view gráfica (GUI), isso tornará a janela (JFrame) visível.
     * </p>
     */
    void display();
}