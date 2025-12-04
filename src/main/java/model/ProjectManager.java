package model;

import java.io.StreamCorruptedException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Map;

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
    private final TaskFactory taskFactory;
    private long nextProjectId = 1;
    private long nextTaskId = 1;

    /**
     * Construtor do ProjectManager.
     * <p>
     * Inicializa a lista de projetos e define a estratégia de
     * persistência padrão (Serialização Java).
     * </p>
     */
    public ProjectManager() {
        this.projects = new ArrayList<>();
        this.dao = new SerializedProjectDAO("data/dados.dat");
        this.taskFactory = new TaskFactory();
    }

    // --- Métodos de Persistência (DAO) ---
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
                this.updateIdCountersAfterLoad();
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

    /**
     * Carrega projetos de um arquivo externo específico e os adiciona à lista atual.
     * @param file O arquivo selecionado pelo usuário.
     * @throws Exception Se ocorrer um erro imprevisto.
     */
    public void importProjectsFromFile(java.io.File file) throws StreamCorruptedException, ClassCastException, Exception {

        try {
            IPersistenceDAO externalDao = new SerializedProjectDAO(file.getAbsolutePath());
            ArrayList<Project> externalProjects = externalDao.load();

            if (externalProjects != null && !externalProjects.isEmpty()) {

                for (Project p : externalProjects) {
                    p.setId(String.valueOf(nextProjectId++));

                    for (Task t : p.getTasks()) {
                        t.setId(String.valueOf(nextTaskId++));
                    }
                }

                this.projects.addAll(externalProjects);
            }
        } catch (StreamCorruptedException e) {
            throw new StreamCorruptedException("tipo inválido. O arquivo deve ser .dat.");
        } catch (ClassCastException e) {
            throw new ClassCastException("os dados do arquivo são incompatíveis com o sistema.");
        } catch (Exception e) {
            throw new Exception("falha ao carregar dados");
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
        String newId = String.valueOf(nextProjectId);
        nextProjectId++;

        Project newProject = new Project(newId, name, generalDeadline);
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

    /**
     * Cria uma nova tarefa (de qualquer tipo) para um projeto.
     * Este método gera o ID, chama a TaskFactory e adiciona a
     * tarefa criada ao projeto correto.
     * @param projectId O ID do projeto que receberá a tarefa.
     * @param type O tipo de tarefa (SIMPLE, DEADLINE, etc.).
     * @param data O "mapa" de dados vindo da View.
     * @throws IllegalArgumentException Se o projeto não for encontrado ou os dados da tarefa forem inválidos.
     */
    public void createTaskForProject(String projectId, TaskType type, Map<String, Object> data)
            throws IllegalArgumentException {

        Project p = this.getProjectById(projectId);
        if (p == null) {
            throw new IllegalArgumentException("Projeto com ID " + projectId + " não encontrado.");
        }

        String newId = String.valueOf(nextTaskId++);

        Task task = this.taskFactory.createTask(newId, type, data);

        p.addTask(task);
    }

    /**
     * Método auxiliar privado para "avançar" os contadores de ID
     * após carregar os dados de um arquivo.
     * Isso evita colisões de ID ao criar novos itens.
     */
    private void updateIdCountersAfterLoad() {
        long maxProjectId = 0;
        long maxTaskId = 0;

        for (Project p : this.projects) {
            try {
                long pId = Long.parseLong(p.getId());
                if (pId > maxProjectId) {
                    maxProjectId = pId;
                }

                for (Task t : p.getTasks()) {
                    long tId = Long.parseLong(t.getId());
                    if (tId > maxTaskId) {
                        maxTaskId = tId;
                    }
                }
            } catch (NumberFormatException e) {
                // Ignora IDs que não são numéricos
            }
        }

        this.nextProjectId = maxProjectId + 1;
        this.nextTaskId = maxTaskId + 1;

        System.out.println("Contadores de ID atualizados: Próximo Projeto = " + nextProjectId + ", Próxima Tarefa = " + nextTaskId);
    }

}
