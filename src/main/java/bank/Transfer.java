package bank;

/**
 * Encapsulates the functionality of a transfer system in a Bank.
 *
 * @author Gordon Freeman
 * @version 1.0.1
 */

import bank.exceptions.AccountDoesNotExistException;
import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/**
 * Die Klasse wird im Kontext vom Kontoüberweisungen verwendet.
 */
public class Transfer extends Transaction {

    /**
     * private Attribute:
     * Sender: Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen hat.
     * Recipient: Dieses Attribut gibt an, welcher Akteur die Geldmenge, die in amount angegeben wurde, überwiesen bekommen hat.
     */
    private String sender, recipient;

    /**
     * Methode, die auf den Wert vom privaten Attribut Sender zugreift
     * @return String {@link Transfer#sender}
     */
    public String getSender() {return sender;}
    /**
     * Methode, die auf den Wert vom privaten Attribut Recipient zugreift
     * @return String {@link Transfer#recipient}
     */
    public String getRecipient() {return recipient;}
    /**
     * Methode, die den Wert von {@link Transfer#recipient} setzt
     * @param recipient neuer Recipient der das alte ersetzt
     */
    public void setRecipient(String recipient) {this.recipient = recipient;}
    /**
     * Methode, die den Wert von {@link Transfer#sender} setzt
     * @param sender neuer Sender der das alte ersetzt
     */
    public void setSender(String sender) {this.sender=sender;}

    /**
     * Konstruktor des Objekts Transfers mit vererbten Attributen
     *
     * @param date the date of the transfer
     * @param amount the amount of the transfer
     * @param description the description about the transfer
     */
    public Transfer(String date, double amount, String description) throws TransactionAttributeException {
        super(date, amount, description);
        setAmount(amount);
    }

    /**
     * Konstruktor des Objekts Transfers mit vererbten und eigenen Attributen
     *
     * @param date the date of the transfer
     * @param amount the amount of the transfer
     * @param description the description about the transfer
     * @param sender the person who transfers
     * @param recipient the person who receives
     */
    public Transfer(String date, double amount, String description, String sender, String recipient) throws TransactionAttributeException {
        super(date,amount,description);
        setAmount(amount);
        setSender(sender);
        setRecipient(recipient);
    }
    /**
     * Copy Constructor der Klasse Transfer
     * @param tr als Instanz der Klasse, deren Attribute adaptiert werden
     */
    public Transfer(Transfer tr) throws TransactionAttributeException {
        this(tr.getDate(),tr.getAmount(),tr.getDescription(),tr.getSender(),tr.getRecipient());
    }

    /**
     * Überschriebene Methode, die den Wert von {@link Transfer#amount} setzt
     * @param amount neuer Amount der das alte ersetzt
     * Methode überprüft, ob Amount positiv ist, sonst wird Fehlermeldung auf der Konsole ausgegeben
     */
    @Override
    public void setAmount(double amount) throws TransactionAttributeException {
        if (amount >= 0.0) {
            super.setAmount(amount);
        } else {
            super.setAmount(0);
            throw new TransactionAttributeException("Negative Transfer amount detected!");
        }
    }

    /**
     * Implementiert Methode {@link CalculateBill#calculate()} des Interfaces {@link CalculateBill}
     * @return double Wert des unveränderten Betrags
     */
    @Override
    public double calculate()
    {
        return super.getAmount();
    }

    /**
     * Die überschriebene Methode {@link Object#toString()} der Klasse Object
     * @return String, der alle Informationen der Klasse Transfer zurückgibt
     * Die Methode ruft die {@link Transaction#toString()} Methode der Oberklasse und
     * dann gibt die Werte der eigenen Klasse aus.
     */
    @Override
    public String toString()
    {
        return super.toString() + "\nCalculated Amount: " + this.calculate()+
                "\nFrom: " + this.getSender() +
                "\nTo: " + this.getRecipient() + "\n";
    }

    /**
     * Die überschriebene Methode {@link Object#equals(Object)} der Klasse Objekt
     * Die Methode ruft als erstens {@link Transaction#equals(Object)} und dann prüft die Attribute
     * der eigenen Attribute auf Gleichheit.
     * @param o wird zu Transfer Objekt explizit gecastet und Attribute werden auf Gleichheit geprüft
     * @return Boolean, ob alle Attribute der Klasse Transfer dem Parameter Object gleichkommen
     */
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        Transfer transfer = (Transfer) o;
        return Objects.equals(sender, transfer.sender) &&
                Objects.equals(recipient, transfer.recipient);
    }
}