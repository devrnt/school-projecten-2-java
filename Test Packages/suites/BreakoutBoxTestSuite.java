package suites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 *
 * @author sam
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    controllers.BoxControllerTest.class,
    controllers.OefeningControllerTest.class,
    controllers.SessieControllerTest.class,
    domein.KlasTest.class,
    domein.SessieTest.class
})
public class BreakoutBoxTestSuite {
    
}
