package kingsheep.team.phhofm;

import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public abstract class UzhShortNameCreature extends Creature {

    //original from exercise
    public UzhShortNameCreature(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }
    //original from exercise
    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your phhofm. That way you can stay anonymous on the ranking list.
        return "Mormon";
    }
}
