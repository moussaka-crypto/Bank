package bank;

/**
 * Ein Interface {@link CalculateBill} mit einer einzigen parameterlosen Methode calculate(), die von Payment und Transfer
 * implementiert wird. Dadurch wird Amount berechnet
 */
public interface CalculateBill {
    /**
     * Methode des Interfaces {@link CalculateBill}
     * @return double Wert der Berechnung, aber Methode nicht implementiert
     */
    double calculate();
}
