import java.util.ArrayList;
import java.math.BigInteger;
import java.lang.ProcessBuilder;
import java.io.PrintWriter;
import java.io.File;
import java.io.IOException;





public class Quadratic {

    public static void main(String[] args) throws IOException{
        ArrayList<Integer> primes = eratosthenes(9000);
        BigInteger N = new BigInteger("182275519598130020422753");
        ArrayList<BigInteger> rList = new ArrayList<BigInteger>();
        int L = primes.size() + 24;
        int[][] binMatrix = rGenerator(rList, primes, N, L);
        //System.out.println(rList.toString());
        ArrayList<int[]> solvMatrix = gaussEl(binMatrix);
        BigInteger factor = findFactor(solvMatrix, rList, N);
        System.out.println(factor.toString());
        BigInteger factor2 = N.divide(factor);
        System.out.println(factor2.toString());

    }


    public static BigInteger findFactor(ArrayList<int[]> solvMatrix, ArrayList<BigInteger> rList, BigInteger N){
      BigInteger one = BigInteger.valueOf(1);
      BigInteger factor = null;
      BigInteger x = one;
      BigInteger y = one;
      do{
        int[] solvVec = solvMatrix.get(0);
        for(int i = 0; i < solvVec.length; i++){
          if(solvVec[i] == 1){
            x = x.multiply(rList.get(i));
            y = y.multiply(rList.get(i).pow(2).mod(N));
          }
        }
        x = x.mod(N);
        y = FloorRoot(y).mod(N);
        factor = N.gcd(y.subtract(x));
        solvMatrix.remove(0);
      }while(factor.equals(one) && !solvMatrix.isEmpty());

      return factor;
    }

    public static ArrayList<int[]> gaussEl(int[][] binMatrix){
      ArrayList<int[]> solvMatrix = new ArrayList<int[]>(binMatrix.length);
      for(int i = 0; i < binMatrix.length; i++){
        int[] vec = new int[binMatrix.length];
        vec[i] = 1;
        solvMatrix.add(vec);
      }

      ArrayList<int[]> binMatrixList = new ArrayList<int[]>(binMatrix.length);
      for(int i = 0; i < binMatrix.length; i++){
        binMatrixList.add(binMatrix[i]);
      }
      int oddIndex = Integer.MAX_VALUE;
      for(int pivot = 0; pivot < binMatrix[0].length; pivot++){
        for(int row = 0; row < binMatrixList.size(); row++){
          if(binMatrixList.get(row)[pivot] == 1){
            if(row < oddIndex){
              oddIndex = row;
            } else{
              for(int col = 0; col < binMatrix[0].length; col++){
                binMatrixList.get(row)[col] = (binMatrixList.get(row)[col] + binMatrixList.get(oddIndex)[col]) % 2;
              }
              for(int col = 0; col < binMatrix[0].length; col++){
                solvMatrix.get(row)[col] = (solvMatrix.get(row)[col] + solvMatrix.get(oddIndex)[col]) % 2;
              }

            }

          }

        }
        if(oddIndex!=Integer.MAX_VALUE){
          binMatrixList.remove(oddIndex);
          solvMatrix.remove(oddIndex);
          oddIndex = Integer.MAX_VALUE;
        }
      }
        return solvMatrix;
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

    public static int[][] rGenerator(ArrayList<BigInteger> rList, ArrayList<Integer> primes, BigInteger N, int L){

      int[][] binMatrix = new int[L][primes.size()];
      int currentRow = 0;
      BigInteger one = BigInteger.valueOf(1);
      BigInteger two = BigInteger.valueOf(2);
      BigInteger three = BigInteger.valueOf(3);
      BigInteger k = one;
      while(currentRow < binMatrix.length){
        k = k.add(one);
        for(BigInteger j = two; j.compareTo(k) <= 0 && currentRow < binMatrix.length; j = j.add(one)){
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
            }
          }
        }
      }
      return binMatrix;
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
