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
public class KlasTest {

    private Klas klas;

    @Before
    public void before() {
        klas = new Klas("Klas1");
    }

    @Test
    public void nieuweKlas_GeeftNulLeerlingen() {
        Assert.assertEquals(0, klas.getLeerlingen().size());
    }

    @Test
    public void nieuweLeerling_VoegtLeerlingToeAanKlas() {
        String naam = "Jan";
        klas.voegLeerlingToe(naam);
        Assert.assertTrue(klas.getLeerlingen().contains(naam));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bestaandeLeerlingToevoegen_GooitIllegalArgumentException() {
        String naam1 = "Jan";
        String naam2 = "Jan".toLowerCase();
        klas.voegLeerlingToe(naam1);
        klas.voegLeerlingToe(naam2);
    }

    @Test
    public void leerlingVerwijderen_VerwijdertLeerling() {
        String naam = "Jan";
        klas.voegLeerlingToe(naam);
        klas.verwijderLeerling(naam);
        Assert.assertTrue(klas.getLeerlingen().isEmpty());
    }
}
