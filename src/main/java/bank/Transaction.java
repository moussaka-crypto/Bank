package bank;

import bank.exceptions.TransactionAttributeException;

import java.util.Objects;

/**
 * Eine abstrakte Oberklasse, die alle Gemeinsamkeiten von Transaktionen enthält
 *  @author Gordon Freeman
 *  @version 1.0.2
 */
public abstract class Transaction implements CalculateBill {
    /**
     * Protected Attribute:
     * Date: Datum der Erstellung des Beitrags {@link Transaction#getDate()}
     * Amount: Geldmenge einer Transaction {@link Transaction#getAmount()}
     * Description: Beschreibung des Vorgangs {@link Transaction#getDescription()}
     */
    protected String date, description;
    protected double amount;

    /**
     * Methode, die auf den Wert von {@link Transaction#date} zugreift
     * @return String {@link Transaction@date}
     */
    public String getDate() {return date;}
    /**
     * Methode, die auf den Wert von {@link Transaction#amount} zugreift
     *
     * @return double {@link Transaction#amount}
     */
    public double getAmount() {return Math.round(amount * 100.0) / 100.0;}
    /**
     * Methode, die auf den Wert von {@link Transaction#description} zugreift
     * @return String {@link Transaction#description}
     */
    public String getDescription() {return description;}

    /**
     * Methode zum Setzen eines privaten Attributs {@link Transaction#description}
     * @param description neue Beschreibung, die gesetzt wird
     */
    public void setDescription(String description) {this.description=description;}
    /**
     * Methode zum Setzen eines privaten Attributs {@link Transaction#amount}
     * @param amount neue Geldmenge, die gesetzt wird
     */
    public void setAmount(double amount) throws TransactionAttributeException {
        this.amount=amount;
    }
    /**
     * Methode zum Setzen eines privaten Attributs {@link Transaction#date}
     * @param date neues Datum, die gesetzt wird
     */
    public void setDate(String date) {this.date = date;}

    /**
     * Konstruktor der Klasse {@link Transaction} mit allen privaten Attributen
     * @param date {@link Transaction#date}
     * @param amount {@link Transaction#amount}
     * @param description {@link Transaction#description}
     */
    public Transaction(String date, double amount, String description)
            throws TransactionAttributeException {
        setDate(date);
        setAmount(amount);
        setDescription(description);
    }

    /**
     * Die überschriebene Methode {@link Object#toString()} der Klasse {@link Object}
     * @return String, der alle Informationen der Klasse Transaction zurückgibt
     */
   @Override
    public String toString()
    {
        return //"Transaction Info: \n" +
                "Date: " + this.getDate() +
                "\nAmount: " + this.getAmount() +
                "\nDescription: " + this.getDescription();
    }

    /**
     * Die überschriebene Methode {@link Object#equals(Object)} der Klasse {@link Object}
     * @param o wird zu Transaction Objekt explizit gecastet und Attribute werden auf Gleichheit geprüft
     * @return true, wenn alle Attribute der Klasse Transaction dem Parameter Object gleichkommen
     *         true, wenn die Referenzen der Objekte gleich sind
     *         false, wenn Objekt/Klasse nicht existiert
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // Referenzen
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Double.compare(that.amount, amount) == 0 &&
                Objects.equals(date, that.date) &&
                Objects.equals(description, that.description);
    }
}
