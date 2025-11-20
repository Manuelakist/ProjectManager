package model;

import java.util.ArrayList;
import java.io.IOException;

/**
 * Interface (Contrato) que define o Padrão de Projeto DAO (Data Access Object)
 * e o Padrão de Projeto Strategy (Estratégia) para a persistência de dados.
 * <p>
 * O objetivo principal desta interface é **desacoplar** (diminuir o acoplamento)
 * a lógica de negócios (o {@link ProjectManager}) dos detalhes de
 * implementação de *como* os dados são salvos.
 * </p>
 * <p>
 * O {@link ProjectManager} não "conhece" a classe {@link SerializedProjectDAO}; ele conhece
 * apenas este contrato ({@code IPersistenceDAO}). Isso permite que a
 * estratégia de salvamento possa ser trocada no futuro (ex: para um Banco de Dados)
 * sem que nenhuma linha de código no {@link ProjectManager} precise ser alterada.
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */

public interface IPersistenceDAO {

    /**
     * Salva a lista completa de projetos em uma fonte de dados (ex: arquivo).
     * @param projects A {@link ArrayList} de {@link Project}s a ser salva.
     * @throws IOException Se ocorrer um erro de escrita no disco.
     * @throws Exception Se ocorrer qualquer outro erro (ex: falha na serialização).
     */
    void save(ArrayList<Project> projects) throws Exception;

    /**
     * Carrega (lê) a lista completa de projetos da fonte de dados.
     * <p>
     * Se a fonte de dados não existir (ex: primeira vez rodando),
     * retorna uma {@link ArrayList} vazia.
     * </p>
     * @return A {@link ArrayList} de {@link Project}s lida do arquivo.
     * @throws IOException Se ocorrer um erro de leitura do disco.
     * @throws ClassNotFoundException Se (no caso da serialização) a classe
     * de um objeto salvo não for encontrada.
     * @throws Exception Se ocorrer qualquer outro erro.
     */
    ArrayList<Project> load() throws Exception;
}
