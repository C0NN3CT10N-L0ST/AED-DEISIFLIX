import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Reader {
    static boolean DEBUG = true;
    static String testDBPath = new File("").getAbsolutePath() + "\\src\\pt\\ulusofona\\deisi\\aed\\deisiflix\\testDB\\";

    public static void movieReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_movies.txt");
        BufferedReader reader = new BufferedReader(fr);

        String linha = null;

        while ((linha = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + linha);
            }

            String dados[] = linha.split(",");

            if (dados.length == 5) {
                int id = Integer.parseInt(dados[0].strip());
                String titulo = dados[1].strip();
                int duracao = Integer.parseInt(dados[2].strip());
                int orcamento = Integer.parseInt(dados[3].strip());
                String data = dados[4].strip();

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Title: "+ titulo);
                    System.out.println("Duration: " + duracao);
                    System.out.println("Budget " + orcamento);
                    System.out.println("Date: " + data);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static void movieVotesReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_movie_votes.txt");
        BufferedReader reader = new BufferedReader(fr);

        String linha = null;

        while ((linha = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + linha);
            }

            String dados[] = linha.split(",");

            if (dados.length == 3) {
                int id = Integer.parseInt(dados[0].strip());
                float mediaVotos = Float.parseFloat(dados[1].strip());
                int totalVotos = Integer.parseInt(dados[2].strip());

                if (DEBUG) {
                    System.out.println("ID: " + id);
                    System.out.println("Vote Average: "+ mediaVotos);
                    System.out.println("Nr. Votes: " + totalVotos);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static void peopleReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_actors.txt");
        BufferedReader reader = new BufferedReader(fr);

        String linha = null;

        while ((linha = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + linha);
            }

            String dados[] = linha.split(",");

            if (dados.length == 5) {
                String tipo = dados[0].strip();
                int idPessoa = Integer.parseInt(dados[1].strip());
                String nome = dados[2].strip();
                String genero = dados[3].strip();
                int idFilme = Integer.parseInt(dados[4].strip());

                if (DEBUG) {
                    System.out.println("Tipe Person: " + tipo);
                    System.out.println("ID: " + idPessoa);
                    System.out.println("Name: "+ nome);
                    System.out.println("Genre: " + genero);
                    System.out.println("ID Movie: " + idFilme);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }

    public static void genresReader() throws IOException {
        FileReader fr = new FileReader(testDBPath + "deisi_genres.txt");
        BufferedReader reader = new BufferedReader(fr);

        String linha = null;

        while ((linha = reader.readLine()) != null) {
            if (DEBUG) {
                System.out.println("Line -> " + linha);
            }

            String dados[] = linha.split(",");

            if (dados.length == 2) {
                String genero = dados[0].strip();
                int id = Integer.parseInt(dados[1].strip());

                if (DEBUG) {
                    System.out.println("Genre Name: " + genero);
                    System.out.println("ID Movie: " + id);
                    System.out.println("-----------".repeat(6));
                }
            }

            // TODO: Add to class
        }

        reader.close();
    }
}
