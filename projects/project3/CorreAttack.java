import java.util.ArrayList;
public class CorreAttack {
	double N;
	int[] keystream;
	public static void main(String[] args) {

		CorreAttack ca = new CorreAttack();
		ca.solve();
	}

	public void solve() {
		int[] k = {1,0,1,1,0,0,0,0,1,0,1,0,0,1,0,1,0,1,0,
			0,0,0,0,0,0,0,0,1,1,1,0,0,0,1,1,1,1,1,1,1,0,1,1,0,
			1,1,1,1,1,1,1,0,1,1,1,0,0,0,0,1,0,0,0,0,0,0,0,1,1,
			0,1,1,0,1,0,0,1,1,0,1,0,1,1,1,1,1,1,0,1,0,1,0,1,1,
			0,1,0,1,0,0,1,1,1,1,1,1,1,1,0,0,0,1,1,1,1,1,0,0,1,
			1,1,1,0,0,1,0,1,1,0,0,1,1,0,0,1,1,0,0,1,1,0,1,1,0,
			1,0,0,0,1,0,1,1,0,0,0,1,0,0,1,1,0,0,1,0,1,0,0,0,1,
			0,0,0,1,0,1,0,0,1,0,0,1,1,1,0,1,0,1,1,1,1,0,0,1};
		keystream = k;
		N = keystream.length;
		ArrayList<Integer> state1 = generateStream(13, 1);
		ArrayList<Integer> state2 = generateStream(15, 2);
		ArrayList<Integer> state3 = generateStream(17, 3);
		int[] res = new int[(int)N];
		for (int i = 0; i < N; i++)
			res[i] = (state1.get(i) + state2.get(i) + state3.get(i) > 1) ? 1 : 0;
		int errors = 0;
		for (int i = 0; i < N; i++) {
			if (res[i] != keystream[i]) {
				errors+= 1;
			}
		}
		if (errors > 0) {
			System.out.println("Wrong result, " + errors + " errors");
		} else {
			System.out.println("State 1: ");
			for (int i = 0; i < 13; i++)
				System.out.print(state1.get(i));
			System.out.println();
		}
	}
	public ArrayList<Integer> generateStream(int deg, int lfsr) {
		int numStates;
		numStates = (int) Math.pow(2, deg) - 1;
		int[] LFSR = new int[deg];
		ArrayList<Integer> output = new ArrayList<Integer>();
		int next, out;
		LFSR[deg-1] = 1;
		double temp;
		double p = 0;
		ArrayList<Integer> best = null;;
		ArrayList<Integer> start;
		for (int i = 0; i < N; i++) {
			// Get output bit
			out = LFSR[0];
			// Get the new value
			switch(lfsr) {
				case 1:
					next = LFSR[0] ^ LFSR[2] ^ LFSR[3] ^ LFSR[6] ^ LFSR[7] ^ LFSR[9] ^ LFSR[11] ^ LFSR[12];
					break;
				case 2:
					next = LFSR[0] ^ LFSR[2] ^ LFSR[4] ^ LFSR[5] ^ LFSR[8] ^ LFSR[9] ^ LFSR[11] ^ LFSR[13];
					break;
				case 3:
					next = LFSR[0] ^ LFSR[1] ^ LFSR[4] ^ LFSR[7] ^ LFSR[9] ^ LFSR[12] ^ LFSR[13] ^ LFSR[15];
					break;
				default:
					next = -1;
					System.err.println("Missing LFSR");
					System.exit(1);
			}
			for (int k = 0; k < deg-1; k++)
				LFSR[k] = LFSR[k + 1];
			LFSR[deg-1] = next;
			output.add(out);
		}
		p = 1 - (hamming(output) / N);
		best = output;
		for (int m = 0; m < numStates; m++) {
			out = LFSR[0];
			switch(lfsr) {
				case 1:
					next = LFSR[0] ^ LFSR[2] ^ LFSR[3] ^ LFSR[6] ^ LFSR[7] ^ LFSR[9] ^ LFSR[11] ^ LFSR[12];
					break;
				case 2:
					next = LFSR[0] ^ LFSR[2] ^ LFSR[4] ^ LFSR[5] ^ LFSR[8] ^ LFSR[9] ^ LFSR[11] ^ LFSR[13];
					break;
				case 3:
					next = LFSR[0] ^ LFSR[1] ^ LFSR[4] ^ LFSR[7] ^ LFSR[9] ^ LFSR[12] ^ LFSR[13] ^ LFSR[15];
					break;
				default: 
					next = -1;
					System.err.println("Missing LFSR");
					System.exit(1);
			}
			for (int k = 0; k < deg-1; k++)
				LFSR[k] = LFSR[k + 1];
			LFSR[deg-1] = next;
			output.remove(0);
			output.add(out);
			// Probability
			temp = 1 - (hamming(output) / N);
			if (temp > 0.7)
				System.out.println(temp);
			if (temp > p) {
				p = temp;	
				best = output;
			}
		}
		System.out.println("Best P -> " + p);
		for (Integer i : best)
			System.out.print(i);
		System.out.println();
		return best;
	}
	double hamming(ArrayList<Integer> out) {
		int h = 0;
		for (int i = 0; i < N; i++)
			h += (out.get(i) + keystream[i]) % 2;
		return	h;
	}
	void print_state(ArrayList<Integer> s) {
		for (Integer i : s)
			System.out.print(i);
		System.out.println();
	}
}
