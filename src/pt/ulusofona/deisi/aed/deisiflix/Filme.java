package pt.ulusofona.deisi.aed.deisiflix;

public class Filme {

    int id;
    String titulo;
    Filme[] atores;
    Filme[] realizadores;
    Filme[] generos;
    String dataLancamento;
    int orcamento;
    int mediaVotos;
    int nrVotos;

    Filme(){}

    Filme(int id, String titulo, Filme[] atores, Filme[] realizadores, Filme[] generos, String dataLancamento, int orcamento, int mediaVotos, int nrVotos){
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
        return id + " | " + titulo + " | " + dataLancamento + " | " + nrVotos + " | " + mediaVotos;
    }

}
