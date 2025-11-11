package view;

import model.Project;
import model.ProjectManager;

/**
 * Fábrica Concreta para criar componentes de UI (interface) no modo Textual (console).
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class TextualViewFactory implements IViewFactory {

    /**
     * {@inheritDoc}
     * <p>
     * Cria e retorna uma nova instância da tela principal no modo texto.
     * </p>
     * @param manager A instância do ProjectManager.
     * @return Uma instância de {@link TextualMainMenuView}.
     */
    @Override
    public IMainMenuView createMainMenuView(ProjectManager manager) {
        return new TextualMainMenuView(manager);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cria e retorna uma nova instância da tela de projeto no modo texto.
     * </p>
     * @param manager A instância do ProjectManager.
     * @param project O Projeto específico que esta tela irá exibir.
     * @return Uma instância de {@link TextualProjectView}.
     */
    @Override
    public IProjectView createProjectView(ProjectManager manager, Project project) {
        return new TextualProjectView(manager, project);
    }
}