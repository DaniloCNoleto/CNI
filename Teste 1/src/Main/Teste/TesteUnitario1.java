import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class TrocaVariaveisTest {

    @Test
    public void testTrocaValores() {
        int A = 52;
        int B = 58;

        // Troca dos valores
        int temp = A;
        A = B;
        B = temp;

        assertEquals(58, A);
        assertEquals(52, B);
    }

    @Test
    public void testLeituraArquivo() {
        int A = 0;
        try (Scanner scanner = new Scanner(new File("Swap_Examples.txt"))) {
            if (scanner.hasNextInt()) {
                A = scanner.nextInt();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        assertEquals(52, A); // Assume-se que o arquivo valores.txt contém o valor 52
    }

    @Test
    public void testLeituraBancoDados() {
        int B = 0;
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Swap_Examples", "username", "password")) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT valor FROM variaveis WHERE variavel = 'B'");
            if (rs.next()) {
                B = rs.getInt("valor");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        assertEquals(58, B); // Assume-se que o banco de dados contém o valor 58 para a variável B
    }
}
