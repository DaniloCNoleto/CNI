import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class TrocaVariaveis {

    public static void main(String[] args) {
        int A = 0, B = 0;

        // Executar script SQL para configurar a base de dados
        executarScriptSQL("Swap_Examples.sql");

        // Leitura do valor de A do arquivo
        try (Scanner scanner = new Scanner(new File("Swap_Examples.txt"))) {
            if (scanner.hasNextInt()) {
                A = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Leitura do valor de B do banco de dados MySQL
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Swap_Examples", "username", "password")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT valor FROM variaveis WHERE variavel = 'B'");
            if (rs.next()) {
                B = rs.getInt("valor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Troca dos valores
        int temp = A;
        A = B;
        B = temp;

        // Gravação do resultado no arquivo de saída
        try (PrintWriter writer = new PrintWriter(new File("Resultado1.txt"))) {
            writer.printf("Valor Origem de A | Valor Origem de B | Valor Final de A | Valor Final de B\n");
            writer.printf("%d | %d | %d | %d\n", temp, B, A, temp);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Atualização do banco de dados com os novos valores
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Swap_Examples", "username", "password")) {
            PreparedStatement pstmt = conn.prepareStatement("UPDATE variaveis SET valor = ? WHERE variavel = ?");
            pstmt.setInt(1, A);
            pstmt.setString(2, "A");
            pstmt.executeUpdate();

            pstmt.setInt(1, B);
            pstmt.setString(2, "B");
            pstmt.executeUpdate();
        } catch (SQLException e) {
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
