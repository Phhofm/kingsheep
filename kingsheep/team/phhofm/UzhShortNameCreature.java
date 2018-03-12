package kingsheep.team.phhofm;

import kingsheep.Creature;
import kingsheep.Simulator;
import kingsheep.Type;

/**
 * Created by kama on 04.03.16.
 */
public abstract class UzhShortNameCreature extends Creature {

    public UzhShortNameCreature(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    public String getNickname(){
        //TODO change this to any nickname you like. This should not be your phhofm. That way you can stay anonymous on the ranking list.
        return "Mormon";
    }
}
