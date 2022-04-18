package pt.ulusofona.deisi.aed.deisiflix;

import java.util.ArrayList;

public class Filme {
    int id;
    String titulo;
    ArrayList<Pessoa> atores;
    ArrayList<Pessoa> realizadores;
    ArrayList<GeneroCinematografico> generos;
    String dataLancamento;
    int orcamento;
    float mediaVotos;
    int nrVotos;

    Filme(){}

    Filme(int id,
          String titulo,
          ArrayList<Pessoa> atores,
          ArrayList<Pessoa> realizadores,
          ArrayList<GeneroCinematografico> generos,
          String dataLancamento,
          int orcamento,
          float mediaVotos,
          int nrVotos
    ){
        this.id = id;
        this.titulo = titulo;
        this.atores = atores;
        this.realizadores = realizadores;
        this.generos = generos;
        this.dataLancamento = dataLancamento;
        this.orcamento = orcamento;
        this.mediaVotos = mediaVotos;
        this.nrVotos = nrVotos;
    }

    public String toString(){
        // Converts date from 'DD-MM-AAAA' to 'AAAA-MM-DD'
        String[] tempDate = this.dataLancamento.split("-");
        String toStringDateFormat = String.join("-", tempDate[2], tempDate[1], tempDate[0]);

        return id + " | " + titulo + " | " + toStringDateFormat + " | " + nrVotos + " | " + mediaVotos;
    }

}
