package view;

import model.Project;
import model.ProjectManager;

/**
 * Interface da Fábrica Abstrata (Abstract Factory).
 * <p>
 * Define o contrato para criar as famílias de componentes de UI
 * (seja Textual ou Gráfica-GUI).
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public interface IViewFactory {

    /**
     * Cria a tela/menu principal da aplicação.
     * @param manager A instância do ProjectManager.
     * @return Uma instância de IMainMenuView (Textual ou Gráfica).
     */
    IMainMenuView createMainMenuView(ProjectManager manager);

    /**
     * Cria a tela de visualização/gerenciamento de um único projeto.
     * @param manager A instância do ProjectManager.
     * @param project O Projeto específico que esta tela irá exibir.
     * @return Uma instância de IProjectView (Textual ou Gráfica).
     */
    IProjectView createProjectView(ProjectManager manager, Project project);
    
}