package view.gui;

import model.Project;
import model.ProjectManager;
import view.IMainMenuView;
import view.IProjectView;
import view.IViewFactory;

/**
 * Fábrica Concreta para criar componentes de UI (interface) no modo Gráfico (Swing).
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class GuiViewFactory implements IViewFactory {

    /**
     * {@inheritDoc}
     * <p>
     * Cria e retorna uma nova instância da tela principal no modo gráfico (Swing).
     * </p>
     * @param manager A instância do ProjectManager.
     * @return Uma instância de {@link GuiMainMenuView}.
     */
    @Override
    public IMainMenuView createMainMenuView(ProjectManager manager) {
        return new GuiMainMenuView(manager);
    }

    /**
     * {@inheritDoc}
     * <p>
     * Cria e retorna uma nova instância da tela de projeto no modo gráfico (Swing).
     * </p>
     * @param manager A instância do ProjectManager.
     * @param project O Projeto específico que esta tela irá exibir.
     * @return Uma instância de {@link GuiProjectView}.
     */
    @Override
    public IProjectView createProjectView(ProjectManager manager, Project project) {
        return new GuiProjectView(manager, project);
    }
}