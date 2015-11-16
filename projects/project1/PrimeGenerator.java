import java.math.*;
import java.util.*;

public class PrimeGenerator {

	public static ArrayList<Long> GeneratePrimeNumbers(long size) { 
		double k;
		int j;
		ArrayList<Long> primes = new ArrayList<Long>();
		long i = 2;
		while (i < size) {
			k=Math.sqrt((double)i)+1.;
			for(j=0; j<primes.size() && primes.get(j)<=k; ++j) { 
				if(i%primes.get(j)==0) { 
					j=0;
					break; 
				}
			}
			if(j != 0  || primes.isEmpty()) 
				primes.add(i);
			i += 1;
		}

/**		System.out.println("Printing primes");
		for(long l: primes)
			System.out.println(l);*/

		return primes;
	}


	/**calculate the square root of a biginteger in logarithmic time*/
	public static BigInteger squareroot(BigInteger x) { 
		BigInteger right = x, left = BigInteger.ZERO, mid; 
		while(right.subtract(left).compareTo(BigInteger.ONE) > 0) { 
			mid = (right.add(left)).shiftRight(1);
			if(mid.multiply(mid).compareTo(x) > 0) 
				right = mid; 
			else 
				left = mid; 
		} 
		return left; 
	}
}
