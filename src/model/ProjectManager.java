package model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Classe principal do Model (Façade).
 * <p>
 * Suas responsabilidades incluem:
 * 1. Gerenciar a lista principal de Projetos.
 * 2. Orquestrar as operações de salvar e carregar dados (usando um DAO).
 * 3. Fornecer uma API pública (métodos) para a View realizar o CRUD de Projetos.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class ProjectManager {

    private final ArrayList<Project> projects;
    private IPersistenceDAO dao;

    /**
     * Construtor do ProjectManager.
     * <p>
     * Inicializa a lista de projetos e define a estratégia de
     * persistência padrão (Serialização Java).
     * </p>
     */
    public ProjectManager() {
        this.projects = new ArrayList<>();
        this.dao = new SerializedProjectDAO("dados.dat");
    }

    // --- Métodos de Persistência (DAO) ---

    /**
     * Define (ou troca) a estratégia de persistência a ser usada.
     * A view pode usar isso para permitir que o usuário escolha
     * entre salvar como .json ou .dat.
     * @param dao A instância do DAO a ser usada (não pode ser nula).
     * @throws IllegalArgumentException Se o DAO for nulo.
     */
    public void setPersistenceStrategy(IPersistenceDAO dao) throws IllegalArgumentException {
        if (dao == null) {
            throw new IllegalArgumentException("A estratégia de persistência (DAO) não pode ser nula.");
        }
        this.dao = dao;
    }

    /**
     * Carrega a lista de projetos do arquivo usando a estratégia de DAO atual.
     * <p>
     * Se o arquivo não existir ou falhar ao carregar, ele apenas imprimirá
     * um erro e continuará com uma lista de projetos vazia.
     * </p>
     */
    public void loadData() {
        try {
            ArrayList<Project> loadedProjects = this.dao.load();

            this.projects.clear();
            if (loadedProjects != null) {
                this.projects.addAll(loadedProjects);
            }

        } catch (Exception e) {
            System.err.println("AVISO: Erro ao carregar dados. Iniciando com lista vazia. Erro: " + e.getMessage());
            this.projects.clear();
        }
    }

    /**
     * Salva a lista de projetos ATUAL no arquivo usando a estratégia de DAO atual.
     */
    public void saveData() {
        try {
            this.dao.save(this.projects);
            System.out.println("Dados salvos com sucesso.");
        } catch (Exception e) {
            System.err.println("ERRO CRÍTICO: Falha ao salvar dados: " + e.getMessage());
        }
    }

    // --- Métodos de CRUD de Projeto ---

    /**
     * Cria um novo projeto, valida-o e o adiciona à lista.
     * @param name O nome do projeto.
     * @param generalDeadline O prazo final.
     * @throws IllegalArgumentException Se o nome ou data forem inválidos (lançado pelo construtor do Project).
     */
    public void createProject(String name, LocalDate generalDeadline) throws IllegalArgumentException {
        Project newProject = new Project(name, generalDeadline);
        this.projects.add(newProject);
    }

    /**
     * Retorna a lista completa de todos os projetos.
     * @return A ArrayList de {@link Project}s.
     */
    public ArrayList<Project> getProjects() {
        return this.projects;
    }

    /**
     * Busca um projeto pelo seu ID.
     * @param projectId O ID do projeto a ser encontrado.
     * @return O objeto {@link Project}, ou {@code null} se não for encontrado.
     */
    public Project getProjectById(String projectId) {
        if (AppUtils.isStringNullOrEmpty(projectId)) {
            return null;
        }

        for (Project p : this.projects) {
            if (p.getId().equals(projectId)) {
                return p;
            }
        }
        return null;
    }

    /**
     * Atualiza os dados de um projeto existente.
     * @param projectId O ID do projeto a ser atualizado.
     * @param newName O novo nome para o projeto.
     * @param newDeadline O novo prazo para o projeto.
     * @return true se o projeto foi encontrado e atualizado, false se não foi encontrado.
     * @throws IllegalArgumentException Se o novo nome ou data forem inválidos (lançado pelos setters do Project).
     */
    public boolean updateProject(String projectId, String newName, LocalDate newDeadline) throws IllegalArgumentException {
        Project projectToUpdate = this.getProjectById(projectId);

        if (projectToUpdate != null) {
            projectToUpdate.setName(newName);
            projectToUpdate.setGeneralDeadline(newDeadline);
            return true;
        }
        return false;
    }

    /**
     * Exclui um projeto da lista com base no seu ID.
     * @param projectId O ID do projeto a ser excluído.
     * @return true se o projeto foi encontrado e removido, false caso contrário.
     */
    public boolean deleteProject(String projectId) {
        if (AppUtils.isStringNullOrEmpty(projectId)) {
            return false;
        }
        return this.projects.removeIf(project -> project.getId().equals(projectId));
    }

}
