/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

/**
 *
 * @author devri
 */
public class YouTiels {

    public static String cutSentence(String zin) {
        int maxLength = 18;
        if (zin.trim().length() > maxLength) {
            String cut = zin.trim().substring(0, maxLength);
            String dot = String.format("%s...", cut);
            return dot;
        }
        return zin;
    }
}
