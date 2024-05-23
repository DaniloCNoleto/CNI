import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

public class CalculoComissaoTest {

    @Test
    public void testCalculoComissao() {
        String nome = "vendedor 1";
        int qtdItens = 48;
        double valorVendas = 1028.27;
        double salarioFixo = 1200.00;
        double comissaoEsperada = qtdItens * 50.00 + valorVendas * 0.05;
        double salarioFinalEsperado = salarioFixo + comissaoEsperada;

        assertEquals(750.00, comissaoEsperada);
        assertEquals(1950.00, salarioFinalEsperado);
    }
}
