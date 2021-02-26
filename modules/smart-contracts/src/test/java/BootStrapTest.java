import edu.emory.viseu.contracts.BootStrap;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;


public class BootStrapTest {

    @Test
    public void testOutput() throws IOException {
        BootStrap x = new BootStrap("/scripts/startup.sh");
        assertEquals(0, x.callStartUpScript());



    }



}
