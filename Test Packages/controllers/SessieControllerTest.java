/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Actie;
import domein.BreakOutBox;
import domein.FoutAntwoordActieEnum;
import domein.Klas;
import domein.Oefening;
import domein.Sessie;
import domein.SessieBeheer;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.SessieDao;

/**
 *
 * @author devri
 */
public class SessieControllerTest {

    private SessieController sessieController;
    private SessieDao sessieRepo;

    private Sessie sessie;

    private Calendar c = Calendar.getInstance();

    @Before
    public void before() {
        sessieController = new SessieController();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        // Mocks
        sessieRepo = Mockito.mock(SessieDao.class);

        // Mocks trainen
        //tijdelijk
        List<Oefening> oef = new ArrayList<Oefening>();
        List<Actie> act = new ArrayList<Actie>();
        oef.add(new Oefening());
        oef.add(new Oefening());
        act.add(new Actie("o"));
        sessie = new Sessie("Sessie 1", "Omschrijving Sessie 1",
                new Klas(), new BreakOutBox("legeBox", "legeOmscrijving", SoortOnderwijsEnum.dagonderwijs, oef, act), c.getTime(),
                SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false
        );
        Mockito.when(sessieRepo.get(1)).thenReturn(sessie);
        Mockito.when(sessieRepo.get(2)).thenReturn(null);
        Mockito.when(sessieRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new Sessie[]{
                                    sessie,
                                    new Sessie("Sessie 2", "Omschrijving Sessie 2", new Klas("2A1"), new BreakOutBox("legeBox", "legeOmscrijving", SoortOnderwijsEnum.dagonderwijs, oef, act), c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false)
                                }
                        )
                )
        );

        // SetterInjection mocks
        sessieController.getSessieBeheer().setSessieRepo(sessieRepo);
    }

    //<editor-fold defaultstate="getters" desc="comment">
    @Test
    public void getAllSessies_returnsAllSessies() {
        Assert.assertEquals(2, sessieController.getAllSessies().size());
    }

    @Test
    public void getSessie_returnsCorrectSessie() {
        Sessie sessie = sessieController.getSessie(1);
        Assert.assertEquals(sessie, this.sessie);
    }
//</editor-fold>

    // <editor-fold desc="=== createSessie ===" >
    @Test
    public void createSessie_voegtNieuweSessieToe() {
        List<Oefening> oef = new ArrayList<Oefening>();
        List<Actie> act = new ArrayList<Actie>();
        oef.add(new Oefening());
        oef.add(new Oefening());
        act.add(new Actie("o"));
        sessieController.createSessie("Sessie3", "Sessie3 omschrijving", new Klas(), new BreakOutBox("legeBox", "legeOmscrijving", SoortOnderwijsEnum.dagonderwijs, oef, act), c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback, false);
        Mockito.verify(sessieRepo).insert(Mockito.any(Sessie.class));
    }
    // </editor-fold>

    // <editor-fold desc="=== updateSessie ===" >
    @Test
    public void updateSessie_wordtGewijzigd() {
        List<Oefening> oef = new ArrayList<Oefening>();
        List<Actie> act = new ArrayList<Actie>();
        oef.add(new Oefening());
        oef.add(new Oefening());
        act.add(new Actie("o"));
        sessieController.updateSessie(1, "sessie89", "omschrijving", new Klas(), new BreakOutBox("legeBox", "legeOmscrijving", SoortOnderwijsEnum.dagonderwijs, oef, act), c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback);
        Assert.assertEquals("sessie89", sessie.getNaam());
        Mockito.verify(sessieRepo).update(Mockito.any(Sessie.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateSessie_sessieNietGevonden_GooitNotFoundException() {
        List<Oefening> oef = new ArrayList<Oefening>();
        List<Actie> act = new ArrayList<Actie>();
        oef.add(new Oefening());
        oef.add(new Oefening());
        act.add(new Actie("o"));
        int foutId = 8;
        sessieController.updateSessie(foutId, "sessie89", "omschrijving", new Klas(), new BreakOutBox("legeBox", "legeOmscrijving", SoortOnderwijsEnum.dagonderwijs, oef, act), c.getTime(), SoortOnderwijsEnum.dagonderwijs, FoutAntwoordActieEnum.feedback);
    }
    // </editor-fold>

    // <editor-fold desc="=== deleteSessie ===" >
    @Test
    public void verwijderSessieMetJuistId_VerwijdertId() {
        sessieController.deleteSessie(1);
        Mockito.verify(sessieRepo).delete(Mockito.any(Sessie.class));
    }

    @Test(expected = NotFoundException.class)
    public void verwijderSessieMetFoutId_GooitNotFoundException() {
        int foutId = 90;
        sessieController.deleteSessie(90);
        Mockito.verify(sessieRepo).delete(Mockito.any(Sessie.class));
    }
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== applyFilter ===">
    @Test
    public void applyFilter_NoTextReturnsAll() {
        sessieController.applyFilter("");
        Assert.assertEquals(2, sessieController.getAllSessies().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        sessieController.applyFilter("sessie 1");
        Assert.assertEquals(1, sessieController.getAllSessies().size());
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getOefeningBeheer ===">
    @Test
    public void getSessieBeheer_returnsSessieBeheer() {
        Assert.assertTrue(sessieController.getSessieBeheer() instanceof SessieBeheer);
    }
    //</editor-fold>

}
