package controllers;

import controllers.OefeningController;
import domein.Groepsbewerking;
import domein.Oefening;
import domein.OefeningBeheer;
import domein.OperatorEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.collections.ObservableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import repository.GenericDao;
import repository.GroepsbewerkingDao;

/**
 *
 * @author sam
 */
public class OefeningControllerTest {

    private OefeningController controller;
    private GenericDao<Oefening> oefeningRepo;
    private GroepsbewerkingDao groepsbwRepo;

    private Oefening oefening;

    @Before
    public void before() {
        // aanmaken controller
        controller = new OefeningController();

        // aanmaken mocks
        oefeningRepo = Mockito.mock(GenericDao.class);
        groepsbwRepo = Mockito.mock(GroepsbewerkingDao.class);

        // trainen mocks
        oefening = new Oefening("opgave", 0, "feedback", new ArrayList<>());
        Mockito.when(oefeningRepo.get(1)).thenReturn(oefening);
        Mockito.when(oefeningRepo.get(2)).thenReturn(null);
        Mockito.when(oefeningRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new Oefening[]{
                                    new Oefening("opgave1", 1, "feedback1", new ArrayList<>()),
                                    new Oefening("opgave2", 1, "feedback2", new ArrayList<>())

                                }
                        )
                )
        );
        Mockito.when(groepsbwRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new Groepsbewerking[]{
                                    new Groepsbewerking("gbw1", 0, OperatorEnum.optellen),
                                    new Groepsbewerking("gbw2", 1, OperatorEnum.aftrekken)

                                }
                        )
                )
        );

        // setter injection mocks
        controller.getOefeningBeheer().setOefeningRepo(oefeningRepo);
        controller.getOefeningBeheer().setGroepsbewerkingRepo(groepsbwRepo);
    }

    //<editor-fold defaultstate="collapsed" desc="=== createOefening ===">
    @Test
    public void createOefening_AddsNewOefening() {
        controller.createOefening("opgave", 0, "feedback", new ArrayList<>());
        Mockito.verify(oefeningRepo).insert(Mockito.any(Oefening.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== updateOefening ===">
    @Test
    public void updateOefening_changesAndPersistsOefening() {
        controller.updateOefening(1, "opgaveUpdate", 0, "feedback", new ArrayList<>());
        Assert.assertEquals("opgaveUpdate", oefening.getOpgave());
        Mockito.verify(oefeningRepo).update(Mockito.any(Oefening.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateOefening_oefeningNotFound_throwsNotFoundException() {
        controller.updateOefening(2, "opgave2", 0, "feedback", new ArrayList<>());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== deleteOefening ===">
    @Test
    public void deleteOefening_callsDeleteInRepo() {
        controller.deleteOefening(1);
        Mockito.verify(oefeningRepo).delete(Mockito.any(Oefening.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteOefening_oefeningNotFound_throwsNotFoundException() {
        controller.deleteOefening(2);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getGroepsbewerkingen ===">
    @Test
    public void getGroepsbewerkingen_returnsListOfGroepsbewerkingen() {
        ObservableList<Groepsbewerking> list = controller.getGroepsbewerkingen();
        Assert.assertEquals(2, list.size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getOefening ===">
    @Test
    public void getOefeningen_ReturnsOefeningen() {
        Assert.assertEquals(2, controller.getOefeningen().size());
    }

    @Test
    public void getOefening_returnsCorrectOefening() {
        Oefening oefening = controller.getOefening(1);
        Assert.assertEquals(oefening, this.oefening);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== applyFilter ===">
    @Test
    public void applyFilter_NoTextReturnsAll() {
        controller.applyFilter("");
        Assert.assertEquals(2, controller.getOefeningen().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        controller.applyFilter("opgave1");
        Assert.assertEquals(1, controller.getOefeningen().size());
    }

    @Test
    public void applyFilter_WithWhiteSpaceReturnsMatches() {
        controller.applyFilter("opgave 1");
        Assert.assertEquals(1, controller.getOefeningen().size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getOefeningBeheer ===">
    @Test
    public void getOefeningBeheer_returnsOefeningBeheer() {
        Assert.assertTrue(controller.getOefeningBeheer() instanceof OefeningBeheer);
    }
    //</editor-fold>

}
