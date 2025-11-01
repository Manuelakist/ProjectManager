package model;

import java.io.*;
import java.util.ArrayList;

/**
 * Implementação do DAO que usa Serialização de Objetos Java.
 * <p>
 * Esta classe salva a {@link ArrayList} de {@link Project}s inteira
 * diretamente em um arquivo binário (ex: "dados.dat").
 * </p>
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class SerializedProjectDAO implements IPersistenceDAO {

    private final String filename;

    /**
     * Construtor que recebe o nome do arquivo a ser usado.
     * @param filename O nome do arquivo (ex: "dados.dat").
     */
    public SerializedProjectDAO(String filename) {
        if (AppUtils.isStringNullOrEmpty(filename)) {
            throw new IllegalArgumentException("O nome do arquivo (filename) não pode ser nulo ou vazio.");
        }
        this.filename = filename;
    }

    /**
     * Salva a lista de projetos no arquivo binário usando {@link ObjectOutputStream}.
     * @param projects A {@link ArrayList} de {@link Project}s a ser salva.
     * @throws IOException Se ocorrer um erro de escrita no disco.
     */
    @Override
    public void save(ArrayList<Project> projects) throws IOException {

        try (FileOutputStream fileOut = new FileOutputStream(this.filename);
             ObjectOutputStream objOut = new ObjectOutputStream(fileOut)) {

            objOut.writeObject(projects);
        }
    }

    /**
     * Carrega a lista de projetos do arquivo binário usando {@link ObjectInputStream}.
     * <p>
     * Se o arquivo não for encontrado (ex: primeira vez rodando),
     * ele retorna uma nova {@link ArrayList} vazia.
     * </p>
     * @return A {@link ArrayList} de {@link Project}s lida do arquivo.
     * @throws IOException Se ocorrer um erro de leitura do disco (diferente de "não encontrado").
     * @throws ClassNotFoundException Se os dados no arquivo forem de uma classe desconhecida.
     */
    @Override
    public ArrayList<Project> load() throws IOException, ClassNotFoundException {

        try (FileInputStream fileIn = new FileInputStream(this.filename);
             ObjectInputStream objIn = new ObjectInputStream(fileIn)) {

            Object rawObject = objIn.readObject();

            if (rawObject instanceof ArrayList) {
                return (ArrayList<Project>) rawObject;
            } else {
                throw new IOException("Tipo de dado inesperado no arquivo de save.");
            }

        } catch (FileNotFoundException e) {
            System.out.println("Nenhum arquivo de save (" + this.filename + ") encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        }
    }
}
