package com.challenge.gameslib;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Classe: Game
 * Descricao: Classe que representa um jogo, contendo todos os seus atributos.
 */
public class Game implements Comparable<Game> {

    // Declaração de todos os atributos de um jogo
    private String name;
    private Bitmap image;
    private String releaseDate;
    private String trailer;
    private List<String> platforms;

    /*
     * Construtor
     * Descricao: Contrutor publico da classe que recebe um objeto JSON e extrai dele todos
     * os atributos do jogo.
     * Parametros:
     *      "json" - objeto JSON que contem as informacoes sobre o jogo.
     */
    public Game(JSONObject json) throws JSONException {

        // Extracao do nome, data de lancamento e link do trailer do jogo.
        this.name = json.getString("name");
        this.releaseDate = json.getString("release_date");
        this.trailer = json.getString("trailer");

        // Baixa a imagem do jogo a partir do link contido no objeto JSON;
        try {
            URL imageUrl = new URL(json.getString("image"));
            this.image = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
        }
        catch(Exception exception) { // Em caso de falha,
            this.image = null; // Atribui valor nulo a imagem.
        }

        // Extrai todas as plataformas em que o jogo esta disponível do objeto JSON.
        this.platforms = new ArrayList<String>();
        JSONArray platformsJson = json.getJSONArray("platforms");
        for(int j = 0; j < platformsJson.length(); j++) {
            this.platforms.add(platformsJson.getString(j));
        }
        Collections.sort(this.platforms); // Ordena as plataformas por ordem alfabética.
    }

    /*
     * Metodo: compareTo
     * Descricao: implementa como deve ser realizada a comparacao de um objeto Game com outro objeto Game.
     * A comparacao eh feita entre os nomes dos jogos. Deve ser implementada para que a classe Game implemente
     * a interface Comparable e seja ordenavel pelo metodo sort da Collections.
     * Parametros:
     *      "game" - objeto com que este objeto deve ser comparado.
     * Retorno: 0 caso os jogos sejam equivalentes, mais que 0 se jogo recebido por argumento deve vir antes
     * na ordenacao, ou menos que 0 se o jogo recebido por argumento deve vir depois na ordenacao.
     */
    public int compareTo(Game game) {
        return name.compareTo(game.name);
    }

    /*
     * Getters publicos para todos os atributos da classe. O getter getPlatforms eh o unico que
     * realiza processamento sobre os dados contidos na classe: ele cria e retorna uma String que
     * contem todos os nomes completos das plataformas armazenadas na lista de plataformas do jogo.
     *
     */
    public String getName() {
        return this.name;
    }
    public Bitmap getImage() {
        return this.image;
    }
    public String getReleaseDate() {
        return this.releaseDate;
    }
    public String getTrailer() {
        return this.trailer;
    }
    public String getPlatforms() {

        // Se nao houver plataformas armazenadas,
        if(this.platforms == null || this.platforms.size() == 0) {
            return null; // Retorna uma String nula.
        }

        /*
         * Cria a String que sera retornada, obtendo os nomes completos das plataformas e separando-os
         * por virgulas e por um "e" entre os dois ultimos, caso exista mais de uma plataforma.
         */
        StringBuilder platforms = new StringBuilder();
        platforms.append(Game.getPlatformName(this.platforms.get(0)));
        for(int i = 1; i < this.platforms.size() - 1; i++) {
            platforms.append(", ");
            platforms.append(Game.getPlatformName(this.platforms.get(i)));
        }
        if(this.platforms.size() > 1) {
            platforms.append(" e ");
        }
        platforms.append(Game.getPlatformName(this.platforms.get(this.platforms.size() - 1)));

        return platforms.toString(); // Retorna a String resultante
    }

    /*
     * Metodo: getPlatformName (estatico)
     * Descricao: Metodo que mapeia uma abreviacao de uma plataforma em seu nome completo.
     * Parametros:
     *      "abbreviation" - abreviacao que deve ser mapeada.
     * Retorno: O nome completo da plataforma.
     */
    public static String getPlatformName(String abbreviation) {
        String name;

        switch(abbreviation) {
            case "X360":
                name = "Xbox 360";
                break;
            case "PS3":
                name = "PlayStation 3";
                break;
            case "PC":
                name = "PC";
                break;
            case "PS4":
                name = "PlayStation 4";
                break;
            case "XONE":
                name = "Xbox One";
                break;
            case "NS":
                name = "Nintendo Switch";
                break;
            case "MAC":
                name = "Mac OS";
                break;
            case "LNX":
                name = "Linux";
                break;
            case "Android":
                name = "Android";
                break;
            case "iOS":
                name = "iOS";
                break;
            default:
                name = abbreviation;
        }

        return name;
    }

    /*
     * Metodo: toGamesList (estatico)
     * Descricao: Metodo que transforma uma string que representa objeto JSON contendo varios
     * jogos em uma lista de objetos Game.
     * Parametros:
     *      "jsonText" - a String em formato JSON a ser convertida em lista de jogos.
     * Retorno: Uma lista contendo todos os jogos da String em formato JSON.
     */
    public static List<Game> toGamesList(String jsonText) {

        List<Game> library = new ArrayList<Game>(); // Cria uma lista de jogos vazia.

        // Processa a String JSON extraindo as informacoes de todos os jogos nela contidas.
        try {
            JSONObject json = new JSONObject(jsonText); // Cria um objeto JSON a partir da String em formato JSON;
            JSONArray games = json.getJSONArray("games"); // Extrai o array com nome "games";

            // Para cada jogo contido no array,
            for(int i = 0; i < games.length(); i++) {

                JSONObject item = (JSONObject)games.get(i); // Extrai o objeto JSON que representa o jogo;
                Game game = new Game(item); // Cria um objeto Game a partir dele;
                library.add(game); // E o adiciona na lista.
            }
            Collections.sort(library); // Ordena os jogos por ordem afabetica.
        }
        catch(JSONException exception) { // Em caso de falha no processamento do JSON,
            library = null; // O retorno sera nulo.
        }

        return library; // Retorna a lista gerada.
    }
}
