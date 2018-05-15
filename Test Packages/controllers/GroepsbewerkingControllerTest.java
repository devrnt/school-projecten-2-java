/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import domein.Groepsbewerking;
import domein.GroepsbewerkingBeheer;
import domein.OperatorEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.GenericDao;
import repository.GenericDaoJpa;
import repository.GroepsbewerkingDao;

/**
 *
 * @author Yanis
 */
public class GroepsbewerkingControllerTest {

    private GroepsbewerkingController controller;
    private GroepsbewerkingDao groepsbwRepo;
    private Groepsbewerking groepsbewerking;

    @Before
    public void before() {
        // aanmaken controller
        controller = new GroepsbewerkingController();

        // aanmaken mocks
        groepsbwRepo = Mockito.mock(GroepsbewerkingDao.class);

        // trainen mocks
        groepsbewerking = new Groepsbewerking("omschrijving", 2, OperatorEnum.vermeningvuldigen);
        Mockito.when(groepsbwRepo.get(1)).thenReturn(groepsbewerking);
        Mockito.when(groepsbwRepo.get(2)).thenReturn(null);
        Mockito.when(groepsbwRepo.findAll()).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new Groepsbewerking[]{
                            new Groepsbewerking("omschrijving1", 2, OperatorEnum.vermeningvuldigen),
                            new Groepsbewerking("omschrijving2", 2, OperatorEnum.vermeningvuldigen)

                        }
                )
                )
        );
        // setter injection mocks        

        controller.getGroepsbewerkingBeheer().setGroepsbewerkingRepo(groepsbwRepo);
    }

    //<editor-fold defaultstate="collapsed" desc="=== createGroepsbewerking ===">
    @Test
    public void createGroepsbewerking_AddsNewGroepsbewerking() {
        controller.createGroepsbewerking("omschrijving", 2, OperatorEnum.vermeningvuldigen);
        Mockito.verify(groepsbwRepo).insert(Mockito.any(Groepsbewerking.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== updateGroepsbewerking ===">
    @Test
    public void updateGroepsbewerking_changesAndPersistsGroepsbewerking() {
        controller.updateGroepsbewerking(1, "updatedOmschrijving", 0, OperatorEnum.optellen);
        Assert.assertEquals("updatedOmschrijving", groepsbewerking.getOmschrijving());
        Mockito.verify(groepsbwRepo).update(Mockito.any(Groepsbewerking.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateGroepsbewerking_groepsbewerkingNotFound_throwsNotFoundException() {
        controller.updateGroepsbewerking(2, "updatedOmschrijvingError", 0, OperatorEnum.optellen);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== deleteGroepsbewerking ===">
    @Test
    public void deleteGroepsbewerking_callsDeleteInRepo() {
        controller.deleteGroepsbewerking(1);
        Mockito.verify(groepsbwRepo).delete(Mockito.any(Groepsbewerking.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteGroepsbewerking_GroepsbewerkingNotFound_throwsNotFoundException() {
        controller.deleteGroepsbewerking(2);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getGroepsbewerkingen ===">
    @Test
    public void getGroepsbewerkingen_returnsListOfGroepsbewerkingen() {
        ObservableList<Groepsbewerking> list = controller.getAllGroepsbewerking();
        Assert.assertEquals(2, list.size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getGroepsbewerking ===">
    @Test
    public void getGroepsbewerking_ReturnsGroepsbewerkingen() {
        Assert.assertEquals(2, controller.getAllGroepsbewerking().size());
    }

    @Test
    public void getgroepsbewerking_returnsCorrectgroepsbewerking() {
        Groepsbewerking groepsbewerking = controller.getGroepsbewerking(1);
        Assert.assertEquals(groepsbewerking, this.groepsbewerking);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== applyFilter ===">
    @Test
    public void applyFilter_NoTextReturnsAll() {
        controller.applyFilter("");
        Assert.assertEquals(2, controller.getAllGroepsbewerking().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        controller.applyFilter("omschrijving1");
        Assert.assertEquals(1, controller.getAllGroepsbewerking().size());
    }

    @Test
    public void applyFilter_WithWhiteSpaceReturnsMatches() {
        controller.applyFilter("     omschrijving1    ");
        Assert.assertEquals(1, controller.getAllGroepsbewerking().size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getOeGroepsbewerking ===">
    @Test
    public void getGroepsbewerkingBeheer_returnsInstanceOfGroepsbewerkingBeheer() {
        Assert.assertTrue(controller.getGroepsbewerkingBeheer() instanceof GroepsbewerkingBeheer);
    }
    //</editor-fold>
}
