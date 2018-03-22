package kingsheep.team.phhofm;

import kingsheep.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class Sheep extends UzhShortNameCreature {

    private HashMap<String, Square> mapWithValues;
    private ArrayList<Square> squareInitializeExpandQueue;
    private Type mySheep;
    private Type enemySheep;
    private final int MAXDISTANCE = 5;
    ListIterator<Square> initializeIter;

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
        System.out.println("x " + this.x);
        System.out.println("y " + this.y);
        System.out.println(map[this.y][this.x].name());   //position of sheep is y,x
        System.out.println(map[14][18]); //this is the outmost square. no greater values or we have out of bounds


        //initialize used variables
        squareInitializeExpandQueue = new ArrayList<>();
        mapWithValues = new HashMap<>();

        //which sheep are we? Which is our enemy? Enemy needs to be set to -999 since we cannot move into it. But the Square where our sheep is in should be neutral since we can move off and on it again depending on the best path/moves.
        mySheep = map[this.y][this.x];

        if (mySheep.equals(Type.SHEEP1)) {
            enemySheep = Type.SHEEP2;
        } else {
            enemySheep = Type.SHEEP1;
        }

        System.out.println(mySheep);
        System.out.println(enemySheep);

        //Create root square where sheep is in for initialization
        Square sheep = new Square(this.type, this.x, this.y, 0, 0, 0);

        //add this root square to the expansion queue for initialization
        squareInitializeExpandQueue.add(sheep);


        initializeIter = squareInitializeExpandQueue.listIterator();

        while (initializeIter.hasNext()){
            Square square = initializeIter.next();
            initializeIter.remove();
            initializeMapWithValues(square,square.yCoor,square.xCoor,map);
            initializeIter = squareInitializeExpandQueue.listIterator();
        }

        System.out.println(initializeIter.hasNext());
        System.out.println(initializeIter);
        //initialize map till Maxdistance: give them distances to sheep and values
       //this will give a concurrentmodificationexception since we add/revome elements while iterating
        // for(Square square: squareInitializeExpandQueue){
      //   initializeMapWithValues(square,square.yCoor,square.xCoor,map);
      //  }

        System.out.println(mapWithValues);
        System.out.println("test");



        //short version: we initialize our own hashmap, where we create the squares and store the value and the distance to the sheep in the squares of the hashmap.
        //we then expand from the sheep, and each expansion the pathProfit of the new Square is originPathProfit-1+ValueOfNewSquare. we store this pathProfit in the new square, only if it did not exist yet or
        //if it is bigger. We also store in the square as arraylist the moves we need to get there.
        //at the end, we choose the Square with the highest distance and the highest PathProfit and execute its stored movementarray for two moves then recalculate everything since our sheeps position
        //and the other things will have changed.

        //each expansion costs -1. or add a int to the square how far away from the sheep it is. that is g (costs)  (A* search)
        //each sqare has an int for its value (h) so grass=1 rhubarb = 5 fence = -999 enemy wolf and next to enemy wolf also. enemy sheep also.
        //expand always the node with the lowest cost globally. after specific time amount stop. choose the longest expanded branch. so furthest away reached square with lowest global cost
        //expand the already existing map by those values.


        //Initialize map squares with distance costs up to 10
        //Look at squares left right up and down of sheep (access coordinates)
        //If is type empty, give total value -1. put in the move-arraylist of that square how you got there (example: Left)
        //expand the node that has the highest distance and the lowest pathProfit. Also in 1 step accessible, so left, right, up, down. You temporarily subtract from the total value of each square its own value,
        //add 1 to this total value, and if the total value is smaller for this square (or if it was empty before), you write (or overwrite) the total value and how you got there (LU -> Left Up).
        //mark the ones that got already expanded to not cycle infinitely. Except their pathProfit changes, then unmark them.



        /*initialize map with distances up to 10 from sheep. This will happen each time new since game states change. this would only need to happen every second time thought since this sheep moves two times. So counter if even use old strategy, if uneven make new. initilize so this makes sense.*/
        //create sheep-root Square
        //Square sheep = new Square(type, x, y, 0, 0, 0);
        //expand: Create neighbor squares. Add values and distances (+1 or root square distance). If they are not outside the board, and dont exist yet in the hashmap, add them to the hashmap and to the toExpand Queue (except distance is >10.



    }

    private void initializeMapWithValues(Square origin, int yPos, int xPos, Type map[][]) {
        //Add all valid neighbour Squares
        try {
            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.yCoor-1 < 0 || origin.distance > MAXDISTANCE) {
            } else {
                //checks if this exists already in the Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, -1, 0))) {
                } else {
                    //create square up and assign distance
                    Square upsquare = new Square(map[yPos - 1][xPos], origin.xCoor, origin.yCoor - 1, origin.distance + 1, 0, 0);
                    //assign value to upsquare
                    if (upsquare.type.equals(Type.EMPTY)) {
                        upsquare.value = 0;
                    } else if (upsquare.type.equals(Type.GRASS)) {
                        upsquare.value = 1;
                    } else if (upsquare.type.equals(Type.RHUBARB)) {
                        upsquare.value = 5;
                    } else if (upsquare.type.equals(Type.FENCE)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(Type.WOLF1)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(Type.WOLF2)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(enemySheep)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(mySheep)) {
                        upsquare.value = 0;
                    } else {
                        throw new java.lang.RuntimeException("could not determine type of upsquare to assign a value");
                    }
                    //add to hashmap
                    mapWithValues.put(getStringCoordinate(upsquare, 0, 0), upsquare);
                    //add to expandQueue
                    //squareInitializeExpandQueue.add(upsquare);
                    initializeIter.add(upsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.xCoor+1 > 18 || origin.distance > MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 0, 1))) {
                } else {
                    //TODO check if this exists already in the Hashmap
                    Square rightsquare = new Square(map[yPos][xPos + 1], origin.xCoor + 1, origin.yCoor, origin.distance + 1, 0, 0);
                    if (rightsquare.type.equals(Type.EMPTY)) {
                        rightsquare.value = 0;
                    } else if (rightsquare.type.equals(Type.GRASS)) {
                        rightsquare.value = 1;
                    } else if (rightsquare.type.equals(Type.RHUBARB)) {
                        rightsquare.value = 5;
                    } else if (rightsquare.type.equals(Type.FENCE)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(Type.WOLF1)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(Type.WOLF2)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(enemySheep)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(mySheep)) {
                        rightsquare.value = 0;
                    } else {
                        throw new java.lang.RuntimeException("could not determine type of rightsquare to assign a value");
                    }
                    //add to hashmap
                    mapWithValues.put(getStringCoordinate(rightsquare, 0, 0), rightsquare);
                    //add to expandQueue
                    //squareInitializeExpandQueue.add(rightsquare);
                    initializeIter.add(rightsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.xCoor-1 < 0 || origin.distance > MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 0, -1))) {
                } else {
                    Square leftsquare = new Square(map[yPos][xPos - 1], origin.xCoor - 1, origin.yCoor, origin.distance + 1, 0, 0);
                    if (leftsquare.type.equals(Type.EMPTY)) {
                        leftsquare.value = 0;
                    } else if (leftsquare.type.equals(Type.GRASS)) {
                        leftsquare.value = 1;
                    } else if (leftsquare.type.equals(Type.RHUBARB)) {
                        leftsquare.value = 5;
                    } else if (leftsquare.type.equals(Type.FENCE)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(Type.WOLF1)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(Type.WOLF2)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(enemySheep)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(mySheep)) {
                        leftsquare.value = 0;
                    } else {
                        throw new java.lang.RuntimeException("could not determine type of leftsquare to assign a value");
                    }

                    //add to hashmap
                    mapWithValues.put(getStringCoordinate(leftsquare, 0, 0), leftsquare);
                    //add to expandQueue
                    //squareInitializeExpandQueue.add(leftsquare);
                    initializeIter.add(leftsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.yCoor+1 > 14 || origin.distance > MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 1, 1))) {
                } else {
                    Square downsquare = new Square(map[yPos + 1][xPos], origin.xCoor, origin.yCoor + 1, origin.distance + 1, 0, 0);
                    if (downsquare.type.equals(Type.EMPTY)) {
                        downsquare.value = 0;
                    } else if (downsquare.type.equals(Type.GRASS)) {
                        downsquare.value = 1;
                    } else if (downsquare.type.equals(Type.RHUBARB)) {
                        downsquare.value = 5;
                    } else if (downsquare.type.equals(Type.FENCE)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(Type.WOLF1)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(Type.WOLF2)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(enemySheep)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(mySheep)) {
                        downsquare.value = 0;
                    } else {
                        throw new java.lang.RuntimeException("could not determine type of downsquare to assign a value");
                    }
                    //add to hashmap
                    mapWithValues.put(getStringCoordinate(downsquare, 0, 0), downsquare);
                    //add to expandQueue
                    //squareInitializeExpandQueue.add(downsquare);
                    initializeIter.add(downsquare);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //do not add square since it is outside of the play board
        }

        //add to the origin square that he has already been expanded


    }


    //at end when whole map is initialized, search if the enemy wolf appears. if yes, mark immediate neighbor squares as -999 value.



    //make data structure for Squares with values yCoor,xCoor,cost,value,pathProfit.
    //Make datastructure like in greedy. Hashmap with string and Square type. The string is the Coordinate (yCoor_xCoor).
    //In the Quare type, store yCoor, xCoor, distance, value, pathProfit.
    //this is used by the methods so it needs to be at the end otherwise in Java the methods will not recognize

    private class Square {
        private Type type;
        private int yCoor, xCoor, distance, value, pathProfit;

        private Square(Type type, int xCoor, int yCoor, int distance, int value, int pathProfit) {
            this.type = type;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
        }
    }
    protected String getStringCoordinate(Square square, int yShift, int xShift) {
        return Integer.toString(square.yCoor + yShift) + "_" + Integer.toString(square.xCoor + xShift);
    }
}