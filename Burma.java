/* Joseph Tully
CSCI 511 Spring 2020
Programming Assignment 2
5/16/2020

*/
import java.io.File;  // Import the File class
import java.io.FileNotFoundException;  // Import this class to handle errors

import java.util.Scanner; // Import the Scanner class to read text files
import java.util.*;

public class Burma {

    //Function for parsing a data file for the input. Takes in file name, returns array of ints
    private static int[] parse(String file) {
        //build arrayList of numbers from given file.
        ArrayList<Integer> nums = new ArrayList<Integer>();
        try {
            File f = new File(file);
            Scanner sc = new Scanner(f);
            while(sc.hasNextInt()){
                nums.add(sc.nextInt());
            }
        }   
        catch (FileNotFoundException e) {
            System.out.println("Error opening given file.");
        }

        //convert arraylist to int[]
        int[] ret = new int[nums.size()];
        Iterator<Integer> iterator = nums.iterator();
        for (int i = 0; i < ret.length; i++)
        {
            ret[i] = iterator.next().intValue();
        }
        return ret;
    }

    public static void minCost(int[] positions, int[] cost, int numSigns, int maxInterval){
        int n = positions.length;
        int m = positions[n-1];
        int[] nextBoard = new int[n-1];
        for (int i=0; i<n-1; i++){
            nextBoard[i] = -1;
        }

        for(int board = 0; board < n-1; board++){
            //System.out.println("board: " + board);
            int nextidx = board+1;
            if(positions[nextidx] - positions[board] > maxInterval){
                //System.out.println("Problem " + positions[nextidx] + " " + positions[board]);
                break;
            }
            for(int idx = board+2; idx<n; idx++){
                //System.out.println("checking cost at idx: " + idx + " " + cost[idx]);
                if(positions[idx] - positions[board] > maxInterval)
                    break;
                if(cost[idx] < cost[nextidx])
                    nextidx = idx;
            }
            nextBoard[board] = nextidx;
            //System.out.println(Arrays.toString(nextBoard));
        }

        //System.out.println(Arrays.toString(nextBoard));

        int[] totCosts = new int[n-numSigns+1];
        for(int i =0; i < totCosts.length; i++){
            totCosts[i] = cost[i];
        }
        for(int startAt = 0; startAt < totCosts.length; startAt++){
            int nextBoardIdx = startAt;
            for(int i = 0; i< numSigns-1; i++){
                nextBoardIdx = nextBoard[nextBoardIdx];
                if(nextBoardIdx == -1){
                    totCosts[startAt] = Integer.MAX_VALUE;
                    break;
                }
                totCosts[startAt] += cost[nextBoardIdx];
            }
        }
        //System.out.println(Arrays.toString(totCosts));
        
        //find min, min index in totCosts
        int minCost = totCosts[0];
        int bestStart = 0;
        for(int i = 1; i< totCosts.length; i++){
            if(totCosts[i] < minCost){
                minCost = totCosts[i];
                bestStart = i;
            }
        }

        System.out.println("Min cost: " + minCost);
        int[] bestPositions = new int[numSigns];
        bestPositions[0] = bestStart;
        int nextBoardIdx = bestStart;
            for(int i = 1; i< numSigns; i++){
                nextBoardIdx = nextBoard[nextBoardIdx];
                bestPositions[i] = nextBoardIdx;
            }
        System.out.println(Arrays.toString(bestPositions));

        int[] distArr = new int[numSigns];
        for(int i = 0; i< numSigns; i++){
            distArr[i] = positions[bestPositions[i]];
        }
        System.out.println(Arrays.toString(distArr));

        return;
    }

    //Main takes in the filename from command line args, parses the file with parse, and 
    public static void main(String[] args){
        String file = args[0];
        int[] nums = parse(file);
        //parse returned contents of file
        int numSigns = nums[0];
        int maxInterval = nums[1];
        int n = (nums.length - 2)/2;
        int[] positions = new int[n], prices = new int[n];
        for( int i = 0; i<n; i++){
            positions[i] = nums[i+2];
            prices[i] = nums[i+n+2];
        }
        minCost(positions, prices, numSigns, maxInterval);

    }

}