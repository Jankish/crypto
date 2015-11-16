import java.math.BigInteger;
import java.util.*;

public class QuadraticSieve {
	private BigInteger N = new BigInteger("148042268393964514407317");
	private BigInteger p;
	private BigInteger q;
	private int L = 1000;
	private int B;
	private ArrayList<Long> F;
	
	public QuadraticSieve(int B) {
		this.B = B;
		F = PrimeGenerator.GeneratePrimeNumbers(B);
	}

	
	public static void main(String[] args){
		QuadraticSieve qs = new QuadraticSieve(100);	
	}


}

