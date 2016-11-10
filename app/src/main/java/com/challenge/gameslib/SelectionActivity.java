package com.challenge.gameslib;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/*
 * Classe: SelectionActivity
 * Descricao: Representa a atividade do aplicativo que exibe todos os jogos para que o usuario selecione um
 * deles e veja seus detalhes.
 */
public class SelectionActivity extends AppCompatActivity {

    // Elementos necessarios para a parte grafica da atividade;
    private GridView gridView; // Grade de exibicao de jogos;
    private GridViewAdapter gridAdapter; // Adaptador utilizado pela grade para exibir os jogos;
    private ProgressBar progressBar; // Barra de progresso mostrada durante o download de dados da internet;

    public static List<Game> GAMES_LIBRARY; // Lista de jogos para exibicao e selecao na atividade;

    public final static String EXTRA_POSITION = "com.challenge.gameslib.POSITION"; // Nome do extra que eh passado para a proxima atividade
    public final static String GAMES_INFO_URL = "https://dl.dropboxusercontent.com/u/34048947/games";

    /*
     * Metodo: onCreate
     * Descricao: Metodo chamado quando a atividade eh criada. Ele inicia o download dos dados dos jogos
     * e exibe a barra de progresso.
     * Parametros:
     *      "savedInstanceState" - dados utilizados em uma outra utilizacao da atividade (caso existam).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Torna a barra de progresso visivel.
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);

        // Inicia o download dos dados dos jogos de forma assincrona.
        GetURLGamesTask task = new GetURLGamesTask();
        task.execute(SelectionActivity.GAMES_INFO_URL);
    }

    /*
     * Metodo: createGrid
     * Descricao: Cria a grade de exibicao de jogos da atividade.
     */
    private void createGrid() {

        // Cria a grade de exibicao e o adaptador com os dados dos jogos para que eles possam ser exibidos na grade;
        gridView = (GridView)findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this, R.layout.grid_item_layout, SelectionActivity.GAMES_LIBRARY);
        gridView.setAdapter(gridAdapter);

        // Adiciona um listener que identifica quando o usuario clica em um item da grade;
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

                // Cria um intent colocando como extra a posicao da grade que foi clicada;
                Intent intent = new Intent(SelectionActivity.this, DetailsActivity.class);
                intent.putExtra(SelectionActivity.EXTRA_POSITION, position);

                // Inicia a atividade que mostra os detalhes do jogo clicado.
                startActivity(intent);
            }
        });
    }

    /*
     * Classe: GetURLGamesTask (aninhada)
     * Descricao: Classe que representa uma tarefa assincrona para a obtencao dos dados dos jogos via internet
     */
    class GetURLGamesTask extends AsyncTask<String, Void, List<Game>> {

        // Atributo exception que armazena uma excessao caso ela ocorra durante a execucao da tarefa.
        private Exception exception;

        /*
         * Metodo: doInBackground
         * Descricao: Metodo da classe abstrata AsyncTask que deve ser implementado contendo quais comandos devem
         * ser executados assincronamente pela tarefa.
         * Parametros:
         *      "urls" - endereco em que as informacoes dos jogos devem ser buscadas.
         * Retorno: A lista de jogos que foi obtida a partir do endereco recebido.
         */
        protected List<Game> doInBackground(String... urls) {

            try {

                // Cria a conexao HTTP com o endereco recebido e cria um leitor para obter os dados da pagina;
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET"); // Utiliza-se o metodo HTTP GET
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                // Um StringBuilder eh criado para que os dados lidos pelo leitor possam ser armazenados nele
                StringBuilder content = new StringBuilder();
                String line = reader.readLine();
                while(line != null) { // Enquanto houver conteudo que foi lido,
                    content.append(line); // Armazena no StringBuilder o conteudo lido,
                    line = reader.readLine(); // E le o proximo conteudo;
                }

                // Fecha o leitor e retorna uma lista de jogos criada a partir do conteudo JSON lido da URL.
                reader.close();
                return Game.toGamesList(content.toString());
            } catch (Exception e) { // Em caso de falha durante a execucao da tarefa,

                // Armazena a excecao ocorrida e retorna nulo.
                this.exception = e;
                return null;
            }
        }

        /*
         * Metodo: onPostExecute
         * Descricao: Metodo da classe abstrata AsyncTask que deve ser implementado. Ele define queis comandos devem ser
         * executados apos a finalizacao da tarefa assincrona. Neste caso, a lista de jogos da atividade eh atualizada
         * com a lista que foi lida da internet e a grade de exibicao dos jogos eh criada.
         */
        protected void onPostExecute(List<Game> games) {

            if(this.exception == null) { // Caso nenhuma excessao tenha ocorrido,

                SelectionActivity.GAMES_LIBRARY = games; // Atualiza a lista de jogos,
                createGrid(); // Cria a grade de exibicao dos jogos,
                progressBar.setVisibility(View.GONE); // E esconde a barra de progresso.
            }
        }
    }
}