import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class TransferTest {
    Transfer t1, t2, t2copy;

    @BeforeEach
    void init()
    {
        assertDoesNotThrow( () ->
        {
            t1 = new Transfer("07.12.2022", 600, "test t1");
            t2 = new Transfer("08.12.2022", 6000, "test t2", "sender t2", "recipient p2");
            t2copy = new Transfer(t2);
        });
        t1.setSender("sender t1");
        t1.setRecipient("recipient t1");
    }

    @Test
    void constructorTest()
    {
        assertNotNull(t1);
        assertNotNull(t2);
        assertNotEquals(t1, t2);
        assertInstanceOf(Transfer.class, t1);
        assertInstanceOf(Transfer.class, t2);

        assertThrows(TransactionAttributeException.class,
                () -> {new Transfer("00.00.0000", -200, "boom");});
    }

    @Test
    void copyConstructorTest()
    {
        assertNotNull(t2copy);
        assertEquals(t2, t2copy);
        assertEquals(t2.getAmount(),t2copy.getAmount());
    }

    @Test
    void equalsTest()
    {
        assertNotEquals(t1,t2);
        assertEquals(t2,t2copy);
        assertDoesNotThrow( () -> assertNotEquals(t1, new Transfer("07.12.2022", 600, "test t1")));
    }

    @Test
    void calculateTest()
    {
        assumeTrue(t1.getAmount() > 0);
        assertEquals(t1.calculate(),t1.getAmount());

        assumeTrue(t2.getAmount() > 0);
        assertEquals(t2.calculate(), t2.getAmount());
    }

    @Test
    void toStringTest()
    {
        String rightOutput =
                        "Date: " + t2.getDate() +
                        "\nAmount: " + t2.getAmount() +
                        "\nDescription: " + t2.getDescription() +
                        "\nCalculated Amount: " + t2.calculate()+
                        "\nFrom: " + t2.getSender() +
                        "\nTo: " + t2.getRecipient() + "\n";
        assertEquals(t2.toString(),rightOutput);
    }
}
