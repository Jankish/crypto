import java.util.BitSet;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.File;
public class Brujin {
	public static void main(String[] args) {
		Brujin b = new Brujin();
		b.Solve();
	}

	public void Solve() {
		int[] left_sequence = generate_sequence(2,4,true);
		int[] right_sequence = generate_sequence(5,4,false);
		try {
			PrintWriter w = new PrintWriter("output");
			for (int i = 0; i < 16; i++) {
				for (int k = 0; k < 624; k+=4) {
					w.print(Integer.toString(left_sequence[i%16]*5 + right_sequence[k%625]));
					w.print(Integer.toString(left_sequence[(i+1)%16]*5 + right_sequence[(k+1)%625]));
					w.print(Integer.toString(left_sequence[(i+2)%16]*5 + right_sequence[(k+2)%625]));
					w.print(Integer.toString(left_sequence[(i+3)%16]*5 + right_sequence[(k+3)%625]));
				}
				w.print(Integer.toString(left_sequence[i%16]*5 + right_sequence[624]));
				if (i == 15) {
					w.print(Integer.toString(left_sequence[(i+1)%16]*5 + right_sequence[0]));
					w.print(Integer.toString(left_sequence[(i+2)%16]*5 + right_sequence[1]));
					w.print(Integer.toString(left_sequence[(i+3)%16]*5 + right_sequence[2]));
				}
			}	
			w.println();
			w.close();
		} catch(FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public int[] generate_sequence(int a, int n, boolean left) {
		int length;
		length = (int) Math.pow(a, n);
		int[] GSR = new int[4];
		int[] output = new int[length];
		int next, out;
		GSR[3] = 1;
		for (int i = 1; i < length; i++) {
			// Get output bit
			out = GSR[0];
			// Get the new value
			if (left) {
				next = GSR[0] ^ GSR[3];
			} else {
                next = (3*(1*GSR[0]+4*GSR[1]+1*GSR[2]+4*GSR[3])) % 5;
			}
			// Shift all bits
			for (int k = 0; k < 3; k++)
				GSR[k] = GSR[k + 1];
			GSR[3] = next;
			output[i] = out;
		}
		//Manually add zero
		output[0] = 0;
		return output;
	}
}
