package com.challenge.gameslib;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerFragment;


/*
 * Classe: DetailsActivity
 * Descricao: Representa a atividade do aplicativo que exibe todos detalhes de um jogo selecionado pelo usuario.
 */
public class DetailsActivity extends AppCompatActivity implements YouTubePlayer.OnInitializedListener{

    private Game game; // Jogo cujos detalhes serao exibidos;
    public static final String YOUTUBE_API_KEY = "AIzaSyBh4HrdH20g9rcK-4y3GEu1ZnFk_WmTF1c"; // Chave para utilizacao da API do YouTube.

    /*
     * Metodo: onCreate
     * Descricao: Metodo chamado quando a atividade eh criada. Ele atualiza todos os elementos graficos com as informacoes do
     * jogo e tambem inicializa o player do YouTube.
     * Parametros:
     *      "savedInstanceState" - dados utilizados em uma outra utilizacao da atividade (caso existam).
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        /*
         * Pega o valor do extra recebido via intent, que eh a posicao da lista de jogos em que o jogo cujos detalhes
         * devem ser exibidos se encontra.
         */
        Intent intent = getIntent();
        int position = intent.getIntExtra(SelectionActivity.EXTRA_POSITION, -1);
        this.game = SelectionActivity.GAMES_LIBRARY.get(position);

        // Preenche os elementos graficos com as informacoes do jogo;
        ImageView image = (ImageView)findViewById(R.id.image);
        image.setImageBitmap(this.game.getImage());

        TextView name = (TextView)findViewById(R.id.name);
        name.setText(this.game.getName());

        TextView releaseDate = (TextView)findViewById(R.id.release_date);
        releaseDate.setText(this.game.getReleaseDate());

        TextView platforms = (TextView)findViewById(R.id.platforms);
        platforms.setText(this.game.getPlatforms());

        // Altera o titulo exibido na action bar para o nome do jogo e habilita o botao de voltar;
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(this.game.getName());

        // Inicaliza o player de video do YouTube.
        YouTubePlayerFragment playerFragment = (YouTubePlayerFragment)getFragmentManager().findFragmentById(R.id.player);
        playerFragment.initialize(DetailsActivity.YOUTUBE_API_KEY, this);
    }

    /*
     * Metodo: onOptionsItemSelected
     * Descricao: Metodo chamado quando a algum item do menu de opcoes eh selecionado. Neste caso, ele verifica se item selecionado
     * eh o botao de voltar da action bar e fecha esta atividade, voltando a atividade de selecao.
     * Parametros:
     *      "item" - o item que foi selecionado.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Se o id do item selecionado for o botao voltar,
        if(item.getItemId() == android.R.id.home) {

            // Encerra esta atividade e retorna true, indicando que o processamento do evento ja foi realizado.
            this.finish();
            return true;
        }

        // Se nao, permite que o processamento normal continue.
        return super.onOptionsItemSelected(item);
    }

    /*
     * Metodo: onInitializationSuccess
     * Descricao: Metodo definido pela interface OnInitializedListener (do YouTubePlayer) que eh chamado quando
     * a inicializacao do player eh realizada com sucesso.
     * Parametros:
     *      "provider" - o provedor usado para inicializar o player;
     *      "player" - o player que foi inicializado e pode ser usado para controlar a reproducao do video;
     *      "wasRestored" - flag que indica se o player foi restaurado de um estado anteriomente salvo.
     */
    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {

        // Se esta o player esta sendo inicalizado e nao restaurado,
        if(!wasRestored) {

            /*
             * Define o estido do player como MINIMAL para que apenas os controles de play/pause estejam disponiveis,
             * tornando a interface mais limpa. E, tambem, carrega o video do trailer do jogo no player.
             */
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.MINIMAL);
            player.cueVideo(this.game.getTrailer().substring(this.game.getTrailer().length() - 11));
        }
    }

    /*
     * Metodo: onInitializationFailure
     * Descricao: Metodo definido pela interface OnInitializedListener (do YouTubePlayer) que eh chamado quando ocorre
     * uma falha durante a inicializacao do player.
     * Parametros:
     *      "provider" - o provedor que nao conseguiu inicializar o player;
     *      "errorReason" - o motivo da falha;
     */
    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult errorReason) {

        if(errorReason.isUserRecoverableError()) { // Se for um erro recuperavel,

            // Cria e exibe uma uam caixa de dialogo com a mensagem de erro.
            errorReason.getErrorDialog(this, 1).show();
        }
        else { // Se nao,

            // Exibe um toast com a mensagem de erro para o usuario.
            String error = String.format(getString(R.string.youtube_player_error), errorReason.toString());
            Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        }
    }
}

