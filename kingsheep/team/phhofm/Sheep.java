package kingsheep.team.phhofm;

import kingsheep.*;

import java.util.*;

import static java.lang.System.*;

public class Sheep extends UzhShortNameCreature {

    private HashMap<String, Square> mapWithValues;  //our hashmap for extended map, meaning squares with values
    private ArrayList<Square> squareInitializeExpandQueue;  //in initialization for expanding the not yet expanded squares. this changes during iteration, no fixed value
    private Type mySheep;
    private Type myWolf;
    private Type enemySheep;
    private Type enemyWolf;
    private final int MAXDISTANCE = 4;  //the distance to the sheep - squares that will be tests. this is mainly a performance influencing parameter
    ListIterator<Square> initializeIter;    //we need this to on the fly change the arraylist squateInitializeExpandQueue withut Java throwing an error
    private ArrayList<Square> allInitializedSquares;    //so we can search thorugh the initilized squares for the enemy wolf and give adjustent squares value -999
    boolean thereIsAValue;
    ArrayList<Square> suqaresToExpandInSearchForPath;
    Square maxPathProfitSquare;
    int maxPathProfit;
    Square goalSquare;
    int goalSquarePathProfitDistance;
    ArrayList<Move> noMoves = new ArrayList<Move>();
    Square enemyWolfSquare;
    Boolean thereIsEnemyWolf;
    Square mySheepSquare;

    //original
    public Sheep(Type type, Simulator parent, int playerID, int x, int y) {
        super(type, parent, playerID, x, y);
    }

