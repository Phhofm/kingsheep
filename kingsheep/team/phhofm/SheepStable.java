package kingsheep.team.phhofm;

import kingsheep.Simulator;
import kingsheep.Type;

public class SheepStable extends UzhShortNameCreature {

    //copied from greedy
    private boolean noMoreFoodAvailable = false;
    private Move lastMove;
    private int counter = 0;

    //own
    private Move move;

    //original
    public SheepStable(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }


    //this is the algorithm for alpha-beta search taken from the book
    //TODO adapt this pseudocode to real code and this scenario
    //how do states look like exactly? make own types? make min and max and utility function
    protected Move alphaBetaSearch(state){  //returns a move
        int v = maxValue(state,a,b);
        return m;                           //TODO return move that leads to state with value v
    }

    protected int maxValue(state,a,b){      //returns a utility value
        if (terminalTest){
            return utility(state);
        }
        int v = 0;
        foreach(action in ACTIONS(state)) do
            int v = MAX(int v, minValue(RESULT(state,action),a,b));
            if v>=b then return v;
            a = MAX(a,v);
        return v;
    }

    protected int minValue(state,a,b){      //returns a utility value
        if (terminalTest){
            return utility(state);
        }
        int v = 2;
        foreach(action in ACTIONS(state)) do
            int v = MIN(int v, maxValue(RESULT(state,action),a,b));
        if v>=a then return v;
        b = MIN(b,v);
        return v;
    }

    //copied from greedy
    private boolean isSquareSafe(Type map[][], Move move){
        int x, y;

        if (move == Move.UP){
            x = this.x;
            y = this.y - 1;
        }else if (move == Move.DOWN){
            x = this.x;
            y = this.y + 1;
        }else if (move == Move.LEFT){
            x = this.x - 1;
            y = this.y;
        }else{
            x = this.x + 1;
            y = this.y;
        }

        if (!isCoordinateValid(map, y, x)){
            return false;
        }

        Type type = map[y][x];

        if(type == Type.FENCE || type == Type.WOLF2 || type == Type.SHEEP2){
            return false;
        }
        return true;
    }

    private boolean isCoordinateValid(Type map[][], int y, int x){
        try{
            Type type = map[y][x];
        }catch (ArrayIndexOutOfBoundsException e){
            return false;
        }
        return true;
    }

    private void fleeFromBadWolf(Type map[][]){

        if (isSquareSafe(map, lastMove)){
            move = lastMove;
            return;
        }

        if (isSquareSafe(map, Move.DOWN)){
            move = Move.DOWN;
        }else if (isSquareSafe(map, Move.RIGHT)){
            move = Move.RIGHT;
        }else if (isSquareSafe(map, Move.UP)){
            move = Move.UP;
        }else{
            move = Move.LEFT;
        }
    }

    //original
    protected void think(Type map[][]) {
		/*
		TODO
		YOUR SHEEP CODE HERE
		
		BASE YOUR LOGIC ON THE INFORMATION FROM THE ARGUMENT map[][]
		
		YOUR CODE NEED TO BE DETERMINISTIC. 
		THAT MEANS, GIVEN A DETERMINISTIC OPPONENT AND MAP THE ACTIONS OF YOUR SHEEP HAVE TO BE REPRODUCIBLE
		
		SET THE MOVE VARIABLE TO ONE TOF THE 5 VALUES
        move = Move.UP;
        move = Move.DOWN;
        move = Move.LEFT;
        move = Move.RIGHT;
        move = Move.WAIT;
		*/
        //move = Move.WAIT; //provided by exercise

        //copied from greedy
        if(alive && !noMoreFoodAvailable){
            char[] objectives = new char[2];
            objectives[0] = 'r';
            objectives[1] = 'g';

            move = getGreedyAction(map, objectives);
            if (move == null){
                move = Move.WAIT;
            }

            if(move == Move.WAIT){
                noMoreFoodAvailable = true;
                fleeFromBadWolf(map);
            }

        }else{
            //focusing on escaping the wolf
            fleeFromBadWolf(map);
        }
        lastMove = move;

        ++counter;
    }
}