import java.math.BigInteger;
import java.util.*;

public class QuadraticSieve {
	//	private BigInteger N = new BigInteger("148042268393964514407317");
	private BigInteger N = new BigInteger("16637");
	private BigInteger p;
	private BigInteger q;
	private int L = 12;
	private int B;
	private ArrayList<Long> F;

	public QuadraticSieve(int B) {
		this.B = B;
		F = PrimeGenerator.GeneratePrimeNumbers(B);
		for(BigInteger bi : generateR(N, L, F))
			System.out.println(bi.toString());
	}

	public BigInteger[] generateR(BigInteger N, int L, ArrayList<Long> F) {
		int count = 0;
		int k = 1;
		int j;
		BigInteger[] r = new BigInteger[L]; 
		BigInteger current;	
		while(count < L) {
			k++;
			for(j = 2; j <= k; j++) {
				if (count >= L)
					break;
				current = PrimeGenerator.squareroot(N.multiply(new BigInteger(Integer.toString(k)))).add(new BigInteger(Integer.toString(j))); 
				System.out.println("Current -> " + current.toString() + "\n" + "k -> " + k + "\n" + "j -> " + j);
				if(isSmooth(current, N, F)) {
					r[count] = current;
					count += 1;
				}
			}
		}

		return r;
	}

	private boolean isSmooth(BigInteger current, BigInteger N, ArrayList<Long> F) {
		BigInteger rSqr;
		rSqr = current.pow(2).mod(N);
		int i=0;
		System.out.println("\t rSqr -> " + rSqr.toString());
		while (!rSqr.equals(BigInteger.ZERO) && !rSqr.equals(BigInteger.ONE)) {
			BigInteger temp = new BigInteger(Long.toString(F.get(i)));
			System.out.println("\t Temp -> " + temp.toString());
			if (rSqr.mod(temp).equals(BigInteger.ZERO)) {
				rSqr = rSqr.divide(temp);
			} else {
				i += 1;
				if(i == F.size()) 
					return false;
			}

		}
		System.out.println(current.toString() + " is found");
		return true;
	}


	public static void main(String[] args){
		QuadraticSieve qs = new QuadraticSieve(30);	
	}


}

