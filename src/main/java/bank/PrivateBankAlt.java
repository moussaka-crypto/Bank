package bank;
import bank.exceptions.*;

import java.io.IOException;
import java.util.*;

public class PrivateBankAlt implements Bank{
    private String name;
    private double incomingInterest, outgoingInterest;
    public Map<String, List<Transaction>> accountsToTransactions = new HashMap<>();

    private boolean isPercent(double interest) {return (interest >= 0 && interest <= 1.00);}


    public String getName() {return name;}


    public void setName(String name) {this.name = name;}
    public double getIncomingInterest() {return incomingInterest;}
    public double getOutgoingInterest() {return outgoingInterest;}

    @Override
    public List<String> getAllAccounts() {
        return new ArrayList<>(accountsToTransactions.keySet());
    }

    public void setIncomingInterest(double incomingInterest)
            throws TransactionAttributeException {
        if (isPercent(incomingInterest)) {
            this.incomingInterest = incomingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }


    public void setOutgoingInterest(double outgoingInterest)
            throws TransactionAttributeException {
        if (isPercent(outgoingInterest)) {
            this.outgoingInterest = outgoingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }


    public PrivateBankAlt(String name, double incomingInterest, double outgoingInterest)
            throws TransactionAttributeException {
        setName(name);
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }


    public PrivateBankAlt(PrivateBankAlt pb)
            throws TransactionAttributeException {
        this(pb.getName(), pb.getIncomingInterest(), pb.getOutgoingInterest());
    }

    @Override
    public boolean equals(Object obj) {
        if(this==obj)
            return true;
        if(obj instanceof PrivateBankAlt pb)
        {
            return this.name.equals(pb.name) &&
                    this.incomingInterest == pb.incomingInterest &&
                    this.outgoingInterest == pb.outgoingInterest &&
                    this.accountsToTransactions.equals(pb.accountsToTransactions);
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder map_elements = new StringBuilder();
        for(Map.Entry<String, List<Transaction>> entry : accountsToTransactions.entrySet()){
            map_elements.append(entry.getKey()).append(" -> "); // alle Transaktionen zum Account

            for(Transaction tr : entry.getValue()){
                map_elements.append(tr.getDescription()).append(" ").append(tr.getAmount()).append(" EUR ");
            }
            map_elements.append("\n");
        }
        return "printing Object: \n" +
                "name: " + this.name + "\n"+
                "incomingInterest: " + this.incomingInterest + "\n"+
                "outgoingInterest: " + this.outgoingInterest + "\n"+
                "Map-Elements:\n" + map_elements;

    }


    @Override
    public void createAccount(String account)
            throws AccountAlreadyExistsException {

        if(this.accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists!");

        this.accountsToTransactions.put(account, new ArrayList<>());
    }



    @Override
    public void createAccount(String account, List<Transaction> transactions)
            throws AccountAlreadyExistsException, TransactionAlreadyExistException, TransactionAttributeException {

        if(this.accountsToTransactions.containsKey(account))
            throw new AccountAlreadyExistsException("Account already exists!");

        Set<Transaction> transactionSet = new HashSet<>(transactions); // keine Duplikate
        if(transactionSet.size() < transactions.size())
            throw new TransactionAlreadyExistException("Transaction already exists!");

        for(Transaction tr : transactions){
            if(tr instanceof Payment p){
                this.setIncomingInterest(p.getIncomingInterest());
                this.setOutgoingInterest(p.getOutgoingInterest());
            }
            else if(tr instanceof Transfer t){
                if(t.getAmount() < 0)
                    throw new TransactionAttributeException("Negative Amount detected!");
            }
        }
        this.accountsToTransactions.put(account, transactions);
    }

    @Override
    public void deleteAccount(String account) throws AccountDoesNotExistException, IOException
    {
     //nah
    }

    @Override
    public void addTransaction(String account, Transaction transaction)
            throws TransactionAlreadyExistException, AccountDoesNotExistException, TransactionAttributeException {

        if(!this.accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Chosen account does not exist! No Transaction allowed!");

        if(containsTransaction(account,transaction))
            throw new TransactionAlreadyExistException("Transaction already exists!");

        if(transaction instanceof Payment p){
            this.setIncomingInterest(p.getIncomingInterest());
            this.setOutgoingInterest(p.getOutgoingInterest());
        }
        else if(transaction instanceof Transfer t)
            if(transaction.getAmount() < 0)
                throw new TransactionAttributeException("Negative Amount detected!");

        this.accountsToTransactions.get(account).add(transaction);
    }


    @Override
    public void removeTransaction(String account, Transaction transaction)
            throws AccountDoesNotExistException, TransactionDoesNotExistException {

        if(!this.accountsToTransactions.containsKey(account))
            throw new AccountDoesNotExistException("Chosen account does not exist! No Transaction detected!");

        if(!containsTransaction(account,transaction))
            throw new TransactionDoesNotExistException("Transaction does not exist!");

        this.accountsToTransactions.get(account).remove(transaction);
    }


    @Override
    public boolean containsTransaction(String account, Transaction transaction) {
        return this.accountsToTransactions.get(account).contains(transaction);
    }


    @Override
    public double getAccountBalance(String account) {
        List<Transaction> transactionList = this.getTransactions(account);
        double balance = 0;
        String accOwner = this.getName();

        for(Transaction t : transactionList)
        {
            if(t instanceof Transfer tr) {
                if (tr.getSender().equals(accOwner)) { // Sendung
                    balance -= tr.getAmount();
                }
            }
            else
                balance += t.getAmount(); // Empfang
        }
        return balance;
    }


    @Override
    public List<Transaction> getTransactions(String account) {
        return new ArrayList<>(this.accountsToTransactions.get(account));
    }


    @Override
    public List<Transaction> getTransactionsSorted(String account, boolean asc) {

        List<Transaction> transactionList = this.getTransactions(account);
        transactionList.sort(Comparator.comparingDouble(Transaction::calculate));

        if(!asc)
            Collections.reverse(transactionList);

        return transactionList;
    }

    @Override
    public List<Transaction> getTransactionsByType(String account, boolean positive) {

        List<Transaction> transactionList = this.getTransactions(account);

        if(positive)
            transactionList.removeIf((Transaction t) -> t.getAmount() < 0);
        else
            transactionList.removeIf((Transaction t) -> t.getAmount() > 0);

        return transactionList;
    }
}
// shitty build
