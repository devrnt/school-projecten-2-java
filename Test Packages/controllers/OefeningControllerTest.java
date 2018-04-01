package controllers;

import controllers.OefeningController;
import domein.Groepsbewerking;
import domein.Oefening;
import domein.OperatorEnum;
import exceptions.NotFoundException;
import java.util.ArrayList;
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
        List<String> omschrijvingen = new ArrayList<>();
        omschrijvingen.add("gbw1");        
        omschrijvingen.add("gbw2");
        Mockito.when(groepsbwRepo.getOmschrijvingen()).thenReturn(omschrijvingen);

        
        
        // setter injection mocks
        controller.setOefeningRepo(oefeningRepo);
        controller.setGroepsbewerkingRepo(groepsbwRepo);
    }

    /* === createOefening === */
    @Test
    public void createOefening_AddsNewOefening() {
        controller.createOefening("opgave", 0, "feedback", new ArrayList<>());
        Mockito.verify(oefeningRepo).insert(Mockito.any(Oefening.class));
    }

    /* === updateOefening === */
    @Test
    public void updateOefening_changesAndPersistsOefening() {
        controller.updateOefening(1, "opgave2", 0, "feedback", new ArrayList<>());
        Assert.assertEquals("opgave2", oefening.getOpgave());
        Mockito.verify(oefeningRepo).update(Mockito.any(Oefening.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateOefening_oefeningNotFound_throwsNotFoundException() {
        controller.updateOefening(2, "opgave2", 0, "feedback", new ArrayList<>());
    }

    /* === deleteOefening === */
    @Test
    public void deleteOefening_callsDeleteInRepo() {
        controller.deleteOefening(1);
        Mockito.verify(oefeningRepo).delete(Mockito.any(Oefening.class));
    }

    @Test(expected = NotFoundException.class)
    public void deleteOefening_oefeningNotFound_throwsNotFoundException() {
        controller.deleteOefening(2);
    }

    /* === getGroepsbewerkingen === */
    @Test
    public void getGroepsbewerkingen_returnsListOfGroepsbewerkingen() {
        ObservableList<Groepsbewerking> list = controller.getGroepsbewerkingen();
        Assert.assertEquals(2, list.size());
    }
}
