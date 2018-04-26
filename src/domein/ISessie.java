/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domein;

import java.util.Date;

/**
 *
 * @author devri
 */
public interface ISessie {
    void setDatum(Date datum);
    void setSoortOnderwijs(SoortOnderwijsEnum soortOnderwijs);
}
