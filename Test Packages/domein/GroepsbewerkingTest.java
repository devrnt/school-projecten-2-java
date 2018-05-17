/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author devri
 */
public class GroepsbewerkingTest {
    
    @Test (expected = IllegalArgumentException.class)
    public void createGroepsbewerking_factor0_throwsIllegalArgumentException(){
        new Groepsbewerking("factor nul", 0, OperatorEnum.delen);
    }
}
