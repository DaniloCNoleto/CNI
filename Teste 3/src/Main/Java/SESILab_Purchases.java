import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class PrecoCamisas {

    public static void main(String[] args) {
        // Executar script SQL para configurar a base de dados
        executarScriptSQL("SESILab_Purchases.sql");

        // Leitura dos dados do arquivo TXT e do banco de dados MySQL
        try (Scanner scanner = new Scanner(new File("SESILab_Purchases.txt"));
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/SESILab_Purchases", "username", "password")) {

            int iteration = 1;
            while (scanner.hasNextLine() && iteration <= 20) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(" ");
                String nomeCliente = dados[0];
                int qtdComprada = Integer.parseInt(dados[1]);
                double valorAplicado = qtdComprada < 12 ? 1083.00 : 1000.00;
                double valorTotal = qtdComprada * valorAplicado;

                // Gravação do resultado no arquivo de saída
                try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado3.txt", true))) {
                    writer.printf("Iteração %d:\n", iteration);
                    writer.printf("Nome do Cliente | Qtd Comprada | Valor Aplicado | Valor Total\n");
                    writer.printf("%s | %d | %.2f | %.2f\n\n", nomeCliente, qtdComprada, valorAplicado, valorTotal);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                iteration++;
            }

            // Leitura dos dados do banco de dados MySQL
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT nome_cliente, qtd_comprada FROM camisas LIMIT 20")) {

                int dbIteration = 1;
                while (rs.next() && dbIteration <= 20) {
                    String nomeCliente = rs.getString("nome_cliente");
                    int qtdComprada = rs.getInt("qtd_comprada");
                    double valorAplicado = qtdComprada < 12 ? 1083.00 : 1000.00;
                    double valorTotal = qtdComprada * valorAplicado;

                    // Gravação do resultado no arquivo de saída
                    try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado3.txt", true))) {
                        writer.printf("Iteração Banco %d:\n", dbIteration);
                        writer.printf("Nome do Cliente | Qtd Comprada | Valor Aplicado | Valor Total\n");
                        writer.printf("%s | %d | %.2f | %.2f\n\n", nomeCliente, qtdComprada, valorAplicado, valorTotal);
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
