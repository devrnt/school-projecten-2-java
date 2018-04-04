/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Klas;
import domein.Sessie;
import domein.SoortOnderwijsEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.SessieDaoJpa;

/**
 *
 * @author devri
 */
public class SessieControllerTest {

    private SessieController sessieController;
    private SessieDaoJpa sessieRepo;

    private Sessie sessie;

    private Calendar c = Calendar.getInstance();

    @Before
    public void before() {
        sessieController = new SessieController();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_YEAR, 1);
        // Mocks
        sessieRepo = Mockito.mock(SessieDaoJpa.class);

        // Mocks trainen
        //tijdelijk
        sessie = new Sessie("Sessie 1", "Omschrijving Sessie 1",
                new Klas(), 2, c.getTime(),
                SoortOnderwijsEnum.dagonderwijs, "feedback"
        );
        Mockito.when(sessieRepo.get(1)).thenReturn(sessie);
        Mockito.when(sessieRepo.get(2)).thenReturn(null);
        Mockito.when(sessieRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new Sessie[]{
                                    sessie,
                                    new Sessie("Sessie 2", "Omschrijving Sessie 2", new Klas("2A1"), 2, c.getTime(), SoortOnderwijsEnum.dagonderwijs, "feedback")
                                }
                        )
                )
        );

        // SetterInjection mocks
        sessieController.setSessieRepo(sessieRepo);
    }

    @Test
    public void getAllSessies_returnsAllSessies() {
        Assert.assertEquals(2, sessieController.getAllSessies().size());
    }

    // <editor-fold desc="=== createSessie ===" >
    @Test
    public void createSessie_voegtNieuweSessieToe() {
        sessieController.createSessie("Sessie3", "Sessie3 omschrijving", new Klas(), 3, c.getTime(), SoortOnderwijsEnum.dagonderwijs, "feedback");
        Mockito.verify(sessieRepo).insert(Mockito.any(Sessie.class));
    }
    // </editor-fold>

    // <editor-fold desc="=== updateSessie ===" >
    @Test
    public void updateSessie_wordtGewijzigd() {
        sessieController.updateSessie(1, "sessie89", "omschrijving", new Klas(), 2, c.getTime(), SoortOnderwijsEnum.dagonderwijs, "feedback");
        Assert.assertEquals("sessie89", sessie.getNaam());
        Mockito.verify(sessieRepo).update(Mockito.any(Sessie.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateSessie_sessieNietGevonden_GooitNotFoundException() {
        int foutId = 8;
        sessieController.updateSessie(foutId, "sessie89", "omschrijving", new Klas(), 2, c.getTime(), SoortOnderwijsEnum.dagonderwijs, "feedback");
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
}
