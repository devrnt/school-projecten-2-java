package exceptions;

import domein.RequiredElement;
import java.util.Collections;
import java.util.Set;

public class InformationRequiredException extends Exception{
    private Set<RequiredElement> informationRequired;
    
    public InformationRequiredException(String message, Set<RequiredElement> itemsRequired){
        super(message);
        informationRequired = itemsRequired;
    }
    
    public Set<RequiredElement> getInformationRequired(){
        return Collections.unmodifiableSet(informationRequired); 
    }
}