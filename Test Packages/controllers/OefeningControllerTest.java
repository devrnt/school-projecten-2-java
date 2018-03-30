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

/**
 *
 * @author sam
 */
public class OefeningControllerTest {

    private OefeningController controller;
    private GenericDao<Oefening> oefeningRepo;
    private GenericDao<Groepsbewerking> groepsbwRepo;

    private Oefening oefening;

    @Before
    public void before() {
        oefening = new Oefening("opgave", 0, "feedback", new ArrayList<>());
        controller = new OefeningController();
        oefeningRepo = Mockito.mock(GenericDao.class);
        groepsbwRepo = Mockito.mock(GenericDao.class);
        Mockito.when(oefeningRepo.get(1)).thenReturn(oefening);
        Mockito.when(oefeningRepo.get(2)).thenReturn(null);
        List<Groepsbewerking> groepsbwList = new ArrayList<>();
        groepsbwList.add(new Groepsbewerking("gbw", 2, OperatorEnum.optellen));
        Mockito.when(groepsbwRepo.findAll()).thenReturn(groepsbwList);

        controller.setOefeningRepo(oefeningRepo);
        controller.setGroepsbewerkingRepo(groepsbwRepo);
    }

    /* === createOefening === */
    @Test
    public void createOefening_AddsNewOefening() {
        controller.createOefening("opgave", 0, "feedback", null);
        Mockito.verify(oefeningRepo).insert(Mockito.any(Oefening.class));
    }

    /* === updateOefening === */
    @Test
    public void updateOefening_changesAndPersistsOefening() {
        controller.updateOefening(1, "opgave2", 0, "feedback", new int[]{});
        Assert.assertEquals("opgave2", oefening.getOpgave());
        Mockito.verify(oefeningRepo).update(Mockito.any(Oefening.class));
    }

    @Test(expected = NotFoundException.class)
    public void updateOefening_oefeningNotFound_throwsNotFoundException() {
        controller.updateOefening(2, "opgave2", 0, "feedback", new int[]{});
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
    public void getGroepsbewerkingen_returnsObservableListWithStrings() {
        ObservableList<String> list = controller.getGroepsbewerkingen();
        Assert.assertEquals("gbw", list.get(0));
    }
}
