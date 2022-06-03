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

        // Defines 'toString' variables
        int genresSize = this.generos != null ? this.generos.size() : 0;
        int directorsSize = this.realizadores != null ? this.realizadores.size() : 0;
        int actorsSize = 0;
        int actrissesSize = 0;

        // Counts number of actors and actresses
        if (this.atores != null) {
            for (Pessoa actor : this.atores) {
                if (actor.genero == 'M') {
                    actorsSize++;
                } else if (actor.genero == 'F') {
                    actrissesSize++;
                }
            }
        }

        // Returns final string
        return id +
                " | " + titulo +
                " | " + toStringDateFormat +
                " | " + nrVotos +
                " | " + mediaVotos +
                " | " + genresSize +
                " | " + directorsSize +
                " | " + actorsSize +
                " | " + actrissesSize;
    }

}
