import java.math.BigInteger;
import java.util.*;
import java.io.*;

public class QuadraticSieve {

	private final BigInteger N; 
	private BigInteger P;
	private BigInteger Q;
	private final int L;
	private final int B;
	private ArrayList<Long> F;
	private int[][] exponents;
	private BigInteger[] r;

	public QuadraticSieve(BigInteger N, int L, int B) {
		this.B = B;
		this.N = N;
		this.L = L;
		long start = System.currentTimeMillis();
		System.out.println("Started clock");
		F = PrimeGenerator.GeneratePrimeNumbers(B);
		System.out.println("Generated primes in " + (System.currentTimeMillis() - start)/1000 + " seconds");		
		exponents = new int[L][F.size()];
		start = System.currentTimeMillis();
		generate();
		System.out.println("Generated matrices in " + (System.currentTimeMillis() - start)/1000 + " seconds");		
		start = System.currentTimeMillis();
		LinkedHashSet<BitSet> solutions = gauss();
		System.out.println("Generated gauss in " + (System.currentTimeMillis() - start)/1000 + " seconds");		
		start = System.currentTimeMillis();
		for (BitSet b : solutions) {
			if (isSolution(b)) { 
				System.out.println("Found solution in " + (System.currentTimeMillis() - start)/1000 + " seconds");		
				System.exit(0);
			}
		}
		System.out.println("No solution found =(");
			/*
		for (BitSet b : solutions) {
			for (int j = 0; j < b.length(); j++) {
				int k = b.get(j) ? 1 : 0;
				System.out.print(k + " ");
			}
			System.out.print("\n");
		} */
	}

	public boolean isSolution(BitSet b) {
		BigInteger LHS = new BigInteger("1");
		BigInteger RHS = new BigInteger("1");
		int[] solExponents = new int[F.size()];
		for (int i = 0; i < b.length(); i++) {
			if (b.get(i)) {
				LHS = LHS.multiply(r[i]);
				for (int k = 0; k < F.size(); k++) { 
					solExponents[k] += exponents[i][k];
				}
			}
		}
		for (int i = 0; i < solExponents.length; i++) { 
			solExponents[i] /= 2;
			BigInteger val = BigInteger.valueOf(F.get(i));
			RHS = RHS.multiply(val.pow(solExponents[i]));
		}
		LHS = LHS.mod(N);
		RHS = RHS.mod(N);
		P = N.gcd(RHS.subtract(LHS));
		if (!P.equals(BigInteger.ONE)) { 
			Q = N.divide(P);
			System.out.println("Found the factors as P = " + P + ", Q = " + Q);
			return true;
		}
		return false;
	}


	public void generate() {
		int count = 0;
		int k = 1;
		int j;
		r = new BigInteger[L]; 
		BigInteger current;
		LinkedHashSet<BitSet> M = new LinkedHashSet<BitSet>();
		while(count < L) {
			k++;
			for(j = 2; j <= k; j++) {
				if (count >= L)
					break;
				current = PrimeGenerator.squareroot(N.multiply(new BigInteger(Integer.toString(k)))).add(new BigInteger(Integer.toString(j))); 
				//System.out.println("Current -> " + current.toString() + "\n" + "k -> " + k + "\n" + "j -> " + j);
				int[] expVector = getExponentVector(current);
				if (expVector != null) {
					BitSet b = convertToBits(expVector);
					if (!M.contains(b)) {
						M.add(b);
						exponents[count] = expVector;
						r[count] = current;
						count += 1;
					}
				}
			}
		}

	}
	public BitSet convertToBits(int[] exps) {
		BitSet b = new BitSet(F.size());
		for (int i = 0; i < exps.length; i++) {
			if (exps[i] % 2 != 0)
				b.flip(i);
		}
		return b;
	}
	private int[] getExponentVector(BigInteger current) {
		BigInteger rSqr;
		int[] exponents = new int[F.size()];
		rSqr = current.pow(2).mod(N);
		int i=0;
		//System.out.println("\t rSqr -> " + rSqr.toString());
		while (!rSqr.equals(BigInteger.ZERO) && !rSqr.equals(BigInteger.ONE)) {
			BigInteger temp = new BigInteger(Long.toString(F.get(i)));
			//System.out.println("\t Temp -> " + temp.toString());
			if (rSqr.mod(temp).equals(BigInteger.ZERO)) {
				exponents[i] += 1;
				rSqr = rSqr.divide(temp);
			} else {
				i += 1;
				if(i == F.size()) 
					return null;
			}

		}
		//System.out.println(current.toString() + " is found");
		return exponents;
	}

	public LinkedHashSet<BitSet> gauss() {
		try {
			BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("input"))); 
			writer.write(L + " " + F.size());
			writer.newLine();
			for(int i = 0; i < L; i++) {
				for(int j = 0; j < F.size(); j++) {
					writer.write(exponents[i][j] + " ");
				}
				writer.newLine();
			}
			writer.close();
			Process p = Runtime.getRuntime().exec("./GaussSolver input output");
			p.waitFor();
			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("output"))); 
			int numSolutions = Integer.valueOf(reader.readLine());
			LinkedHashSet<BitSet> possibleSolutions = new LinkedHashSet<BitSet>();
			String solution;
			BitSet b;
			for (int i = 0; i < numSolutions; i++) {
				b = new BitSet(F.size() + 1);
				solution = reader.readLine().replaceAll(" ", "");
				for (int j = 0; j < solution.length(); j++) {
					if (solution.charAt(j) == '1')
						b.flip(j);
				}
				possibleSolutions.add(b);
			}
			reader.close();
			return possibleSolutions;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException io) {
			io.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
		return null;
	}

	public static void main(String[] args){
		BigInteger N = new BigInteger("148042268393964514407317");
		//BigInteger N = new BigInteger("92434447339770015548544881401");
		int L = 1000;
		int B = 5000;
		QuadraticSieve qs = new QuadraticSieve(N, L, B);	
	}


}

