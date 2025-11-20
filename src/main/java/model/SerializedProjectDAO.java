package model;

import java.io.*;
import java.util.ArrayList;

/**
 * Implementação do DAO que utiliza a Serialização Nativa de Objetos do Java.
 * <p>
 * Esta classe é responsável por persistir (salvar) e recuperar a lista de projetos
 * diretamente em um arquivo binário no disco.
 * </p>
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class SerializedProjectDAO implements IPersistenceDAO {

    private final String filename;

    /**
     * Construtor da classe.
     * @param filename O caminho ou nome do arquivo a ser utilizado para salvar os dados.
     * Não pode ser nulo ou vazio.
     */
    public SerializedProjectDAO(String filename) {
        this.filename = filename;
    }

    /**
     * Salva a lista completa de projetos no arquivo especificado.
     * <p>
     * Este método utiliza o mecanismo de serialização do Java para converter
     * a lista de objetos (e todos os objetos contidos nela) em uma sequência
     * de bytes e gravá-los no arquivo.
     * </p>
     * @param projects A lista de {@link Project}s a ser persistida.
     * @throws IOException Se ocorrer qualquer erro de entrada/saída (ex: disco cheio, sem permissão).
     */
    @Override
    public void save(ArrayList<Project> projects) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(filename);

             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
             oos.writeObject(projects);
        }
    }

    /**
     * Carrega a lista de projetos do arquivo especificado.
     * <p>
     * Este método lê o arquivo binário, desserializa os bytes de volta para
     * objetos Java e retorna a lista original.
     * </p>
     * <p>
     * Se o arquivo não for encontrado (o que é comum na primeira execução do programa),
     * o método captura a exceção {@link FileNotFoundException} e retorna uma nova lista vazia,
     * permitindo que o programa inicie sem erros.
     * </p>
     * @return A {@link ArrayList} de {@link Project}s recuperada do arquivo,
     * ou uma lista vazia se o arquivo não existir.
     * @throws IOException Se ocorrer um erro geral de leitura.
     * @throws ClassNotFoundException Se a classe dos objetos salvos não for encontrada
     * (ex: se o código foi alterado drasticamente e é incompatível).
     */
    @Override
    public ArrayList<Project> load() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(filename);
             ObjectInputStream ois = new ObjectInputStream(fis)) {

            return (ArrayList<Project>) ois.readObject();

        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }
    }
}