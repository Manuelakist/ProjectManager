package model;

import java.util.ArrayList;
import java.io.IOException;

/**
 * Interface para a camada de Persistência de Dados (DAO).
 * <p>
 * Descreve os métodos que qualquer classe de persistência (como
 * SerializedProjectDAO ou JsonProjectDAO) deve obrigatoriamente implementar.
 * </p>
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public interface IPersistenceDAO {

    /**
     * Salva a lista completa de projetos em uma fonte de dados (ex: arquivo).
     * @param projects A {@link ArrayList} de {@link Project}s a ser salva.
     * @throws IOException Se ocorrer um erro de escrita no disco.
     * @throws Exception Se ocorrer qualquer outro erro (ex: falha na serialização/JSON).
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
     * @throws Exception Se ocorrer qualquer outro erro (ex: JSON mal formatado).
     */
    ArrayList<Project> load() throws Exception;
}
