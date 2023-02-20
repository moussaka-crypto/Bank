import bank.*;
import bank.exceptions.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
class PrivateBankTest {

    PrivateBank pb, pbCopy, pbEquals;
    Payment p1, p2, p2copy;
    IncomingTransfer it; OutgoingTransfer ot;
    List<Transaction> transactionList = new ArrayList<>();

    @BeforeEach
    void fullHouse()
    {
        try{
            p1 = new Payment("12.12.2022", 100, "deposit", 0.3, 0.3);
            p2 = new Payment("13.12.2022", -150, "withdrawal", 0.25, 0.25);
            p2copy = new Payment(p2);

            it = new IncomingTransfer("12.12.2022", 100, "incoming", "first", "second");
            ot = new OutgoingTransfer("13.12.2022", 120, "outgoing", "second", "first");

            pb = new PrivateBank("Bank1", 0.420, 0.310);
            pbCopy = new PrivateBank(pb);

            pb.createAccount("firstAcc");
            pb.addTransaction("firstAcc", p1);
            pb.addTransaction("firstAcc", it);

            pb.createAccount("secondAcc");
            pb.addTransaction("secondAcc", p2);
            pb.addTransaction("secondAcc", ot);
        }
        catch(Exception ex){System.out.println(ex.getClass() + " | " + ex.getMessage());}
    }

    @AfterEach
    void cleanHouse() {
        File folder = new File("src/main/accounts");
        if(folder.exists()) {
            for(File file : Objects.requireNonNull(folder.listFiles())) {
                if(!file.isDirectory())
                    assertTrue(file.delete());
            }
        }
    }

    @Test
    void DeserializerTest()
    {
        try{
            PrivateBank test = new PrivateBank("Sparkasse", 0.21, 0.31);
            assertTrue(test.containsTransaction("firstAcc", it));
            //Lesen und Schreiben bewiesen, dass sie funktionieren
        }
        catch(Exception ex) {ex.getMessage();}
    }

    @Test
    void constructorTest()
    {
        assertNotNull(pb);
        assertFalse(pb.getName().isEmpty());
        assertTrue(pb.isPercent(pb.getIncomingInterest()));
        assertTrue(pb.isPercent(pb.getOutgoingInterest()));
    }
    @Test
    void copyConstructor_equals_Test()
    {
        assertEquals(pb, pbCopy);
        assertNotEquals(pb, pbEquals);
    }

    @Test
    void toStringTest()
    {
        String rightOutput =
                "Private Bank Infos: \n" +
                "name: " + pb.getName() + "\n"+
                "incomingInterest: " + pb.getIncomingInterest() + "\n"+
                "outgoingInterest: " + pb.getOutgoingInterest() + "\n";
        assertEquals(pb.toString(), rightOutput);
    }

    @Test
    void createAccountTest()
    {
        assertTrue(pb.getAccountBalance("firstAcc") >= 0 || pb.getAccountBalance("firstAcc") < 0);

        assertDoesNotThrow(() -> pb.createAccount("test") );
        assertThrows(AccountAlreadyExistsException.class, () -> pb.createAccount("test"));

        assertTrue(pb.getTransactions("test").isEmpty());

        assertDoesNotThrow(() -> pb.createAccount("test2", transactionList));
        assertEquals(pb.getTransactions("test2"), transactionList);
    }

    @Test
    void add_contains_TransactionTest() // addTransaction wird automatisch damit getestet
    {
        assertTrue(pb.containsTransaction("firstAcc", p1));
        assertTrue(pb.containsTransaction("firstAcc", it));
        assertTrue(pb.containsTransaction("secondAcc", p2));
        assertTrue(pb.containsTransaction("secondAcc", ot));

        assertFalse(pb.containsTransaction("firstAcc", p2));
        assertFalse(pb.containsTransaction("secondAcc", p1));
        assertFalse(pb.containsTransaction("firstAcc", ot));
        assertFalse(pb.containsTransaction("secondAcc", it));
    }

