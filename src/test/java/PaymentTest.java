import bank.Payment;
import bank.Transfer;
import bank.exceptions.TransactionAttributeException;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class PaymentTest {

    Payment p1, p2, p1copy,p2copy;

    @BeforeEach
    void init(){
        try{ // assertDoesNotThrow better
            p1 = new Payment("06.12.2022", -100, "test p1");
            p2 = new Payment("07.12.2022", 300, "test p2", 0.2, 0.16);
            p2copy = new Payment(p2);
            p1.setIncomingInterest(0.26);
            p1.setOutgoingInterest(0.18);
            p1copy = new Payment(p1);
        }
        catch(TransactionAttributeException ex){System.out.println(ex.getClass() + " | " + ex.getMessage());}
    }

    @Test
    void constructorTest()
    {
        assertNotNull(p1);
        assertNotNull(p2);
        assertNotEquals(p1, p2);
        assertInstanceOf(Payment.class, p1);
        assertInstanceOf(Payment.class, p2);
        assertEquals(-100, p1.getAmount());
        assertEquals(300, p2.getAmount());

        assertThrows(TransactionAttributeException.class,
                () -> {Payment pEx = new Payment("00.00.0000", 12, "boom", -0.3, 0.3);});
        assertThrows(TransactionAttributeException.class,
                () -> {Payment pEx1 = new Payment("00.00.0000", 12, "boom", 0.3, -0.3);});
    }

    @Test
    void copyConstructorTest()
    {
        assertNotNull(p2copy);
        assertEquals(p2, p2copy);
        assertEquals(p2copy.getAmount(), p2.getAmount());
        assertNotSame(p2, p2copy); //Referenzen testen
    }

    @Test
    void calculateTest()
    {
        assumeTrue(p1.getAmount() < 0);
        assertEquals((1+p1.getOutgoingInterest())*p1.getAmount(), p1.calculate());
        assertEquals(p1copy.calculate(), p1.calculate());

        assumeTrue(p2.getAmount() > 0);
        assertEquals((1-p2.getIncomingInterest())*p2.getAmount(), p2.calculate());
        assertEquals(p2copy.calculate(), p2.calculate());

        assertDoesNotThrow(() -> p2.setAmount(-300));
        assertNotEquals(p2copy.calculate(), p2.calculate());

        assumeFalse(p2.getAmount() > 0);
        assertEquals((1+p2.getOutgoingInterest())*p2.getAmount(), p2.calculate());
    }

    @Test
    void equalsTest()
    {
        assertNotEquals(p1,p2);
        assertEquals(p2, p2copy);
        assertDoesNotThrow( () -> assertNotEquals(p1, new Payment("06.12.2022", 1000, "test equals")));

        assertDoesNotThrow(() -> assertNotEquals(p2, new Transfer("06.12.2022", 1000, "test equals")));
    }
    @Test
    void toStringTest()
    {
        String rightOutput =
                "Date: " + p2.getDate() +
                "\nAmount: " + p2.getAmount() +
                "\nDescription: " + p2.getDescription() +
                "\nCalculated Amount: " + p2.calculate()+
                "\nIncInterest: " + p2.getIncomingInterest() +
                "\nOutInterest: " + p2.getOutgoingInterest() + '\n';
        assertEquals(rightOutput, p2.toString());
    }
}