package domein;

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;
import org.mockito.Mockito;
import repository.GenericDao;

/**
 *
 * @author sam
 */
public class DomeinControllerTest {

    private DomeinController controller;
    private GenericDao<Oefening> oefeningRepo;

    @Before
    public void before() {
        controller = new DomeinController();
        oefeningRepo = Mockito.mock(GenericDao.class);
        controller.setOefeningRepo(oefeningRepo);
    }

    /* === createOefening === */

    @Test
    public void createOefening_AddsNewOefening() {
        controller.createOefening("opgave", 0, "feedbacj", null);
        Mockito.verify(oefeningRepo).insert(Mockito.any(Oefening.class));      
    }

}