    @Test
    void removeTransactionTest()
    {
        try {
            pb.removeTransaction("secondAcc", p2);
            pb.removeTransaction("secondAcc", ot);
        }
        catch(Exception ex) {ex.getMessage();}
        //boolean test = pb.containsTransaction("secondAcc", p2);
        assertFalse(pb.containsTransaction("secondAcc", p2));
        assertFalse(pb.containsTransaction("secondAcc", ot));
        assertThrows(AccountDoesNotExistException.class, () -> pb.removeTransaction("boom", p2));
    }

    @Test
    void ExceptionsTest()
    {
        AccountAlreadyExistsException aaeEx = Assertions.assertThrows(AccountAlreadyExistsException.class,
                () -> pb.createAccount("firstAcc"));

        Assertions.assertEquals(AccountAlreadyExistsException.class, aaeEx.getClass());

        AccountDoesNotExistException adneEx = Assertions.assertThrows(AccountDoesNotExistException.class,
                () -> pb.addTransaction("failAcc", p1));

        Assertions.assertEquals(AccountDoesNotExistException.class, adneEx.getClass());

        TransactionAlreadyExistException taeEx = Assertions.assertThrows(TransactionAlreadyExistException.class,
                () -> pb.addTransaction("firstAcc", it));

        Assertions.assertEquals(TransactionAlreadyExistException.class, taeEx.getClass());

        TransactionDoesNotExistException tdneEx = Assertions.assertThrows(TransactionDoesNotExistException.class,
                () -> pb.removeTransaction("firstAcc", ot));

        Assertions.assertEquals(TransactionDoesNotExistException.class, tdneEx.getClass());

        TransactionAttributeException taEx = Assertions.assertThrows(TransactionAttributeException.class,
                () -> pb.addTransaction("firstAcc", new Transfer("00.00.0000", -20, "boom")));
        Assertions.assertEquals(TransactionAttributeException.class, taEx.getClass());
    }

    @Test
    void getTransactionsSortedTest() {
        assertDoesNotThrow(() -> pb.createAccount("Jojo"));
        assertDoesNotThrow(() -> {
            pb.addTransaction("Jojo",p1);
            pb.addTransaction("Jojo",p2);
        });

        List<Transaction> listTestAsc = pb.getTransactionsSorted("Jojo",true);
        //in einer Schleife testen ob akt element <= Nachfolger
        assertTrue(listTestAsc.get(0).equals(p2) && listTestAsc.get(1).equals(p1));

        List<Transaction> listTestDesc = pb.getTransactionsSorted("Jojo", false);
        // >=
        assertTrue(listTestDesc.get(0).equals(p1) && listTestDesc.get(1).equals(p2));
    }

    @Test
    void getTransactionsByTypeTest() {
        try{
            pb.createAccount("Jojo");
        }
        catch(Exception ex) {ex.getMessage();}

        assertDoesNotThrow(() -> {
            pb.addTransaction("Jojo",p1);
            pb.addTransaction("Jojo",p2);
        });

        List<Transaction> listPos = pb.getTransactionsByType("Jojo",true);
        assertTrue(listPos.get(0).equals(p1) && !listPos.contains(p2));

        List<Transaction> listNeg = pb.getTransactionsByType("Jojo", false);
        assertTrue(listNeg.get(0).equals(p2) && !listNeg.contains(p1));
    }

    @Test
    void BonusTest()
    {
        try{
            pb.createAccount("bonus",transactionList);
        }
        catch(Exception ex) {ex.getMessage();}

        assertDoesNotThrow(() -> {
            PrivateBank privateBankTestSerialize = new PrivateBank("bonus",0.2,0.1);
            assertDoesNotThrow(() -> privateBankTestSerialize.createAccount("bonus"));
            assertEquals(pb.accountsToTransactions.get("bonus"), privateBankTestSerialize.accountsToTransactions.get("bonus"));
        });
    }

    @Test
    void deleteAccountTest()
    {
        assertDoesNotThrow(() -> {
            pb.createAccount("dummy", pb.getTransactions("firstAcc"));
            assertEquals(2, pb.getTransactions("dummy").toArray().length);
            pb.deleteAccount("dummy");
        });
        assertThrows(AccountDoesNotExistException.class, () -> pb.deleteAccount("dummy"));
        assertThrows(NullPointerException.class, () -> {int i = pb.getTransactions("dummy").toArray().length;});
    }
}
