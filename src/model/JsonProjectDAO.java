package model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * Implementação do DAO que usa JSON (com a biblioteca Gson).
 * <p>
 * Esta classe salva a {@link ArrayList} de {@link Project}s
 * em um arquivo de texto legível (ex: "dados.json").
 * </p>
 *
 * @author Manuela Skrsypcsak Kist
 * @version 1.0
 */
public class JsonProjectDAO implements IPersistenceDAO {

    private final String filename;
    private final Gson gson;

    /**
     * Construtor que recebe o nome do arquivo a ser usado.
     * @param filename O nome do arquivo (ex: "dados.json").
     */
    public JsonProjectDAO(String filename) {
        if (AppUtils.isStringNullOrEmpty(filename)) {
            throw new IllegalArgumentException("O nome do arquivo (filename) não pode ser nulo ou vazio.");
        }
        this.filename = filename;

        this.gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .setPrettyPrinting()
                .create();
    }

    /**
     * Salva a lista de projetos no arquivo .json usando {@link Gson#toJson}.
     * @param projects A {@link ArrayList} de {@link Project}s a ser salva.
     * @throws IOException Se ocorrer um erro de escrita no disco.
     */
    @Override
    public void save(ArrayList<Project> projects) throws IOException {

        try (Writer writer = new FileWriter(this.filename)) {
            gson.toJson(projects, writer);
        }
    }

    /**
     * Carrega a lista de projetos do arquivo .json usando {@link Gson#fromJson}.
     * <p>
     * Se o arquivo não for encontrado (ex: primeira vez rodando),
     * ele retorna uma nova {@link ArrayList} vazia.
     * </p>
     * @return A {@link ArrayList} de {@link Project}s lida do arquivo.
     * @throws IOException Se ocorrer um erro de leitura do disco (diferente de "não encontrado").
     */
    @Override
    public ArrayList<Project> load() throws IOException {

        try (Reader reader = new FileReader(this.filename)) {

            Type listType = new TypeToken<ArrayList<Project>>() {}.getType();

            ArrayList<Project> loadedProjects = gson.fromJson(reader, listType);

            if (loadedProjects == null) {
                return new ArrayList<>();
            }
            return loadedProjects;

        } catch (FileNotFoundException e) {
            System.out.println("Nenhum arquivo de save (" + this.filename + ") encontrado. Iniciando com lista vazia.");
            return new ArrayList<>();
        }
    }

    // --- CLASSE ANINHADA ---

    /**
     * Classe "tradutora" (TypeAdapter) aninhada e estática para {@link LocalDate}.
     * <p>
     * Esta classe é privada pois seu uso é exclusivo do {@link JsonProjectDAO}.
     * Ela ensina o Gson a converter {@code LocalDate} para uma {@code String}
     * (formato "AAAA-MM-DD") e vice-versa.
     * </p>
     */
    private static class LocalDateAdapter extends TypeAdapter<LocalDate> {

        private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

        /**
         * <p>Chamado pelo Gson para escrever (serializar) um objeto LocalDate.</p>
         * Converte o objeto LocalDate em uma String ISO ("AAAA-MM-DD").
         */
        @Override
        public void write(final JsonWriter jsonWriter, final LocalDate localDate) throws IOException {
            if (localDate == null) {
                jsonWriter.nullValue();
            } else {
                jsonWriter.value(localDate.format(formatter));
            }
        }

        /**
         * <p>Chamado pelo Gson para ler (deserializar) uma String em um LocalDate.</p>
         * Converte a String ISO ("AAAA-MM-DD") de volta em um objeto LocalDate.
         */
        @Override
        public LocalDate read(final JsonReader jsonReader) throws IOException {
            if (jsonReader.peek() == com.google.gson.stream.JsonToken.NULL) {
                jsonReader.nextNull();
                return null;
            } else {
                String dateStr = jsonReader.nextString();
                return LocalDate.parse(dateStr, formatter);
            }
        }
    }

}
