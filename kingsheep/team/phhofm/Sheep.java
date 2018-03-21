package kingsheep.team.phhofm;

import kingsheep.*;

import java.util.HashMap;

public class Sheep extends UzhShortNameCreature {

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

        //Create type square. Add a int for the value of the square
        //each expansion costs -1. or add a int to the square how far away from the sheep it is. that is g (costs)  (A* search)
        //each sqare has an int for its value (h) so grass=1 rhubarb = 5 fence = -999 enemy wolf and next to enemy wolf also. enemy sheep also.
        //expand always the node with the lowest cost globally. after specific time amount stop. choose the longest expanded branch. so furthest away reached square with lowest global cost
        //expand the already existing map by those values.


        //make data structure for Squares with values yCoor,xCoor,cost,value,totalValue.
        //Make datastructure like in greedy. Hashmap with string and Square type. The string is the Coordinate (yCoor_xCoor).
        //In the Quare type, store yCoor, xCoor, cost, value, totalValue.

        class Square {
            private Type type;
            private int yCoor, xCoor, cost, value, totalValue;

            protected Square(Type type, int xCoor, int yCoor, int cost, int value, int totalValue) {
                this.type = type;
                this.xCoor = xCoor;
                this.yCoor = yCoor;
            }
        }




    }
}