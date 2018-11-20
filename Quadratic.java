import java.util.ArrayList;
import java.math.BigInteger;
import java.lang.ProcessBuilder;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;





public class Quadratic {

    public static void main(String[] args) throws IOException{
        ArrayList<Integer> primes = eratosthenes(30);
        BigInteger N = BigInteger.valueOf(16637);
        ArrayList<BigInteger> rList = new ArrayList<BigInteger>();
        int[][] binMatrix = rGenerator(rList, primes, N);
        System.out.println(rList.toString());
        File inFile = new File("inputMatrix.txt");
        //File outFile = new File("~/Desktop/outputMatrix.txt");
        PrintWriter writer = new PrintWriter(inFile, "UTF-8");
        writer.println("12 10");
        for(int i = 0; i < binMatrix.length; i++){
          for(int j = 0; j < binMatrix[0].length; j++){
            writer.print(binMatrix[i][j] + " ");
          }
          writer.println();
        }
        writer.close();
        ArrayList<String> list = new ArrayList<String>();
        list.add("/Users/macbook/Desktop/GaussBin.cpp");
        list.add("/Users/macbook/Desktop/inputMatrix.txt");
        //list.add("/Users/macbook/Desktop/outputMatrix.txt");


        ProcessBuilder process = new ProcessBuilder(list);
        process.start();
        System.out.println("directory: " + process.directory());


    }


    public static ArrayList<Integer> eratosthenes(int B){
      int p = 2;
      ArrayList<Integer> primeList = new ArrayList<Integer>();
      int listLength = B;
      for(int i = 2; i < listLength; i++){
        primeList.add(i);
      }
      int pos = 1;
      while(Math.pow(p, 2) < listLength){
        for(int i = pos; i < primeList.size(); i++){
          if(primeList.get(i) % p == 0){
            primeList.remove(i);

          }
        }
        p = primeList.get(pos);
        pos++;

      }
      return primeList;

    }

    public static int[][] rGenerator(ArrayList<BigInteger> rList, ArrayList<Integer> primes, BigInteger N){

      // 20 är inte rätt!!
      int[][] binMatrix = new int[12][primes.size()];
      int currentRow = 0;
      BigInteger one = BigInteger.valueOf(1);
      BigInteger two = BigInteger.valueOf(2);
      BigInteger three = BigInteger.valueOf(3);
      BigInteger twenty = BigInteger.valueOf(15);
      for(BigInteger k = three; k.compareTo(twenty) < 0; k = k.add(one)){
        for(BigInteger j = two; j.compareTo(k) <= 0; j = j.add(one)){
          BigInteger r = FloorRoot(N.multiply(k)).add(j);
          BigInteger rPow = r.pow(2).mod(N);
          int[] binVec = vecBSmooth(primes, rPow);
          if(binVec != null){
            boolean match = false;
            for(int row = 0; row < currentRow; row++){
              for(int col = 0; col < binMatrix[0].length; col++){
                if(binVec[col] != binMatrix[row][col]){
                    break;
                }
                if(col == binVec.length-1){
                  match = true;
                }
              }
              if (match) break;
            }
            if(!match){
              rList.add(r);
              for(int col = 0; col < binMatrix[0].length; col++){
              binMatrix[currentRow][col] = binVec[col];
              }
              currentRow++;
              if(currentRow == binMatrix.length){
                return binMatrix;
              }
            }
          }
        }
      }
      return binMatrix;
    }

    public static int[][] binaryMatrix(){
      return null;
    }

    public static int[] vecBSmooth(ArrayList<Integer> primes, BigInteger r){
      BigInteger one = BigInteger.valueOf(1);
      BigInteger zero = BigInteger.valueOf(0);
      int pos = 0;
      int[] binArray = new int[primes.size()];
      while(pos < primes.size() && r.compareTo(one) != 0){
        BigInteger prime = BigInteger.valueOf(primes.get(pos));
        while(r.mod(prime).equals(zero)){
          r = r.divide(prime);
          binArray[pos] = (binArray[pos]+1) % 2;
        }
        pos++;
      }
      if(r.compareTo(one) == 0){
        return binArray;
      }

      return null;
    }


    private static BigInteger FloorRoot(BigInteger x){
        BigInteger one = BigInteger.valueOf(1);
        BigInteger two = BigInteger.valueOf(2);
        long intermediateGuess = (long)Math.sqrt(x.floatValue());
        BigInteger guess = BigInteger.valueOf(intermediateGuess);
        boolean guessBelowGoal;
        boolean guessPlusOneAboveGoal;

        do{
          guess = guess.add(x.divide(guess)).divide(two);
          guessBelowGoal = guess.pow(2).compareTo(x) <= 0;
          guessPlusOneAboveGoal = guess.add(one).pow(2).compareTo(x) > 0;
        } while (!(guessBelowGoal && guessPlusOneAboveGoal));

        return guess;
      }



}
