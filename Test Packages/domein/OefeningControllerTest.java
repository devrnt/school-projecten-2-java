package domein;

import controllers.OefeningController;
import exceptions.NotFoundException;
import java.util.ArrayList;
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
    private Oefening oefening;

    @Before
    public void before() {
        oefening = new Oefening("opgave", 0, "feedback", new ArrayList<>());
        controller = new OefeningController();
        oefeningRepo = Mockito.mock(GenericDao.class);
        Mockito.when(oefeningRepo.get(1)).thenReturn(oefening);
        Mockito.when(oefeningRepo.get(2)).thenReturn(null);

        controller.setOefeningRepo(oefeningRepo);
    }

    /* === createOefening === */
    @Test
    public void createOefening_AddsNewOefening() {
        controller.createOefening("opgave", 0, "feedback", new int[]{});
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
}
