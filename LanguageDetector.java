import java.util.Scanner;


import java.util.Arrays;
import java.io.File;
import java.io.FileNotFoundException;


public class LanguageDetector {	
	//this is a constant used to calculate matrix distance
	final static double P_VAL = 2; 

	public static void main(String[] args) throws FileNotFoundException {
    	LanguageGUI gui = new LanguageGUI();
    	gui.display();
	
	}	
	
	//gets matrices that represent each language
	public static double[][] getMatrix(String fileName) throws 
										FileNotFoundException {
		double counter = 0;
		double[][] out;
		out = new double[26][26];
		File in = new File(fileName);
		Scanner console = new Scanner(in);

		while(console.hasNext()) {
			String word = console.next().toLowerCase();
			//apostrophes are the only punctuation we could think of that 
			//should be taken out so that the word "don't" would be treated
			//as "dont"
			word = word.replace("'", "");

			if(word.length() >= 2) {
				for(int i = 0; i <= (word.length()-2); i++) {
					char first = word.charAt(i);
					char second = word.charAt(i+1);
					int f = (int) first - 'a';
					int s = (int) second - 'a';
					//if there are weird punctuations like "/!.>< etc, the 
					//program essentially ignores it
					if(f <= 25 && s <= 25 && f >= 0 && s >= 0) {
					out[f][s]++;
					}
				}
			}

		}

		//sums the total of all 26*26 entries
		for(int i = 0; i <= 25; i++) {
			for(int j = 0; j <= 25; j++) {
				counter += out[i][j];
			}
		}

		//standardizes so that each entry represents a percentage because 
		//we didn't have the same number of character pairs in each text file
		for(int i = 0; i <= 25; i++) {
			for(int j = 0; j <= 25; j++) {
				out[i][j] = out[i][j]/counter*100;
			}
		}
	return out;
	}

	//makes a matrix that represents user input
	public static double[][] getMatrixIn(String input) {
		double counter = 0;
		double[][] out;
		out = new double[26][26];
		Scanner console = new Scanner(input);
	
		while(console.hasNext()) {
			String word = console.next().toLowerCase();
			word = word.replace("'", "");
			if(word.length() >= 2) {
				for(int i = 0; i <= (word.length()-2); i++) {
					//since these might be accented words we had to change to 
					//QWERTY standard inputs. see method for more on this
					char first = correctChars(word.charAt(i));
					char second = correctChars(word.charAt(i+1));
					//had to make a weird exception because the german ß is
					//written in English as "ss"
					if((int) first == 225) {
						out[18][18]++;
					}
					int f = (int) first - 'a';
					int s = (int) second - 'a';
					if(f <= 25 && s <= 25 && f >= 0 && s >= 0) {
						out[f][s]++;
					}
				}
	
			}	
		}
		console.close();

		for(int i = 0; i <= 25; i++) {
			for(int j = 0; j <= 25; j++) {
				counter += out[i][j];
			}
		}
		for(int i = 0; i <= 25; i++) {
			for(int j = 0; j <= 25; j++) {
				out[i][j] = out[i][j]/counter*100;
			}
		}
	return out;
	}

	//uses the frobenius norm to calculate distance between matrices 
	public static double getDistance(double[][] arrOne, double[][] arrTwo) {
		double distance = 0;
		for(int i = 0; i <= 25; i++) {
			for(int j = 0; j <= 25; j++) {
				double valOne = Math.abs(arrOne[i][j] - arrTwo[i][j]);
				distance += Math.pow(valOne, P_VAL);
			}
		}
	double finalDistance = Math.pow(distance, (1/P_VAL));
	return finalDistance; 
	}

	//generates an array of the distance user input has to each of the 
	//7 languages
	public static double[] getDistanceArray(double[][] arrInput) throws 
												FileNotFoundException {
		double[][] english = getMatrix("english.txt");
		double[][] italian = getMatrix("italian.txt");
		double[][] spanish = getMatrix("spanish.txt");
		double[][] dutch = getMatrix("dutch.txt");
		double[][] french = getMatrix("french.txt");
		double[][] german = getMatrix("german.txt");
		double[][] norwegian = getMatrix("norwegian.txt");
		double eD = getDistance(english, arrInput);
		double iD = getDistance(italian, arrInput);
		double sD = getDistance(spanish, arrInput);
		double dD = getDistance(dutch, arrInput);
		double fD = getDistance(french, arrInput);
		double gD = getDistance(german, arrInput);
		double nD = getDistance(norwegian, arrInput);
		double[] distances = {eD, iD, sD, dD, fD, gD, nD};
		return distances;
	}

	//provides a guess for the top 3 closest languages 
	public static String guessLang(double[][] input) throws 
									FileNotFoundException {
		double[] distances = getDistanceArray(input);
		String[] languages = {"English", "Italian", "Spanish", "Dutch", 
									"French", "German", "Norwegian"};
		//these empty arrays will store the top 3 languages and their
		//distance values
		double[] ordDistances = new double[3];
		String[] ordLangs = new String[3];

		//selection sort iterated 3x
		for(int i = 0; i <= 2; i++) {
			int b = getSmallest(distances);
			ordDistances[i] = distances[b];
			ordLangs[i] = languages[b];
			distances[b] = 10000;
		}

		String output = 
		String.format("The input is most likely in %s with a distance score of %.2f. ", 
														ordLangs[0], ordDistances[0]);
		output += 
		String.format("The next most likely choices are: \n%s with distance score %.2f (+%.2f) \n%s with distance score %.2f (+%.2f)", 
		ordLangs[1], ordDistances[1], ordDistances[1] - ordDistances[0], 
		ordLangs[2], ordDistances[2], ordDistances[2] - ordDistances[0]); 
	    
		return output;
	}

	//useful method to return the index of the smallest value in the array
	//of distances 
	public static int getSmallest(double[] in) {
		double start = 10000;
		int ind = 0;
		for(int j = 0; j < in.length; j++) {
        		if(in[j] < start) {
        			start = in[j];
        			ind = j;
        		}
        	}
		return ind;
	}
		
	//this method essentially removes accents from characters - we tried to 
	//think of a better way but couldn't, and Google didn't offer much of 
	//use. 
	public static char correctChars(char first) {
		if((int) first >= 224 && (int) first <= 230) {
			return 'a';
		}
		if((int) first == 231) {
			return 'c';
		}
 		if((int) first >= 232 && (int) first <= 235) {
 			return 'e';
 		}
  		if((int) first >= 236 && (int) first <= 239) {
 			return 'i';
 		}
 		if(((int) first == 240) || ((int) first >= 242 && (int) first <= 246) 
 												|| (int) first == 248) {
 			return 'o';
 		}
 		if((int) first == 241) {
 			return 'n';
 		}
 		if((int) first >= 249 && (int) first <= 252) {
 			return 'u';
 		}
 		if((int) first == 225) {
 			return 's';
 		} else {
 			return first;
 		}

	}

	//creating labels that have some information about each language.
	public static String[] label() {
		String[] s = {"English is a West Germanic language, with influences from the Romance languages as well.", 
		"Italian is a Romance language, and considered one of the closest to Latin.", 
		"Spanish is a Romance language with mostly Latin, but some Greek and Arab influences.", 
		"Dutch is a West Germanic language considered to be roughly in between its close relatives German and English.", 
        "French is a Romance language that was also influenced by Celtic languages.", 
        "German is a West Germanic language that borrows words from Latin and Greek and, to a lesser extent, French and English.", 
        "Norwegian is a North Germanic language. It has two official written forms; this program identifies Nynorsk."};
      	return s; 
	}
}