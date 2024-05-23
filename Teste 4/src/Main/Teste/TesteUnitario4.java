import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class DuracaoJogoXadrezTest {

    @Test
    public void testCalculoDuracaoJogo() {
        String inicio = "20:07";
        String termino = "20:32";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime inicioTime = LocalTime.parse(inicio, formatter);
        LocalTime terminoTime = LocalTime.parse(termino, formatter);
        Duration duration = Duration.between(inicioTime, terminoTime);

        String resultadoEsperado = duration.toHours() > 12 ? "Jogo considerado empatado" : duration.toString();

        assertEquals("PT2H55M", duration.toString());
        assertEquals("PT2H55M", resultadoEsperado);
    }

    @Test
    public void testCalculoDuracaoJogoEmpate() {
        String inicio = "23:17";
        String termino = "03:27";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalTime inicioTime = LocalTime.parse(inicio, formatter);
        LocalTime terminoTime = LocalTime.parse(termino, formatter);
        Duration duration = Duration.between(inicioTime, terminoTime);

        String resultadoEsperado = duration.toHours() > 12 ? "Jogo considerado empatado" : duration.toString();

        assertEquals("PT14H", duration.toString());
        assertEquals("Jogo considerado empatado", resultadoEsperado);
    }
}
