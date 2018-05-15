/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.io.Serializable;
import javafx.beans.property.SimpleStringProperty;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

/**
 *
 * @author sam
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Groepsbewerking.findAll",
            query = "SELECT o FROM Groepsbewerking o")
})
public class Groepsbewerking implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String omschrijving;
    private int factor;
    @Enumerated(EnumType.STRING)
    private OperatorEnum operator;
    @Transient
    private SimpleStringProperty omschrijvingProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty factorProperty = new SimpleStringProperty();
    @Transient
    private SimpleStringProperty operatorProperty = new SimpleStringProperty();
    
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
        omschrijvingProperty.set(omschrijving);
    }
    
    public void setFactor(int factor) {
        this.factor = factor;
        factorProperty.set(Integer.toString(factor));
    }
    
    public void setOperator(OperatorEnum operator) {
        this.operator = operator;
        operatorProperty.set(operator.toString());
    }
    
    public SimpleStringProperty getOmschrijvingProperty(){
        return omschrijvingProperty;
    }
    
    public SimpleStringProperty getFactorProperty(){
        return factorProperty;
    }
    
    public SimpleStringProperty getOperatorProperty(){
        return operatorProperty;
    }
    
}
