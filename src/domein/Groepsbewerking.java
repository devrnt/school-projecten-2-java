/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 *
 * @author sam
 */
@Entity
public class Groepsbewerking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String omschrijving;
    private int factor;
    private OperatorEnum operator;

    public Groepsbewerking(String omschrijving, int factor, OperatorEnum operator) {
        this.omschrijving = omschrijving;
        this.factor = factor;
        this.operator = operator;
    }
    
    protected Groepsbewerking(){
        
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
    
    
    
    
}
