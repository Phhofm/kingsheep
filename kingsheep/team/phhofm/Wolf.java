package kingsheep.team.phhofm;

import kingsheep.*;

public class Wolf extends UzhShortNameCreature {

    public Wolf(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    protected void think(Type map[][]) {
		/*
		TODO
		YOUR WOLF CODE HERE
		
		BASE YOUR LOGIC ON THE INFORMATION FROM THE ARGUMENT map[][]
		
		YOUR CODE NEED TO BE DETERMINISTIC. 
		THAT MEANS, GIVEN A DETERMINISTIC OPPONENT AND MAP THE ACTIONS OF YOUR WOLF HAVE TO BE REPRODUCIBLE
		
		SET THE MOVE VARIABLE TO ONE TOF THE 5 VALUES
        move = Move.UP;
        move = Move.DOWN;
        move = Move.LEFT;
        move = Move.RIGHT;
        move = Move.WAIT;
		*/
        //move = Move.WAIT; //provided by exercise

        //code of awesome wolf
        int i;

        for(i=0;i<6;i++){
            if(i<3){
            move=Move.UP;}
            else {
                move = Move.LEFT;
            }
            }
        }
    }
