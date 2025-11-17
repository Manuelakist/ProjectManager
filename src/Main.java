import com.formdev.flatlaf.intellijthemes.FlatDarkPurpleIJTheme;
import com.formdev.flatlaf.intellijthemes.FlatGruvboxDarkSoftIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatGitHubDarkIJTheme;
import com.formdev.flatlaf.intellijthemes.materialthemeuilite.FlatMoonlightIJTheme;
import model.ProjectManager;
import view.IViewFactory;
import view.IMainMenuView;
import view.ViewFactoryProvider;

import javax.swing.*;

/**
 * Ponto de entrada da aplicação.
 * <p>
 * Esta classe é responsável
 * por inicializar o Model (ProjectManager) e a View (IViewFactory)
 * e "ligar" os dois.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class Main {

    /**
     * Método principal que inicia a aplicação.
     * @param args Argumentos de linha de comando (não utilizados).
     */
    public static void main(String[] args) {

        try {
//          UIManager.setLookAndFeel(new FlatGitHubDarkIJTheme());
            UIManager.setLookAndFeel(new FlatDarkPurpleIJTheme());
//          UIManager.setLookAndFeel(new FlatMoonlightIJTheme());
        } catch(Exception ex) {
            System.err.println("Falha ao iniciar o Look and Feel (FlatLaf).");
        }

        ProjectManager manager = new ProjectManager();

        manager.loadData();

        try {
            // Para a interface textual: "textual"
            // Para a interface gráfica: "gui"
            ViewFactoryProvider.configure("gui");

            IViewFactory factory = ViewFactoryProvider.getFactory();

            IMainMenuView mainMenuView = factory.createMainMenuView(manager);

            mainMenuView.display();

        } catch (Exception e) {
            System.err.println("Falha crítica ao iniciar a aplicação:");
            e.printStackTrace();
        }
    }
}