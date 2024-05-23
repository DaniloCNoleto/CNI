import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class CalculoComissao {

    public static void main(String[] args) {
        // Executar script SQL para configurar a base de dados
        executarScriptSQL("Vendedores_Data.sql");

        // Leitura dos dados do arquivo TXT e do banco de dados MySQL
        try (Scanner scanner = new Scanner(new File("Vendedores_Data.txt"));
             Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Vendedores_Data", "username", "password")) {

            int iteration = 1;
            while (scanner.hasNextLine() && iteration <= 20) {
                String linha = scanner.nextLine();
                String[] dados = linha.split(" ");
                String nome = dados[0];
                int qtdItens = Integer.parseInt(dados[1]);
                double valorVendas = Double.parseDouble(dados[2]);
                double salarioFixo = Double.parseDouble(dados[3]);

                // Calcular comissão e salário final
                double comissao = qtdItens * 50.00 + valorVendas * 0.05;
                double salarioFinal = salarioFixo + comissao;

                // Gravação do resultado no arquivo de saída
                try (PrintWriter writer = new PrintWriter(new FileWriter("Resultado2.txt", true))) {
                    writer.printf("Iteração %d:\n", iteration);
                    writer.printf("Vendedor | Qtd Itens | Valor Vendido | Valor Comissão | Salário | Salário Final\n");
                    writer.printf("%s | %d | %.2f | %.2f | %.2f | %.2f\n\n", nome, qtdItens, valorVendas, comissao, salarioFixo, salarioFinal);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                iteration++;
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
