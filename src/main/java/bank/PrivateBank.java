package bank;
import bank.exceptions.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class PrivateBank implements Bank{
    private String name;
    private double incomingInterest, outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new LinkedHashMap<>();
    private String directoryName = "src/main/accounts";

    /**
     * Getter für das Attribut incomingInterest
     * Gibt directoryName zurück
     */
    public String getDirectoryName() {
        return directoryName;
    }
    /**
     * Setter für das Attribut Name
     * @param directoryName setzt den Namen der Bank
     */
    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    /**
     * @param interest ob Zinssatz erlaubt ist
     * @return true wenn im Intervall [1, 100] %
     */
    public boolean isPercent(double interest) {return (interest >= 0 && interest <= 1.00);}

    /**
     * Getter für das Attribut incomingInterest
     * Gibt Name zurück
     */
    public String getName() {return name;}
    /**
     * Setter für das Attribut Name
     * @param name setzt den Namen der Bank
     */
    public void setName(String name) {this.name = name;}
    /**
     * Getter für das Attribut incomingInterest
     * Gibt incomingInterest zurück
     */

    public double getIncomingInterest() {return incomingInterest;}
    /**
     * Getter für das Attribut outgoing Interest
     * Gibt outgoingInterest zurück
     */
    public double getOutgoingInterest() {return outgoingInterest;}

    /**
     * Methode, die den Wert von {@link PrivateBank#incomingInterest} setzt
     * @param incomingInterest neue Zins, wodurch die alte ersetzt wird
     * nur wenn {@link PrivateBank#isPercent(double)} true liefert, sonst Fehlermeldung
     * @exception TransactionAttributeException thrown during Attribute check
     */
    public void setIncomingInterest(double incomingInterest)
            throws TransactionAttributeException {
        if (isPercent(incomingInterest)) {
            this.incomingInterest = incomingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }

    /**
     * Methode, die den Wert von {@link PrivateBank#outgoingInterest} setzt
     * @param outgoingInterest neuer Zins, wodurch die alte ersetzt wird
     * nur wenn {@link PrivateBank#isPercent(double)} true liefert, sonst Fehlermeldung
     * @exception TransactionAttributeException thrown during Attribute check
     */
    public void setOutgoingInterest(double outgoingInterest)
            throws TransactionAttributeException {
        if (isPercent(outgoingInterest)) {
            this.outgoingInterest = outgoingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }
    /**
     * Constructor for Private Bank
     * @param name Name of Bank
     * @param incomingInterest inc Interest
     * @param outgoingInterest outg Interest
     * @throws TransactionAttributeException during attr check by setter methods
     */
    public PrivateBank(String name, double incomingInterest, double outgoingInterest)
            throws TransactionAttributeException, TransactionAlreadyExistException, AccountAlreadyExistsException, IOException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
        readAccounts();
    }

    /**
     *
     * @param pb instance of class to be copied
     * @throws TransactionAttributeException during attribute check
     */
    public PrivateBank(PrivateBank pb)
            throws TransactionAttributeException, TransactionAlreadyExistException, AccountAlreadyExistsException, IOException {
        this(pb.getName(), pb.getIncomingInterest(), pb.getOutgoingInterest());
    }

    /**
     * Die überschriebene Methode {@link Object#equals(Object)} der Klasse Objekt
     * Die Methode ruft als erstens {@link PrivateBank #equals(Object)} und dann prüft die eigenen Attribute auf Gleichheit.
     * @param obj wird zu Payment Objekt explizit gecastet und Attribute werden auf Gleichheit geprüft
     * @return Boolean, ob alle Attribute der Klasse Payment dem Parameter Object gleichkommen
     */
    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj instanceof PrivateBank pb)
        {
            return this.name.equals(pb.name) &&
                    this.incomingInterest == pb.incomingInterest &&
                    this.outgoingInterest == pb.outgoingInterest;
        }
        return false;
    }

    /**
     * Die überschriebene Methode {@link Object#toString()} der Klasse Object
     * @return String, der alle Informationen der Klasse zurückgibt
     * alle Informationen mit append in {@link StringBuilder} platzieren
     */
    @Override
    public String toString() {
        /*StringBuilder map_elements = new StringBuilder();
        for(Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()){
            map_elements.append(entry.getKey()).append(" -> ["); // alle Transaktionen zum Account

            for(Transaction tr : entry.getValue()){
                map_elements.append(tr.getDescription()).append(" ").
                        append(tr.getAmount()).append(" \u20ac").append(" | ");
            }
            map_elements.append("]\n\n");
        }*/
        return "Private Bank Infos: \n" +
                "name: " + this.name + "\n"+
                "incomingInterest: " + this.incomingInterest + "\n"+
                "outgoingInterest: " + this.outgoingInterest + "\n";

    }
    /*
     *
     * @return  String des Inhalts der Map  {@link PrivateBank#accountsToTransactions}
     */
    /*public String MapToString(){
        String map_elements="";
        for(Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()){
            map_elements += entry.getKey() +" => ";
            for(Transaction tra : entry.getValue()){
                if(tra.getClass().equals(Payment.class)){
                    Payment payment = (Payment) tra;
                    map_elements += "\n\t\t{Class: \""+ tra.getClass().getSimpleName() +"\" / Desc: \"" +tra.getDescription() + "\" / Amount: \""+ tra.getAmount()+ " EUR"
                            +"\" / Inc: "+ payment.getIncomingInterest() + "\" Outgoing: \""+ payment.getOutgoingInterest()+"\" }";
                }else if(tra.getClass().equals(Transfer.class)){
                    Transfer transfer = (Transfer) tra;
                    map_elements += "\n\t\t{Class: \""+ tra.getClass().getSimpleName() +"\" / Desc: \"" +tra.getDescription() + "\" / Amount: \""+ tra.getAmount()+ " EUR\""
                            +"\" / Snd: "+ transfer.getSender() + "\" / Recip: \""+ transfer.getRecipient()+"\" }";
                }else{
                    map_elements += "\n\t\t{Class: \""+ tra.getClass().getSimpleName() +"\" / Desc: \"" +tra.getDescription() + "\" / Amount: \""+ tra.getAmount()+ " EUR\""+" }";
                }

            }
            map_elements += "\n";
        }
        return map_elements;
    }
    */

    /**
     * Reads persistent accounts from {@link PrivateBank#getDirectoryName()}
     * by parsing contents to {@link PrivateBank#accountsToTransactions}
     * @throws IOException
     * @throws AccountAlreadyExistsException
     * @throws TransactionAttributeException
     * @throws TransactionAlreadyExistException
     */
    public void readAccounts()
            throws IOException, AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {

        File[] filePaths = new File(directoryName).listFiles();
        assert filePaths != null;

        for(File path : filePaths)
        {
            String result = new String(Files.readAllBytes(Paths.get(String.valueOf(path))));

            //extract account name
            String filePathName = String.valueOf(path).split("\\.(?=[^\\.]+$)")[0]; // Pfad ohne .json
            String accountName = filePathName.substring(filePathName.lastIndexOf("\\")).split("\\\\")[1]; //UNC path name

            //Json String as JsonArray
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            JsonArray jsonArr = gson.fromJson(result, JsonArray.class);

            //jsonArr as Transaction List
            Type transactionListType = new TypeToken<List<Transaction>>(){}.getType(); //mapping automatisch
            Gson gson1 = new GsonBuilder()
                    .registerTypeAdapter(transactionListType, new CustomDeserializer())
                    .create();
            List<Transaction> list = gson1.fromJson(jsonArr, transactionListType);

            createAccount(accountName, list);
        }
    }

    /**
     * Makes account persistent by parsing its contents to a permanent json file
     * @param account to be serialized
     *
     * @throws IOException
     */
    public void writeAccount(String account) throws IOException
    {
        if(!accountsToTransactions.get(account).isEmpty())
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            StringBuilder json = new StringBuilder("[\n");
            List<Transaction> list = getTransactions(account); // go to town

            for(Transaction listElement : list)
            {
                gsonBuilder.registerTypeHierarchyAdapter(listElement.getClass(), new CustomSerializer());
                Gson gson = gsonBuilder.setPrettyPrinting().create();
                json.append(gson.toJson(listElement)).append(",\n");
            }
            //remove last comma
            StringBuffer sb = new StringBuffer(json.toString());
            sb.deleteCharAt(sb.length()-2);
            json = new StringBuilder(sb + "]"); // close list

            //als Datei schreiben
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(directoryName+"\\"+account+".json"));
            bufferedWriter.write(json.toString());
            bufferedWriter.close();
        }
    }

    /**
     * creates new account in the instance of Private Bank
     * @param account the account to be added
     * @throws AccountAlreadyExistsException when account name already used
     */
    @Override
    public void createAccount(String account)
            throws AccountAlreadyExistsException, IOException {

        if(this.accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists!");

        this.accountsToTransactions.put(account, new ArrayList<>());
        deleteJSON(account);
        writeAccount(account);
    }

    /**
     * Creates new account with a list of transactions
     * @param account      the account to be added
     * @param transactions a list of already existing transactions which should be added to the newly created account
     * @throws AccountAlreadyExistsException when account name already used
     * @throws TransactionAlreadyExistException transaction exists in another account
     * @throws TransactionAttributeException during attribute check
     */
    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException, IOException {

        createAccount(account); //opt
        for(Transaction tr : transactions){
            try{
                addTransaction(account, tr);
            }
            catch (AccountDoesNotExistException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Creates new transaction to a given account
     * @param account     the account to which the transaction is added
     * @param transaction the transaction which should be added to the specified account
     * @throws TransactionAlreadyExistException if transaction name already exists in bank
     * @throws AccountDoesNotExistException if account does not exist
     * @throws TransactionAttributeException during attribute check
     */
    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException, IOException {

        if(!this.accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Chosen account does not exist! No Transaction allowed!");

        if(containsTransaction(account,transaction))
            throw new TransactionAlreadyExistException("Transaction already exists!");

        if(transaction instanceof Payment){
            ((Payment) transaction).setIncomingInterest(getIncomingInterest());
            ((Payment) transaction).setOutgoingInterest(getOutgoingInterest());
        }
        else if(transaction instanceof Transfer)
            if(transaction.getAmount() < 0)
                throw new TransactionAttributeException("Negative Amount detected!");

        this.accountsToTransactions.get(account).add(transaction);
        deleteJSON(account);
        writeAccount(account);
}

    /**
     * Deletes a transaction
     * @param account     the account from which the transaction is removed
     * @param transaction the transaction which is removed from the specified account
     * @throws AccountDoesNotExistException when the chosen account does not exist
     * @throws TransactionDoesNotExistException when chosen transaction does not exist
     */
    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException, IOException {

        if(!this.accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Chosen account does not exist! No Transaction detected!");

        if(!containsTransaction(account,transaction))
            throw new TransactionDoesNotExistException("Transaction does not exist!");

        this.accountsToTransactions.get(account).remove(transaction);
        deleteJSON(account);
        writeAccount(account);
    }

    private void deleteJSON(String account){
        File[] files = new File(directoryName).listFiles();
        for(File file : files){

            //extract account name
            String filePath = String.valueOf(file).split("\\.(?=[^\\.]+$)")[0]; // Pfad ohne .json
            String accountName = filePath.substring(filePath.lastIndexOf("\\")).split("\\\\")[1]; //UNC

            if(accountName.equals(account)){ //new stuff for delete
                file.delete();
                break;
            }
        }
    }

    /**
     * Deletes a given account in a Private Bank object
     * @param account the account to be deleted
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    @Override
    public void deleteAccount(String account)
            throws AccountDoesNotExistException, IOException
    {
        if(!this.accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Chosen account does not exist!");

        deleteJSON(account);
        accountsToTransactions.remove(account);
    }

    /**
     * checks if transaction list contains a specific transaction
     * @param account     the account from which the transaction is checked
     * @param transaction the transaction to search/look for
     * @return true if transaction part of list
     */
    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return accountsToTransactions.get(account).contains(transaction);
    }

    /**
     * calculates balance of a given account
     * @param account the selected account
     * @return balance as double value
     */
    @Override
    public double getAccountBalance(String account) {
        List<Transaction> transactionList = this.getTransactions(account);
        double balance = 0;

        for(Transaction t : transactionList){
            balance += t.calculate(); // auto werden inc/out Methode von calculate aufgerufen
        }
        return balance;
    }

    /**
     * delivers a list of all Transactions to a given account
     * @param account the selected account
     * @return array list with all transactions associated with chosen account
     */
    @Override
    public List<Transaction> getTransactions(String account) {
        return new ArrayList<>(this.accountsToTransactions.get(account));
    }

    /**
     * sorts transactions in ascending or descending order
     * @param account the selected account
     * @param asc     selects if the transaction list is sorted in ascending or descending order
     * @return a sorted transaction in a user-chosen order
     */
    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {

        List<Transaction> transactionList = this.getTransactions(account);
        transactionList.sort(Comparator.comparingDouble(Transaction::calculate));

        if(!asc)
            Collections.reverse(transactionList);

        return transactionList;
    }

    /**
     * delivers list of transactions by type
     * @param account  the selected account
     * @param positive selects if positive or negative transactions are listed(type)
     * @return list of user-defined transactions (positive or negative)
     */
    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {

        List<Transaction> transactionList = this.getTransactions(account);

        if(positive)
            transactionList.removeIf((Transaction t) -> t.calculate() < 0);
        else
            transactionList.removeIf((Transaction t) -> t.calculate() > 0);

        return transactionList;
    }

    /**
     * @return a list of all accounts in a private bank object
     */
    @Override
    public List<String> getAllAccounts() {
        return new ArrayList<>(accountsToTransactions.keySet());
    }
}
