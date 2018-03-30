package domein;

import java.util.ArrayList;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author sam
 */
public class OefeningTest {
    private final Oefening oefening;

    public OefeningTest() {
        oefening = new Oefening("opgave", 1, "feedback", new ArrayList<>());
    }
      
    @Test(expected = IllegalArgumentException.class)
    public void OpgaveNull_ThrowsArgumentException(){
        oefening.setOpgave(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void OpgaveLeeg_ThrowsArgumentException(){
        oefening.setOpgave("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void OpgaveSpaties_ThrowsArgumentException(){
        oefening.setOpgave("             ");
    }
    
    public void OpgaveGeldig_SetsOpgave(){
        String opgave2 = "opgave2";
        oefening.setOpgave(opgave2);
        assertEquals(opgave2, oefening.getOpgave());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void FeedbackNull_ThrowsArgumentException(){
        oefening.setFeedback(null);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void FeedbackLeeg_ThrowsArgumentException(){
        oefening.setFeedback("");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void FeedbackSpaties_ThrowsArgumentException(){
        oefening.setFeedback("             ");
    }
    
    public void FeedbackGeldig_SetsOpgave(){
        String feedback2 = "feedback2";
        oefening.setFeedback(feedback2);
        assertEquals(feedback2, oefening.getFeedback());
    }
    
    

}
