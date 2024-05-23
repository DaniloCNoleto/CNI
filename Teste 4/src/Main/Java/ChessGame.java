import java.io.*;
import java.sql.*;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class DuracaoJogoXadrez {

    public static void main(String[] args) {
        // Executar script SQL para configurar a base de dados
        executarScriptSQL("Chess_Games.sql");

        // Leitura dos dados do arquivo TXT e do banco de dados MySQL
        try (Scanner scanner = new Scanner(new File("Chess_Games.txt"));
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Chess_Games", "username", "password")) {

            int iteration = 1;
            while (scanner.hasNextLine() && iteration <= 20) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(" ");
                int numPartida = Integer.parseInt(dados[0]);
                LocalTime inicio = LocalTime.parse(dados[1], DateTimeFormatter.ofPattern("HH:mm"));
                LocalTime termino = LocalTime.parse(dados[2], DateTimeFormatter.ofPattern("HH:mm"));
                Duration duracao = Duration.between(inicio, termino);
                String resultado = duracao.toHours() > 12 ? "Jogo considerado empatado" : duracao.toString();

                // Gravação do resultado no arquivo de saída
                try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado4.txt", true))) {
                    writer.printf("Iteração %d:\n", iteration);
                    writer.printf("Núm. Partida | Início | Término | Tempo Total\n");
                    writer.printf("%d | %s | %s | %s\n\n", numPartida, inicio, termino, resultado);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                iteration++;
            }

            // Leitura dos dados do banco de dados MySQL
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT num_partida, inicio, termino FROM partidas LIMIT 20")) {

                int dbIteration = 1;
                while (rs.next() && dbIteration <= 20) {
                    int numPartida = rs.getInt("num_partida");
                    LocalTime inicio = rs.getTime("inicio").toLocalTime();
                    LocalTime termino = rs.getTime("termino").toLocalTime();
                    Duration duracao = Duration.between(inicio, termino);
                    String resultado = duracao.toHours() > 12 ? "Jogo considerado empatado" : duracao.toString();

                    // Gravação do resultado no arquivo de saída
                    try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado4.txt", true))) {
                        writer.printf("Iteração Banco %d:\n", dbIteration);
                        writer.printf("Núm. Partida | Início | Término | Tempo Total\n");
                        writer.printf("%d | %s | %s | %s\n\n", numPartida, inicio, termino, resultado);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    dbIteration++;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (SQLException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void executarScriptSQL(String caminhoArquivoSQL) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306", "username", "password");
             Statement stmt = conn.createStatement()) {

            StringBuilder script = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivoSQL))) {
                String linha;
                while ((linha = br.readLine()) != null) {
                    script.append(linha).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            for (String comando : script.toString().split(";")) {
                if (!comando.trim().isEmpty()) {
                    stmt.execute(comando);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
