package domein;

import exceptions.InformationRequiredException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author sam
 */
public class OefeningBuilder {
    private Oefening oefening;
    private Set requiredElements;
    
    public void createOefening(){
        this.oefening = new Oefening();
    }
    
    public void buildOpgave(String opgave){
        oefening.setOpgave(opgave);
    }
    
    public void buildAntwoord(String antwoord){
        if (antwoord == null || antwoord.trim().length() == 0)
            oefening.setAntwoord(Integer.MIN_VALUE);
        else {
            try {
                oefening.setAntwoord(Integer.parseInt(antwoord));
            } catch (NumberFormatException e){
                oefening.setAntwoord(Integer.MIN_VALUE);
            }
        }
    }
    
    public void buildFeedback(String feedback){
        oefening.setFeedback(feedback);
    }
    
    public void buildGroepsbewerkingen(List<Groepsbewerking> groepsbewerkingen){
        oefening.setGroepsbewerkingen(groepsbewerkingen);
    }
    
    public Oefening getOefening() throws InformationRequiredException {
        requiredElements  = new TreeSet();
        if (oefening.getOpgave() == null || oefening.getOpgave().trim().length() == 0){
            requiredElements.add(RequiredElement.OpgaveRequired);
        }
        return oefening;
    }

    
}
