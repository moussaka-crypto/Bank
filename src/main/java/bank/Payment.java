package bank;
/**
 * Encapsulates the functionality of a payment system in a Bank. The intanced object is either deposit(Einzahlung) or withdrawal(Auszahlung)
 *
 * @author Gordon Freeman
 * @version 1.0.2
 */

import bank.exceptions.TransactionAttributeException;

/**
 * Die Klasse repräsentiert Ein- und Auszahlungen.
 */
public class Payment extends Transaction {

    /**
     * private Attribute:
     * incomingInterest: Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Einzahlung („Deposit“) anfallen
     * outgoingInterest: Zinsen (positiver Wert in Prozent, 0 bis 1), die bei einer Auszahlung („Withdrawal“) anfallen
     */
    private double incomingInterest, outgoingInterest;
    /**
     *
     * @param interest ob Zins in Prozent möglich ist
     * @return true wenn im Intervall 1% bis 100%
     *         false wenn nicht
     */
    private boolean isPercent(double interest) {return (interest >= 0 && interest <= 1.00);}

    /**
     * Methode, die auf den Wert vom privaten Attribut incomingInterest zugreift
     * @return double {@link Payment#incomingInterest}
     */
    public double getIncomingInterest() {return Math.round(incomingInterest * 100.0) / 100.0;}
    /**
     * Methode, die auf den Wert vom privaten Attribut outgoingInterest zugreift
     * @return double {@link Payment#outgoingInterest}
     */
    public double getOutgoingInterest() {return Math.round(outgoingInterest * 100.0) / 100.0;}
    /**
     * Methode, die den Wert von {@link Payment#outgoingInterest} setzt
     * @param outgoingInterest neuer Zins, wodurch die alte ersetzt wird
     * nur wenn {@link Payment#isPercent(double)} true liefert, sonst Fehlermeldung
     */
    public void setOutgoingInterest(double outgoingInterest) throws TransactionAttributeException{
        if (isPercent(outgoingInterest)) {
            this.outgoingInterest = outgoingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }
    /**
     * Methode, die den Wert von {@link Payment#incomingInterest} setzt
     * @param incomingInterest neue Zins, wodurch die alte ersetzt wird
     * nur wenn {@link Payment#isPercent(double)} true liefert, sonst Fehlermeldung
     */
    public void setIncomingInterest(double incomingInterest) throws TransactionAttributeException {
        if (isPercent(incomingInterest)) {
            this.incomingInterest = incomingInterest;
        }
        else{
            throw new TransactionAttributeException("Interest out of bounds!");
        }
    }

    /**
     * Die überschriebene Methode, die die Beschreibung von einem Payment Objekt verändert, abhängig vom Amount
     * @param description neue Beschreibung, die gesetzt wird
     */

    public void setDescription(String description) { this.description = description;
    }

    /**
     * Konstruktor des Objekts Payment mit vererbten und eigenen Attributen
     *
     * @param date the date of the payment
     * @param amount the amount of the payment
     * @param description the information about the payment
     */
    public Payment(String date, double amount, String description) throws TransactionAttributeException {
        super(date,amount,description); //Aufruf von Konstr. mit 3 Atributen
    }

    /**
     * Konstruktor des Objekts Payment mit vererbten und eigenen Attributen
     *
     * @param date the date of the payment
     * @param amount the amount of the payment
     * @param description the information about the payment
     * @param incomingInterest the incoming interest for the deposit
     * @param outgoingInterest the outgoing interest for the withdrawal
     */
    public Payment(String date, double amount, String description, double incomingInterest, double outgoingInterest)
            throws TransactionAttributeException {
        super(date,amount,description); //Aufruf von Konstr. mit 3 Atributen
        setIncomingInterest(incomingInterest);
        setOutgoingInterest(outgoingInterest);
    }
    /**
     * Copy Constructor der Klasse Payment
     * @param p als Instanz der Klasse, deren Attribute adaptiert werden
     */
    public Payment(Payment p) throws TransactionAttributeException {
        this(p.getDate(), p.getAmount(), p.getDescription(), p.getIncomingInterest(), p.getOutgoingInterest());
    }

    /**
     * Implementiert Methode {@link CalculateBill#calculate()} des Interfaces {@link CalculateBill}
     * Es wird geprüft, ob Amount positiv ist:
     * falls ja, soll der Wert des incomingInterest-Attributes prozentual von der Einzahlung abgezogen und das Ergebnis zurückgegeben werden
     * sonst, soll der Wert des outgoingInterest-Attributes prozentual zu der Auszahlung hinzuaddiert und das Ergebnis zurückgegeben werden.
     * @return double Wert des veränderten Betrags
     */
    @Override
    public double calculate()
    {
        if(this.getAmount() >= 0)
        {
            return (1-getIncomingInterest())*this.getAmount();
        }
        else{
            return (1+getOutgoingInterest())*this.getAmount();
        }
    }

    /**
     * Die überschriebene Methode {@link Object#toString()} der Klasse Object
     * @return String, der alle Informationen der Klasse Payment zurückgibt
     * Die Methode ruft die {@link Transaction#toString()} Methode der Oberklasse und
     * dann gibt die Werte der eigenen Klasse aus.
     */
    @Override
    public String toString()
    {
        return super.toString() + "\nCalculated Amount: " + this.calculate()+
                "\nIncInterest: " + this.getIncomingInterest() +
                "\nOutInterest: " + this.getOutgoingInterest() + '\n';
    }

    /**
     * Die überschriebene Methode {@link Object#equals(Object)} der Klasse Objekt
     * Die Methode ruft als erstens {@link Transaction#equals(Object)} und dann prüft die Attribute
     * der eigenen Attribute auf Gleichheit.
     * @param o wird zu Payment Objekt explizit gecastet und Attribute werden auf Gleichheit geprüft
     * @return Boolean, ob alle Attribute der Klasse Payment dem Parameter Object gleichkommen
     */

    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false; // falls nicht existiert
        Payment payment = (Payment) o;
        return Double.compare(payment.incomingInterest, incomingInterest) == 0 &&
                Double.compare(payment.outgoingInterest, outgoingInterest) == 0;
    }
}