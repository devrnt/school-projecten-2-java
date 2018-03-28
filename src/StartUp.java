
import domein.DomeinController;


/**
 *
 * @author sam
 */
public class StartUp {
    public static void main(String[] args) {
        DomeinController controller = new DomeinController();
        controller.createOefening("oef", 0, "feed", null);
        controller.close();    
    }
}
