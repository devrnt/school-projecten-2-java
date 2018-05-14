package controllers;

import domein.Actie;
import domein.ActieBeheer;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.collections.ObservableList;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import repository.GenericDao;

public class ActieControllerTest {

    private ActieController controller;
    private GenericDao<Actie> actieRepo;
    private Actie actie;

    @Before
    public void before() {
        // aanmaken controller
        controller = new ActieController();

        // aanmaken mocks
        actieRepo = Mockito.mock(GenericDao.class);

        // trainen mocks
        actie = new Actie("Actie");
        Mockito.when(actieRepo.get(1)).thenReturn(actie);
        Mockito.when(actieRepo.get(2)).thenReturn(null);
        Mockito.when(actieRepo.findAll()).thenReturn(
                new ArrayList<>(Arrays.asList(
                        new Actie[]{
                            new Actie("Actie1"),
                            new Actie("Actie2")
                        }
                )
                )
        );
        // setter injection mocks        

        controller.getActieBeheer().setActieRepo(actieRepo);
    }

    //<editor-fold defaultstate="collapsed" desc="=== createActie ===">
    @Test
    public void createActie_AddsNewActie() {
        controller.createActie("Actie");
        Mockito.verify(actieRepo).insert(Mockito.any(Actie.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== updateActie ===">
    @Test
    public void updateActie_changesAndPersistsActie() {
        controller.updateActie(1, "updatedActie");
        Assert.assertEquals("updatedActie", actie.getOmschrijving());
        Mockito.verify(actieRepo).update(Mockito.any(Actie.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateActie_actieNotFound_throwsNotFoundException() {
        controller.updateActie(2, "updatedActie");
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== deleteActie ===">
    @Test
    public void deleteActie_callsDeleteInRepo() {
        controller.deleteActie(1);
        Mockito.verify(actieRepo).delete(Mockito.any(Actie.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteActie_AatieNotFound_throwsNotFoundException() {
        controller.deleteActie(2);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getActie ===">
    @Test
    public void getActies_returnsListOfActies() {
        ObservableList<Actie> list = controller.getAllActies();
        Assert.assertEquals(2, list.size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getActie ===">
    @Test
    public void getActies_ReturnsActies() {
        Assert.assertEquals(2, controller.getAllActies().size());
    }

    @Test
    public void getActie_returnsCorrectActie() {
        Actie actie = controller.getActie(1);
        Assert.assertEquals(actie, this.actie);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== applyFilter ===">
    @Test
    public void applyFilter_NoTextReturnsAll() {
        controller.applyFilter("");
        Assert.assertEquals(2, controller.getAllActies().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        controller.applyFilter("actie1");
        Assert.assertEquals(1, controller.getAllActies().size());
    }

    @Test
    public void applyFilter_WithWhiteSpaceReturnsMatches() {
        controller.applyFilter("     actie1    ");
        Assert.assertEquals(1, controller.getAllActies().size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getActie ===">
    @Test
    public void getActieBeheer_returnsInstanceOfActieBeheer() {
        Assert.assertTrue(controller.getActieBeheer() instanceof ActieBeheer);
    }
    //</editor-fold>
}
