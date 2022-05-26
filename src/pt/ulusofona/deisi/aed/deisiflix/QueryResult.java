package pt.ulusofona.deisi.aed.deisiflix;

public class QueryResult {
    String valor;   // Valor calculado pela query
    long tempo;     // Tempo que a query demorou a executar (ms)

    QueryResult() {}

    QueryResult(String valor, long tempo) {
        this.valor = valor;
        this.tempo = tempo;
    }
}
