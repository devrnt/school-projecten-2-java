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
    private Leerling leerling;

    @Before
    public void before() {
        klas = new Klas("Klas1");
        leerling = new Leerling();
    }

    @Test
    public void nieuweKlas_GeeftNulLeerlingen() {
        Assert.assertEquals(0, klas.getLeerlingen().size());
    }

    @Test
    public void nieuweLeerling_VoegtLeerlingToeAanKlas() {
        leerling.setVoornaam("Jan");
        leerling.setNaam("Timmermans");
        klas.voegLeerlingToe(leerling);
        Assert.assertTrue(klas.getLeerlingen().contains(leerling));
    }

    @Test(expected = IllegalArgumentException.class)
    public void bestaandeLeerlingToevoegen_GooitIllegalArgumentException() {
        leerling.setVoornaam("Jan");
        leerling.setNaam("Timmermans");
        klas.voegLeerlingToe(leerling);
        Leerling leerling2 = new Leerling("Jan".toLowerCase(), "Timmermans".toLowerCase());
        klas.voegLeerlingToe(leerling2);
    }

    @Test
    public void leerlingVerwijderen_VerwijdertLeerling() {
        leerling.setVoornaam("Jan");
        leerling.setNaam("Timmermans");
        klas.voegLeerlingToe(leerling);
        klas.verwijderLeerling(leerling);
        Assert.assertTrue(klas.getLeerlingen().isEmpty());
    }
}
