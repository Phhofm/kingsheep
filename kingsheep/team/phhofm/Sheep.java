package kingsheep.team.phhofm;

import kingsheep.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Sheep extends UzhShortNameCreature {

    private HashMap<String, Square> mapWithValues;
    private ArrayList<Square> squareInitializeExpandQueue;

    //original
    public Sheep(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
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
        move = Move.LEFT; //provided by exercise
        //System.out.println(map[3][9].name());  //prints out RHUBARB or EMPTY or SHEEP2 etc
        System.out.println("x "+ x);
        System.out.println("y "+ y);
        System.out.println(map[y][x].name());   //position of sheep is y,x
        int x = -999;
        System.out.println(x);

        //each expansion costs -1. or add a int to the square how far away from the sheep it is. that is g (costs)  (A* search)
        //each sqare has an int for its value (h) so grass=1 rhubarb = 5 fence = -999 enemy wolf and next to enemy wolf also. enemy sheep also.
        //expand always the node with the lowest cost globally. after specific time amount stop. choose the longest expanded branch. so furthest away reached square with lowest global cost
        //expand the already existing map by those values.

        //make data structure for Squares with values yCoor,xCoor,cost,value,totalValue.
        //Make datastructure like in greedy. Hashmap with string and Square type. The string is the Coordinate (yCoor_xCoor).
        //In the Quare type, store yCoor, xCoor, distance, value, totalValue.

        class Square {
            private Type type;
            private int yCoor, xCoor, distance, value, totalValue;
            private boolean visited = false;

            protected Square(Type type, int xCoor, int yCoor, int distance, int value, int totalValue) {
                this.type = type;
                this.xCoor = xCoor;
                this.yCoor = yCoor;
            }
        }

        //Initialize map squares with distance costs up to 10
        //Look at squares left right up and down of sheep (access coordinates)
        //If is type empty, give total value -1. put in the move-arraylist of that square how you got there (example: Left)
        //expand the node that has the highest distance and the lowest totalValue. Also in 1 step accessible, so left, right, up, down. You temporarily subtract from the total value of each square its own value,
        //add 1 to this total value, and if the total value is smaller for this square (or if it was empty before), you write (or overwrite) the total value and how you got there (LU -> Left Up).
        //mark the ones that got already expanded to not cycle infinitely. Except their totalvalue changes, then unmark them.



        /*initialize map with distances up to 10 */
        //create sheep-root Square
        Square sheep = new Square(type,x,y,0,0,0);
        //expand: Create neighbor squares. Add values and distances (+1 or root square distance). If they are not outside the board, and dont exist yet in the hashmap, add them to the hashmap and to the toExpand Queue (except distance is >10.

    private void addNotVisitedAccessibleNeighbourSquaresToQueue(Square origin, int xPos, int yPos) {
        //Add all valid neighbour Squares
        try {
            addSquareToQueueIfAccessible(new Square(map[yPos - 1][xPos], origin.getXCoordinate(), origin.getYCoordinate() - 1, objective, Move.UP, this));
            addSquareToQueueIfAccessible(new Square(map[yPos + 1][xPos], origin.getXCoordinate(), origin.getYCoordinate() + 1, objective, Move.DOWN, this));
            addSquareToQueueIfAccessible(new Square(map[yPos][xPos - 1], origin.getXCoordinate() - 1, origin.getYCoordinate(), objective, Move.LEFT, this));
            addSquareToQueueIfAccessible(new Square(map[yPos][xPos + 1], origin.getXCoordinate() + 1, origin.getYCoordinate(), objective, Move.RIGHT, this));
        } catch (ArrayIndexOutOfBoundsException e) {
            //do not add square since it is outside of the play board
        }
    }


    }
}