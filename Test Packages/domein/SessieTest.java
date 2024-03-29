/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author devri
 */
public class SessieTest {

    private Sessie sessie;
    private Calendar calendar;

    @Before
    public void before() {
        sessie = new Sessie();
        calendar = Calendar.getInstance();
    }

    @Test
    public void setSessieDatumOpVandaag_werkt() {
        Date vandaag = calendar.getTime();

        sessie.setDatum(vandaag);
        Assert.assertEquals(vandaag, sessie.getDatum());
    }

    @Test
    public void setSessieDatumOpMorgen_werkt() {
        Date morgen = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        sessie.setDatum(morgen);
        Assert.assertEquals(morgen, sessie.getDatum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void setSessieDatumOpGisteren_gooitExecption() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date gisteren = cal.getTime();

        sessie.setDatum(gisteren);
    }
}