    //original
    protected void think(Type map[][]) {
        //initialize variables
        squareInitializeExpandQueue = new ArrayList<>();    //queue with squares we can expand for the initialization (squares added to mapWithValues but have no pathprofit yet (=999)
        allInitializedSquares = new ArrayList<>();          //arraylist with all initialized squares to maxdistance
        mapWithValues = new HashMap<>();                    //extended map with additional values that i need
        thereIsAValue = false;                              //check if there is food. otherwise just escape wolf
        suqaresToExpandInSearchForPath = new ArrayList<>(); //this is a queue which squares can be extended for pathProfit (need to be initialized and have their own pathprofit already assigned
        maxPathProfit = -999999999;                         //this is super low so we can find the desitred square at the end
        thereIsEnemyWolf = false;

        //which sheep are we? Which is our enemy? Enemy needs to be set to -999 since we cannot move into it. But the Square where our sheep is in should be neutral since we can move off and on it again depending on the best path/moves.
        mySheep = map[this.y][this.x];

        if (mySheep.equals(Type.SHEEP1)) {
            myWolf = Type.WOLF1;
            enemySheep = Type.SHEEP2;
            enemyWolf = Type.WOLF2;
        } else {
            myWolf = Type.WOLF2;
            enemySheep = Type.SHEEP1;
            enemyWolf = Type.WOLF1;
        }

        //Create root square where sheep is in for initialization
        Square sheep = new Square(this.type, this.x, this.y, 0, 0, 0, false, noMoves);

        mySheepSquare = sheep;

        //TODO WHYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY
        //set this as initial goalSquare
        goalSquare = sheep;

        //add this root square to the expansion queue for initialization
        squareInitializeExpandQueue.add(sheep);

        //we can already insert this root sheep into the hashmap
        mapWithValues.put(getStringCoordinate(sheep, 0, 0), sheep);

        //TODO DO WE NEED THIS SEPARATE ARRAYLIST
        //add also for later searching for enemy wolf in initialized squares
        allInitializedSquares.add(sheep);

        //make iterator since we modify the arraylist while traversing it. we would get a concurrentModificationException otherwise
        initializeIter = squareInitializeExpandQueue.listIterator();

        //initialize hashmap until maxdistance. give every square the distance and the value according to type. fill in standard pathprofit since not known yet
        while (initializeIter.hasNext()) {
            //take next square and temp store it
            Square square = initializeIter.next();
            //remove this square from the iterator since we are processing it now
            initializeIter.remove();
            //initialization method
            initializeMapWithValues(square, square.yCoor, square.xCoor, map);
            //assign new before we iterate over again
            initializeIter = squareInitializeExpandQueue.listIterator();
        }

        //search for enemy wolf and give adjustent squares the value of -999 so we will never move right next to the enemy wolf to be eaten. Be eaten is the WORST CASE. This method has been tested with myWolf.
        if (thereIsEnemyWolf) {
            //give adjustent squares value -999 if they exist
            //up
            if (mapWithValues.containsKey(getStringCoordinate(enemyWolfSquare, -1, 0))) {
                mapWithValues.get(getStringCoordinate(enemyWolfSquare, -1, 0)).value = -999;
            }
            //left
            if (mapWithValues.containsKey(getStringCoordinate(enemyWolfSquare, 0, -1))) {
                mapWithValues.get(getStringCoordinate(enemyWolfSquare, 0, -1)).value = -999;
            }
            //right
            if (mapWithValues.containsKey(getStringCoordinate(enemyWolfSquare, 0, 1))) {
                mapWithValues.get(getStringCoordinate(enemyWolfSquare, 0, 1)).value = -999;
            }
            //down
            if (mapWithValues.containsKey(getStringCoordinate(enemyWolfSquare, 1, 0))) {
                mapWithValues.get(getStringCoordinate(enemyWolfSquare, 1, 0)).value = -999;
            }
        }

        //now the search path
        //search for the square with the highest distance value and the highest pathProfit (carful: not all squares have pathProfits yet since we are assigning them in this iteration.
        //search first for the squares with existing pathProfit
        //from them, choose the one with the highest distance and the highest pathProfit. choose what happens if there are multiple that have same (random or systematic continuing?)
        //assign the neighboring suqares a pathProfit
        //rinse and repeat (the rinse here is a joke. just repeat)
        //should we stop once we reach a max distance square? since it always chose the one with the highest profit
        //set expandedForSearchPath true if already expanded, overwrite to false if that square's pathProfit gets overwritten since we found a better path to that square and it will change adjustent squares
        //expand from sheep always square with highest pathProfit unless already expanded


        //ok. so expand from the root always the one with the highest pathProfit. set flag if expanded to true. if pathValue of a square changes change the flag to false since we found another way.
        //when expand store in the move array of that square. append to the movearray of the original square. new pathvalue is originalsquarepathvalue-1+valueofnewsquare.
        //take the allInitializedSquares Array. Iterate and search who has pathValue. then scan for the ones who are not expanded yet. if all are valued and expanded you are done.
        //our goals is then to find the square with the highest pathvalue and highest distance (so use addition. distance+pathvprofit) and then execute the movementarray

        //first step: search in array for the ones that have a pathprofit
        //kick out those that were already expanded
        //take the one remaining with the highest pathprofit and expand

        //if there is no value greater than 0 in the initilaized squares array, it doesnt matter which path we take. we then need to extend the initialization and just go to the objective since lots of time passed till we found one
        //if we find no value greater than 0, we just flee the enemy wolf
        //iterating thorugh arraylist is faster than hashmap
        for (Square square : allInitializedSquares) {
            if (square.value > 0) {
                thereIsAValue = true;
                break;
            }
        }

        //tested till here
        //ps we always get eaten before there is no value anymore
        if (!thereIsAValue) {
            out.println("Sheep found no value");
            if (thereIsEnemyWolf) {
                if (enemyWolfSquare.yCoor > mySheepSquare.yCoor && mySheepSquare.yCoor > 0 && mapWithValues.get(getStringCoordinate(mySheepSquare,-1,0)).value!=-999) {
                    move = Move.UP;
                } else if(enemyWolfSquare.yCoor < mySheepSquare.yCoor && mySheepSquare.yCoor < 14 && mapWithValues.get(getStringCoordinate(mySheepSquare,+1,0)).value!=-999) {
                    move=Move.DOWN;
                } else if (enemyWolfSquare.xCoor > mySheepSquare.xCoor && mySheepSquare.xCoor > 0 && mapWithValues.get(getStringCoordinate(mySheepSquare,0,-1)).value!=-999) {
                    move=Move.LEFT;
                } else if (enemyWolfSquare.xCoor < mySheepSquare.xCoor && mySheepSquare.xCoor < 18 && mapWithValues.get(getStringCoordinate(mySheepSquare,0,+1)).value!=-999) {
                    move=Move.RIGHT;
                }
            }
        } else {
            for (int i = 0; i < 2; i++) {
                pathing();
            }

            //rinse and repeat

            //find the square with the highest pathProfit and the highest distance
            Set<String> keys = mapWithValues.keySet();
            for (String key : keys) {
                Square square = mapWithValues.get(key);
                //if(square.pathProfit>=goalSquare.pathProfit && square.distance >= ) //remember sheep square has pathProfit 0 and from then on each step invokes a penalty. So sheep square is likely to have highest global pathProfit and that would mean our sheep stays in place, this is not desired behavior.
                if (square.pathProfit == 999) {
                    //pathprofit was not processed yet of this square so ignore otherwise we will get some arbitrary not processed square
                } else {
                    int currentPathProfitDistanceValue = square.pathProfit + square.distance;
                    if (currentPathProfitDistanceValue >= goalSquarePathProfitDistance) {
                        goalSquare = square;
                        goalSquarePathProfitDistance = goalSquare.pathProfit + goalSquare.distance;
                    }
                }
            }


            //execute the MOVES. "YOU GOT THE MOVES LIKE JAGGER. YOU GOT THE MOVES LIKE JAGGER. YOU GOT THE MOOOOOOOOOOOVES LIKE JAGGER." *sing and dance* I think i am sitting way to long at this exercise. Its friday evening, 21:40, the Lichthof is almost empty. Why am i doing masters? I think pretty much everyone is a better student than me. This code is so inefficient and complicated.
            //moveLikeJagger(goalSquare);

            if (goalSquare.sheepGotTheMovesLikeJagger.isEmpty()) {
                move = Move.WAIT;
            } else {
                move = goalSquare.sheepGotTheMovesLikeJagger.get(0);
            }
        }


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

    private void pathing() {
        //build the arraylist of squares to expand. we want to expand those that have a valid assigned pathProfit (not 999) and have not been expanded yet / where the flag is false (when they get overwritten in the process we change the flag again see other comments in this file haha :P so much what am i doing
        for (Square square : allInitializedSquares) {
            if (square.pathProfit != 999 && !square.expandedForSearchPath) {
                suqaresToExpandInSearchForPath.add(square);
            }
        }

        //we need to reset maxPathProfit
        maxPathProfit = -99999999;

        //find the square with the highest pathprofit to expand
        for (Square square : suqaresToExpandInSearchForPath) {
            if (square.pathProfit >= maxPathProfit) {
                maxPathProfitSquare = square;
                maxPathProfit = square.pathProfit;
            }
        }

        //expand this highest pathProfit Square. first upsqaure.
        if (mapWithValues.containsKey(getStringCoordinate(maxPathProfitSquare, -1, 0))) {
            //get the upsquare
            Square square = mapWithValues.get(getStringCoordinate(maxPathProfitSquare, -1, 0));
            //assign the pathprofit to the upsquare
            if(square.pathProfit == 999 || maxPathProfitSquare.pathProfit - 1 + square.value > square.pathProfit) {
                square.pathProfit = maxPathProfitSquare.pathProfit - 1 + square.value;  //this is correct
                //store also the moves for that square how to reach it
                ArrayList<Move> temp = new ArrayList<>();
                temp.addAll(maxPathProfitSquare.sheepGotTheMovesLikeJagger);
                temp.add(Move.UP);
                square.sheepGotTheMovesLikeJagger = new ArrayList<>();
                square.sheepGotTheMovesLikeJagger.addAll(temp);
            }
            //Collections.copy(square.sheepGotTheMovesLikeJagger, temp); //ERROR java.lang.IndexOutOfBoundsException: Source dows not fit in dest
            //square.sheepGotTheMovesLikeJagger = maxPathProfitSquare.sheepGotTheMovesLikeJagger; this is bad. they both reference to the same object afterwards and owuld change the arraylist of the origin square too
            //square.sheepGotTheMovesLikeJagger.add(Move.UP); //it breaks here! Sheep gets move up storey in array, as does maxprofitsquare, as does upsquare. is it linked, does it point to the same store location?
        }
        //leftsquare
        if (mapWithValues.containsKey(getStringCoordinate(maxPathProfitSquare, 0, -1))) {
            Square square = mapWithValues.get(getStringCoordinate(maxPathProfitSquare, 0, -1));
            if(square.pathProfit == 999 || maxPathProfitSquare.pathProfit - 1 + square.value > square.pathProfit) {
                square.pathProfit = maxPathProfitSquare.pathProfit - 1 + square.value;
                ArrayList<Move> temp = new ArrayList<>();
                temp.addAll(maxPathProfitSquare.sheepGotTheMovesLikeJagger);
                temp.add(Move.LEFT);
                square.sheepGotTheMovesLikeJagger = new ArrayList<>();
                square.sheepGotTheMovesLikeJagger.addAll(temp);
            }
        }
        //rightsquare
        if (mapWithValues.containsKey(getStringCoordinate(maxPathProfitSquare, 0, 1))) {
            Square square = mapWithValues.get(getStringCoordinate(maxPathProfitSquare, 0, 1));
            if(square.pathProfit == 999 || maxPathProfitSquare.pathProfit - 1 + square.value > square.pathProfit) {
                square.pathProfit = maxPathProfitSquare.pathProfit - 1 + square.value;
                ArrayList<Move> temp = new ArrayList<>();
                temp.addAll(maxPathProfitSquare.sheepGotTheMovesLikeJagger);
                temp.add(Move.RIGHT);
                square.sheepGotTheMovesLikeJagger = new ArrayList<>();
                square.sheepGotTheMovesLikeJagger.addAll(temp);
            }
        }
        //downsquare
        if (mapWithValues.containsKey(getStringCoordinate(maxPathProfitSquare, 1, 0))) {
            Square square = mapWithValues.get(getStringCoordinate(maxPathProfitSquare, 1, 0));
            if(square.pathProfit == 999 || maxPathProfitSquare.pathProfit - 1 + square.value > square.pathProfit) {
                square.pathProfit = maxPathProfitSquare.pathProfit - 1 + square.value;
                ArrayList<Move> temp = new ArrayList<>();
                temp.addAll(maxPathProfitSquare.sheepGotTheMovesLikeJagger);
                temp.add(Move.DOWN);
                square.sheepGotTheMovesLikeJagger = new ArrayList<>();
                square.sheepGotTheMovesLikeJagger.addAll(temp);
            }
        }
        //set this square to expanded so it wont get expanded again (unless we find a more profitable path to it, then we will of course expand it again).
        maxPathProfitSquare.expandedForSearchPath = true;
        //TODO IF YOU FIND A PATHPROFIT THAT IS HIGHER THE EXPANDED FLAG SHOULD BE RESET AND IT SHOULD GET CONSIDERED AGAIN
        //in other words, if one square we expand already exists, but we find a better way to it (higher pathprofit) we should unflag it because we can then expand it and reach neighboring squares maybe also with better path/pathprofit
    }

    //This method adds all valid neighbor squares for initialization to the hashmap. It assigns the distance and the value, and gives the pathprofit 999 as spaceholder since it can never normally reach this value.
    private void initializeMapWithValues(Square origin, int yPos, int xPos, Type map[][]) {
        //Add all valid neighbour Squares
        try {
            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.yCoor - 1 < 0 || origin.distance >= MAXDISTANCE) {
            } else {
                //checks if this exists already in the Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, -1, 0))) {
                } else {
                    //create square up and assign distance
                    Square upsquare = new Square(map[yPos - 1][xPos], origin.xCoor, origin.yCoor - 1, origin.distance + 1, 0, 999, false, noMoves);
                    //assign value to upsquare
                    if (upsquare.type.equals(Type.EMPTY)) {
                        upsquare.value = 0;
                    } else if (upsquare.type.equals(Type.GRASS)) {
                        upsquare.value = 1;
                    } else if (upsquare.type.equals(Type.RHUBARB)) {
                        upsquare.value = 5;
                    } else if (upsquare.type.equals(Type.FENCE)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(myWolf)) {
                        upsquare.value = -999;
                    } else if (upsquare.type.equals(enemyWolf)) {
                        upsquare.value = -999;
                        thereIsEnemyWolf = true;
                        enemyWolfSquare = upsquare;
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
                    squareInitializeExpandQueue.add(upsquare);
                    allInitializedSquares.add(upsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.xCoor + 1 > 18 || origin.distance >= MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 0, 1))) {
                } else {
                    Square rightsquare = new Square(map[yPos][xPos + 1], origin.xCoor + 1, origin.yCoor, origin.distance + 1, 0, 999, false, noMoves);
                    if (rightsquare.type.equals(Type.EMPTY)) {
                        rightsquare.value = 0;
                    } else if (rightsquare.type.equals(Type.GRASS)) {
                        rightsquare.value = 1;
                    } else if (rightsquare.type.equals(Type.RHUBARB)) {
                        rightsquare.value = 5;
                    } else if (rightsquare.type.equals(Type.FENCE)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(myWolf)) {
                        rightsquare.value = -999;
                    } else if (rightsquare.type.equals(enemyWolf)) {
                        rightsquare.value = -999;
                        thereIsEnemyWolf = true;
                        enemyWolfSquare = rightsquare;
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
                    squareInitializeExpandQueue.add(rightsquare);
                    allInitializedSquares.add(rightsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.xCoor - 1 < 0 || origin.distance >= MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 0, -1))) {
                } else {
                    Square leftsquare = new Square(map[yPos][xPos - 1], origin.xCoor - 1, origin.yCoor, origin.distance + 1, 0, 999, false, noMoves);
                    if (leftsquare.type.equals(Type.EMPTY)) {
                        leftsquare.value = 0;
                    } else if (leftsquare.type.equals(Type.GRASS)) {
                        leftsquare.value = 1;
                    } else if (leftsquare.type.equals(Type.RHUBARB)) {
                        leftsquare.value = 5;
                    } else if (leftsquare.type.equals(Type.FENCE)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(myWolf)) {
                        leftsquare.value = -999;
                    } else if (leftsquare.type.equals(enemyWolf)) {
                        leftsquare.value = -999;
                        thereIsEnemyWolf = true;
                        enemyWolfSquare = leftsquare;
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
                    squareInitializeExpandQueue.add(leftsquare);
                    allInitializedSquares.add(leftsquare);
                }
            }

            //check if outside map. Map is always 15x19 squares, see assignment. Or distance is too great because resource limits (time especially).
            if (origin.yCoor + 1 > 14 || origin.distance >= MAXDISTANCE) {
            } else {
                //checks if exists already in Hashmap
                if (mapWithValues.containsKey(getStringCoordinate(origin, 1, 1))) {
                } else {
                    Square downsquare = new Square(map[yPos + 1][xPos], origin.xCoor, origin.yCoor + 1, origin.distance + 1, 0, 999, false, noMoves);
                    if (downsquare.type.equals(Type.EMPTY)) {
                        downsquare.value = 0;
                    } else if (downsquare.type.equals(Type.GRASS)) {
                        downsquare.value = 1;
                    } else if (downsquare.type.equals(Type.RHUBARB)) {
                        downsquare.value = 5;
                    } else if (downsquare.type.equals(Type.FENCE)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(myWolf)) {
                        downsquare.value = -999;
                    } else if (downsquare.type.equals(enemyWolf)) {
                        downsquare.value = -999;
                        thereIsEnemyWolf = true;
                        enemyWolfSquare = downsquare;
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
                    squareInitializeExpandQueue.add(downsquare);
                    allInitializedSquares.add(downsquare);
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            //do not add square since it is outside of the play board
        }
    }


    //at end when whole map is initialized, search if the enemy wolf appears. if yes, mark immediate neighbor squares as -999 value.


    //make data structure for Squares with values yCoor,xCoor,cost,value,pathProfit.
    //Make datastructure like in greedy. Hashmap with string and Square type. The string is the Coordinate (yCoor_xCoor).
    //In the Quare type, store yCoor, xCoor, distance, value, pathProfit.
    //this is used by the methods so it needs to be at the end otherwise in Java the methods will not recognize

    private class Square {
        private Type type;
        private int yCoor, xCoor, distance, value, pathProfit;
        private boolean expandedForSearchPath;
        private ArrayList<Move> sheepGotTheMovesLikeJagger;

        private Square(Type type, int xCoor, int yCoor, int distance, int value, int pathProfit, boolean expandedForsearchPath, ArrayList<Move> sheepGotTheMovesLikeJagger) {
            this.type = type;
            this.xCoor = xCoor;
            this.yCoor = yCoor;
            this.distance = distance;
            this.value = value;
            this.pathProfit = pathProfit;
            this.expandedForSearchPath = expandedForSearchPath;
            this.sheepGotTheMovesLikeJagger = sheepGotTheMovesLikeJagger;
        }
    }

    protected String getStringCoordinate(Square square, int yShift, int xShift) {
        return Integer.toString(square.yCoor + yShift) + "_" + Integer.toString(square.xCoor + xShift);
    }
}