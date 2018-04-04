/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.BreakOutBox;
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
        box = new BreakOutBox("bob1", "bob omschrijving", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Mockito.when(boxRepo.get(1)).thenReturn(box);
        Mockito.when(boxRepo.get(2)).thenReturn(null);
        Mockito.when(boxRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new BreakOutBox[]{
                                    box,
                                    new BreakOutBox("bob2", "bob omschrijving 2", new ArrayList<>(), new ArrayList<>(), new ArrayList<>())
                                }
                        )
                )
        );

        // SetterInjection mocks
        boxController.setBreakOutBoxRepo(boxRepo);
    }

    @Test
    public void getAllBobs_returnsAllBobs() {
        Assert.assertEquals(2, boxController.getAllBreakOutBoxen().size());
    }

    // <editor-fold desc="=== createBox ===" >
    @Test
    public void createBox_voegtNieuweBoxToe() {
        boxController.createBreakOutBox("bob3", "bob omschrijving 3", new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        Mockito.verify(boxRepo).insert(Mockito.any(BreakOutBox.class));
    }
    // </editor-fold>

    //<editor-fold desc="=== applyFilter ===">
    @Test
    public void applyFilter_GeenTextReturnsAll(){
        boxController.applyFilter("");
        Assert.assertEquals(2, boxController.getAllBreakOutBoxen().size());
    }
    
    @Test
    public void applyFilter_ReturnsMatches(){
        boxController.applyFilter("bob 1");
        Assert.assertEquals(1, boxController.getAllBreakOutBoxen().size());
    }
    
    @Test
    public void applyFilter_WithWhiteSpacesReturnsMatches(){
        boxController.applyFilter(" b o b 1");
        Assert.assertEquals(1, boxController.getAllBreakOutBoxen().size());
    }
    // </editor-fold>
}
