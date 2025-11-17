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
                .registerTypeAdapter(Task.class, new TaskAdapter())
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

    // --- CLASSES ANINHADAS ---

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

    /**
     * Classe "tradutora" (TypeAdapter) que ensina o Gson a lidar
     * com a hierarquia polimórfica (Herança) da classe abstrata Task.
     * <p>
     * Baseado na solução de "RuntimeTypeAdapterFactory".
     * </p>
     */
    private static class TaskAdapter implements com.google.gson.JsonSerializer<Task>, com.google.gson.JsonDeserializer<Task> {

        private static final String CLASS_META_KEY = "TIPO_DA_CLASSE";

        /**
         * {@inheritDoc}
         * <p>Chamado pelo Gson para ESCREVER (serializar) um objeto Task.</p>
         * Ele delega a serialização ao Gson e adiciona um campo de "tipo".
         */
        @Override
        public com.google.gson.JsonElement serialize(Task src, java.lang.reflect.Type typeOfSrc,
                                                     com.google.gson.JsonSerializationContext context) {

            com.google.gson.JsonElement jsonElement = context.serialize(src, src.getClass());

            jsonElement.getAsJsonObject().addProperty(CLASS_META_KEY, src.getClass().getName());

            return jsonElement;
        }

        /**
         * {@inheritDoc}
         * <p>Chamado pelo Gson para LER (deserializar) um objeto Task.</p>
         * Ele lê o campo "tipo" e usa a classe correta para carregar.
         */
        @Override
        public Task deserialize(com.google.gson.JsonElement json, java.lang.reflect.Type typeOfT,
                                com.google.gson.JsonDeserializationContext context)
                throws com.google.gson.JsonParseException {

            com.google.gson.JsonObject jsonObject = json.getAsJsonObject();

            com.google.gson.JsonPrimitive prim = (com.google.gson.JsonPrimitive) jsonObject.get(CLASS_META_KEY);
            String className = prim.getAsString();

            Class<?> clazz;
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new com.google.gson.JsonParseException(e);
            }

            return context.deserialize(json, clazz);
        }
    }

}
