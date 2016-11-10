package com.challenge.gameslib;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/*
 * Classe: GridViewAdapter
 * Descricao: Classe que representa um adaptador de conteudo a ser utilizado pelo GridView existente
 * na atividade principal, para que a imagem a nome dos jogos possam ser exibidos como item da grade.
 */
public class GridViewAdapter extends ArrayAdapter {

    // Declaracao dos atributos privados necessarios para o adaptador.
    private Context context;
    private int resource;
    private List<Game> games = new ArrayList<Game>();

    /*
     * Construtor
     * Descricao: Construtor publico da classe que inicializa os atributos da classe
     * Parametros:
     *      "context" - contexto atual da aplicacao;
     *      "resource" - o ID do recurso que referencia o arquivo de layout a ser utilizado para cada item da grade;
     *      "games" - lista de jogos que deverao ser adaptados e exibidos na grade.
     */
    public GridViewAdapter(Context context, int resource, List<Game> games) {

        super(context, resource, games); // Chama o construtor da classe pai.

        // Armazena nos atributos da classe todos os parametros recebidos.
        this.resource = resource;
        this.context = context;
        this.games = games;
    }

    /*
     * Metodo: getView
     * Descricao: Gera e retorna uma view que mostra a imagem e nome do jogo na posicao "position" da lista de jogos.
     * Parametros:
     *      "position" - posicao na lista do jogo cuja view deve ser gerada;
     *      "convertView" - a visao antiga para ser reutilizada, se possivel;
     *      "parent" - o elemento pai ao qual esta visao sera eventualmente acoplada.
     * Retorno: A view gerada a partir dos dados do jogo da lista desejado.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Atribui a variavel view a view antiga recebida (ou nao) via parametro e cria uma variavel holder.
        View view = convertView;
        ViewHolder holder = null;

        if(view == null) { // Caso nao exista uma view antiga para ser reutilizada;

            // Cria uma view atraves do LayoutInflater;
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            view = inflater.inflate(resource, parent, false);

            // Cria um holder e adiciona as views correspondentes a imagem e ao nome do jogo a ele;
            holder = new ViewHolder();
            holder.text = (TextView)view.findViewById(R.id.text);
            holder.image = (ImageView)view.findViewById(R.id.image);

            // Seta o conteudo da view criada como o holder que acaba de ser criado.
            view.setTag(holder);
        }
        else { // Caso exista uma view antiga a ser reutilizada;

            // Pega o holder ja existente na view;
            holder = (ViewHolder)view.getTag();
        }

        // Preenche o conteudo das views que compoem o holder com o conteudo correspondente do jogo desejado.
        Game item = games.get(position);
        holder.text.setText(item.getName());
        holder.image.setImageBitmap(item.getImage());

        return view; // Retorna a visao gerada.
    }

    /*
     * Classe: ViewHolder (aninhada)
     * Descricao: Classe que representa o conteudo da view que eh utilizada para exibir o conteudo
     * de cada item da grade.
     */
    static class ViewHolder {
        TextView text; // Nome do jogo;
        ImageView image; // Imagem do jogo;
    }
}