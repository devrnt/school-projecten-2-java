package controllers;

import domein.Groepsbewerking;
import domein.Groep;
import domein.GroepBeheer;
import domein.OperatorEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
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
public class GroepControllerTest {

    private GroepController controller;
    private GenericDao<Groep> groepRepo;

    private Groep groep;

    @Before
    public void before() {
        // aanmaken controller
        controller = new GroepController();

        // aanmaken mocks
        groepRepo = Mockito.mock(GenericDao.class);

        // trainen mocks
        groep = new Groep("groep", new ArrayList<>());
        Mockito.when(groepRepo.get(1)).thenReturn(groep);
        Mockito.when(groepRepo.get(2)).thenReturn(null);
        Mockito.when(groepRepo.findAll()).thenReturn(
                new ArrayList<>(
                        Arrays.asList(
                                new Groep[]{
                                    new Groep("groep1", new ArrayList<>()),
                                    new Groep("groep2", new ArrayList<>())

                                }
                        )
                )
        );

        // setter injection mocks
        controller.getGroepBeheer().setGroepRepo(groepRepo);
    }

    //<editor-fold defaultstate="collapsed" desc="=== createGroep ===">
    @Test
    public void createGroep_AddsNewGroep() {
        controller.createGroep("eenGroep", new ArrayList<>());
        Mockito.verify(groepRepo).insert(Mockito.any(Groep.class));
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== updateGroep ===">
    @Test
    public void updateGroep_changesAndPersistsGroep() {
        controller.updateGroep(1, "groepUpdate", new ArrayList<>());
        Assert.assertEquals("groepUpdate", groep.getNaam());
        Mockito.verify(groepRepo).update(Mockito.any(Groep.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateGroep_groepNotFound_throwsNotFoundException() {
        controller.updateGroep(2, "groepNotFound", new ArrayList<>());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== deleteGroep ===">
    @Test
    public void deleteGroep_callsDeleteInRepo() {
        controller.deleteGroep(1);
        Mockito.verify(groepRepo).delete(Mockito.any(Groep.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteGroep_groepNotFound_throwsNotFoundException() {
        controller.deleteGroep(2);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getGroep ===">
    @Test
    public void getGroepen_ReturnsGroepen() {
        Assert.assertEquals(2, controller.getGroepen().size());
    }

    @Test
    public void getGroep_returnsCorrectGroep() {
        Groep groep = controller.getGroep(1);
        Assert.assertEquals(groep, this.groep);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== applyFilter ===">
    @Test
    public void applyFilter_NoTextReturnsAll() {
        controller.applyFilter("");
        Assert.assertEquals(2, controller.getGroepen().size());
    }

    @Test
    public void applyFilter_ReturnsMatches() {
        controller.applyFilter("groep1");
        Assert.assertEquals(1, controller.getGroepen().size());
    }

    @Test
    public void applyFilter_WithWhiteSpaceReturnsMatches() {
        controller.applyFilter("     groep1    ");
        Assert.assertEquals(1, controller.getGroepen().size());
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="=== getGroepBeheer ===">
    @Test
    public void getGroepBeheer_returnsGroepBeheer() {
        Assert.assertTrue(controller.getGroepBeheer() instanceof GroepBeheer);
    }
    //</editor-fold>

}
