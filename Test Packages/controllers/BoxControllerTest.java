/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.BreakOutBox;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;
import repository.GenericDao;

/**
 *
 * @author devri
 */
public class BoxControllerTest {

    private BoxController boxController;
    private GenericDao<BreakOutBox> boxRepo;

    private BreakOutBox box;

    @Before
    public void before() {
        boxController = new BoxController();

        // Mock
        boxRepo = Mockito.mock(GenericDao.class);

        // Mocks trainen
        box = new BreakOutBox("bob1", "bob omschrijving", new ArrayList<>(), new ArrayList<>());
        Mockito.when(boxRepo.get(1)).thenReturn(box);
        Mockito.when(boxRepo.get(2)).thenReturn(null);
        Mockito.when(boxRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new BreakOutBox[]{
                                    box,
                                    new BreakOutBox("bob2", "bob omschrijving 2", new ArrayList<>(), new ArrayList<>())
                                }
                        )
                )
        );

        // SetterInjection mocks
        boxController.setBreakOutBoxRepo(boxRepo);
    }

//<editor-fold defaultstate="getters" desc="comment">
    @Test
    public void getAllBobs_returnsAllBobs() {
        Assert.assertEquals(2, boxController.getAllBreakOutBoxen().size());
    }
    
    @Test
    public void geefBreakOutBox_returnsCorrectBoB(){
        BreakOutBox box = boxController.GeefBreakOutBox(1);
        Assert.assertEquals(box, this.box);
    }
//</editor-fold>

    // <editor-fold desc="=== createBox ===" defaultstate="collapsed">
    @Test
    public void createBox_voegtNieuweBoxToe() {
        boxController.createBreakOutBox("bob3", "bob omschrijving 3", new ArrayList<>(), new ArrayList<>());
        Mockito.verify(boxRepo).insert(Mockito.any(BreakOutBox.class));
    }
    // </editor-fold>

    // <editor-fold desc="=== updateBox ===" defaultstate="collapsed">
    @Test
    public void updateBox_wordtGewijzigd() {
        boxController.updateBreakOutBox(1, "nieuweBox1", "nieuweBox1 omschrijving", new ArrayList<>(), new ArrayList<>());
        Assert.assertEquals("nieuweBox1", box.getNaam());
        Mockito.verify(boxRepo).update(Mockito.any(BreakOutBox.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateBox_BoxNietGevonden_GooitNotFoundException() {
        int foutId = 8;
        boxController.updateBreakOutBox(foutId, "boxNietCorr", "foute omschrijving", new ArrayList<>(), new ArrayList<>());
    }
    // </editor-fold>

    // <editor-fold desc="=== deleteSessie ===" defaultstate="collapsed">
    @Test
    public void verwijderBoxMetJuistId_VerwijdertBox() {
        boxController.deleteBreakOutBox(1);
        Mockito.verify(boxRepo).delete(Mockito.any(BreakOutBox.class));
    }

    @Test(expected = NotFoundException.class)
    public void verwijderBoxMetFoutId_GooitNotFoundException() {
        int foutId = 90;
        boxController.deleteBreakOutBox(foutId);
        Mockito.verify(boxRepo).delete(Mockito.any(BreakOutBox.class));
    }
    // </editor-fold>

    //<editor-fold desc="=== applyFilter ===" defaultstate="collapsed">
    @Test
    public void applyFilter_GeenTextReturnsAll() {
        boxController.applyFilter("");
        Assert.assertEquals(2, boxController.getAllBreakOutBoxen().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        boxController.applyFilter("bob 1");
        Assert.assertEquals(1, boxController.getAllBreakOutBoxen().size());
    }

    @Test
    public void applyFilter_WithWhiteSpacesReturnsMatches() {
        boxController.applyFilter(" b o b 1");
        Assert.assertEquals(1, boxController.getAllBreakOutBoxen().size());
    }
    // </editor-fold>
}
