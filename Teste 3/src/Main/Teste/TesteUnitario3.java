import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class PrecoCamisasTest {

    @Test
    public void testCalculoPrecoCamisas() {
        String nomeCliente = "Customer 1";
        int qtdComprada = 7;
        double valorAplicado = qtdComprada < 12 ? 1083.00 : 1000.00;
        double valorTotalEsperado = qtdComprada * valorAplicado;

        assertEquals(1083.00, valorAplicado);
        assertEquals(10830.00, valorTotalEsperado);
    }

    @Test
    public void testCalculoPrecoCamisasDesconto() {
        String nomeCliente = "Customer 2";
        int qtdComprada = 15;
        double valorAplicado = qtdComprada < 12 ? 1083.00 : 1000.00;
        double valorTotalEsperado = qtdComprada * valorAplicado;

        assertEquals(1000.00, valorAplicado);
        assertEquals(15000.00, valorTotalEsperado);
    }
}
