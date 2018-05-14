/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author sam
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Groepsbewerking.findAll",
            query = "SELECT o FROM Groepsbewerking o")
})
public final class Groepsbewerking implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String omschrijving;
    private int factor;

    @Enumerated(EnumType.STRING)
    private OperatorEnum operator;

    public Groepsbewerking(String omschrijving, int factor, OperatorEnum operator) {
        setOmschrijving(omschrijving);
        setFactor(factor);
        setOperator(operator);
    }

    protected Groepsbewerking() {

    }

    public int getId() {
        return id;
    }

    public String getOmschrijving() {
        return omschrijving;
    }

    public int getFactor() {
        return factor;
    }

    public OperatorEnum getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return omschrijving;
    }

    public void setOmschrijving(String omschrijving) {
        this.omschrijving = omschrijving;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
    }

}
