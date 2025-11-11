package view;

/**
 * Interface (Produto Abstrato) para a tela de detalhes do projeto.
 * <p>
 * Define o comportamento essencial que qualquer tela de gerenciamento
 * de projeto (textual ou gráfica) deve ter.
 * </p>
 */
public interface IProjectView {

    /**
     * Inicia e exibe os detalhes do projeto e suas tarefas.
     * <p>
     * Em uma view textual, isso iniciará um novo loop de menu.
     * Em uma view gráfica (GUI), isso tornará a janela visível.
     * </p>
     */
    void displayProjectDetails();

}