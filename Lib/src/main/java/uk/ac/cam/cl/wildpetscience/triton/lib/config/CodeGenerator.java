package uk.ac.cam.cl.wildpetscience.triton.lib.config;

import java.util.Random;

public class CodeGenerator {

    // Hacky but saves the work of reading all the words in from a file.
    // This is just the 100 most common nouns in English.
    private static String[] words = {"time","year","people","way","man","day","thing","child","mr","government","work","life","woman","system","case","part","group","number","world","house","area","company","problem","service","place","hand","party","school","country","point","week","member","end","state","word","family","fact","head","month","side","business","night","eye","home","question","information","power","change","interest","development","money","book","water","other","form","room","level","car","council","policy","market","court","effect","result","idea","use","study","job","name","body","report","line","law","face","friend","authority","road","minister","rate","door","hour","office","right","war","mother","person","reason","view","term","period","centre","figure","society","police","city","need","community","million","kind","price"};

    public String nextCode() {
        final StringBuilder sb = new StringBuilder();
        Random r = new Random();
        r.ints(0, 100)
                .limit(3)
                .forEach(i -> sb.append(words[i]).append(' '));
        return sb.toString().substring(0, sb.length() - 1); // remove trailing space
    }

}
