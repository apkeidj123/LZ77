package selfstudy.e1.tsuki;

import java.io.*;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Lz77 {

	public static void main(String args[]) throws FileNotFoundException, IOException {

		String in = null,input="",inp=null;
		try {
			FileInputStream file = new FileInputStream("input.txt"); // input
			DataInputStream data = new DataInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(data));

			inp = br.readLine();
			input+=inp;
			while((in=br.readLine())!=null){
				input+="\n";
				input+=in;
				
			}
			
			System.out.println("in= "+in);
			System.out.println(input);

			FileOutputStream f1 = new FileOutputStream("compress.txt"); // compress
			DataOutputStream d1 = new DataOutputStream(f1);

			int pointer = 0, len1 = 0;
			char nc = '0';
			String w, y = null;
			// String[] dictionary = new String[100];
			int i, j = 0, z, l = 0;

			// System.out.println(input.length()); //22
			for (i = 0; i <= input.length(); i++) {
				System.out.println("i1= " + i);
				w = null;
				w = input.substring(0, i); // input 的最左為1開始往右增加到i
				System.out.println("w= " + w);
				j = i + 1;
				do {
					y = input.substring(i, j); // from i to j-1
					System.out.println("y= " + y);
					// add
					if (y.length() > 1) {
						w = input.substring(0, j - 1);
						System.out.println("w2= " + w);
					}
					// add
					z = w.lastIndexOf(y); // if y has the member which is not in
											// x ,z will be -1 ;z return the
											// start index of the last substring
											// y
					System.out.println("z= " + z);
					if (z != -1) {
						l = z;
						j++;
						// System.out.println("z2= " + z);
						if (j > input.length()) // end of the input
						{
							System.out.println("j= " + j);
							pointer = i - l;
							i = input.length();//
							len1 = y.length();// length of characters copied
							nc = '0';
							System.out.println("(pointer(offset),length,next characterr) = " + "(" + pointer + ","
									+ len1 + "," + nc + ")");
							d1.writeInt(pointer); // offset
							d1.writeInt(len1); // length of the same string
							d1.writeChar(nc);
							break;
						}
					} else {
						// i = w.length() + (y.length() - 1); // -1 -> j + "1"
						// System.out.println("i2= " + i);
						if (y.length() == 1) // new character
						{
							pointer = 0;
							len1 = 0;
						}
						// add
						else if ((l + y.length() - 1) > i) {
							pointer = i - l;
							len1 = y.length() - 1;// length of characters copied
							i = w.length();
							System.out.println("i2= " + i);

						}
						// add
						else {
							pointer = i - l; // search buffer ... "1"
												// "0" | look ahead
												// buffer
							i = w.length();
							System.out.println("i3= " + i);
							len1 = y.length() - 1;// length of characters copied

						}
						nc = y.charAt(y.length() - 1);// next character
						System.out.println("(pointer(offset),length,next characterr) = " + "(" + pointer + "," + len1
								+ "," + nc + ")");
						d1.writeInt(pointer);
						d1.writeInt(len1);
						d1.writeChar(nc);
						break;
					}
				} while (z != -1);
			}
			d1.close();
			data.close();
		} catch (Exception ex) {
		}

		FileInputStream f2 = new FileInputStream("compress.txt");
		DataInputStream d2 = new DataInputStream(f2);
		BufferedReader b = new BufferedReader(new InputStreamReader(d2));

		FileOutputStream fos = new FileOutputStream("decompress.txt");
		DataOutputStream dos = new DataOutputStream(fos);

		int p = 0, len = 0;// temp , temp_length
		char n;
		String decomp = "";
		System.out.println("decomp:");
		// System.out.println("decomp.length()= "+decomp.length());
		while (f2.available() != 0) {
			int t = 0, lt = 0;
			String x = "", v = "";
			p = d2.readInt();// pointer
			// System.out.println(p);
			len = d2.readInt();// length
			// System.out.println(len);
			n = d2.readChar();// nextcharacter
			// System.out.println(n);
			System.out.println("(pointer(offset),length,next characterr) = " + "(" + p + "," + len + "," + n + ")");
			if (p == 0) {
				decomp += n;
				// System.out.println("decomp.length()= "+decomp.length());
			} else {
				if (n == '0') {
					decomp += (decomp.substring((decomp.length() - p), ((decomp.length() - p) + len)));

				} else if (len > p) {
					lt = len;
					while (lt > p) {
						lt -= p;
						t++;
						if (lt <= 2 * p && lt > p) {
							len = lt;
						}
					}

					x += (decomp.substring((decomp.length() - p), ((decomp.length() - p) + len - p)));// the
																										// hidden
																										// characters
					v += (decomp.substring((decomp.length() - p), (decomp.length())));
					for (int k = 0; k < t; k++) {
						decomp += v;
					}
					decomp += x + n;
				} else {
					System.out.println("decomp.length()= " + decomp.length() + " (decomp.length()-p)= "
							+ (decomp.length() - p) + " ((decomp.length()-p)+len)= " + ((decomp.length() - p) + len));
					decomp += (decomp.substring((decomp.length() - p), ((decomp.length() - p) + len)) + n); // search
																											// buffer
																											// "0"
																											// "1"...|
																											// look
																											// ahead
																											// buffer
					// System.out.println("decomp.length()= "+decomp.length()+"
					// (decomp.length()-p)= " + (decomp.length()-p)+"
					// ((decomp.length()-p)+len)= "+((decomp.length()-p)+len));
				}

			}
			System.out.println("decomp= " + decomp);
		}
		dos.writeChars(decomp);
		// System.out.println("decomp= "+decomp);

		b.close();
		dos.close();

	}

}
