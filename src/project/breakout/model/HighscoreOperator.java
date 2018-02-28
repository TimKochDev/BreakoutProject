package project.breakout.model;

import java.awt.List;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * This class manages the access to the highscore ranking.
 *
 */
public class HighscoreOperator {
	private String FILE_NAME = "HighscoreRanking.txt";
	private List highscoreNames = new List();
	private List highscoreValues = new List();
	private boolean readingSuccessful = false;

	public HighscoreOperator() {
		try {
			BufferedReader rd = new BufferedReader(new FileReader(FILE_NAME));
			while (true) {
				String highscoreName = rd.readLine();
				String highscoreValue = rd.readLine();
				if (highscoreName == null) {
					rd.close();
					break;
				}

				highscoreNames.add(highscoreName);
				highscoreValues.add(highscoreValue);

			}			
			readingSuccessful = true;
		} catch (Exception e) {
			System.out.println("Could not load or read " + FILE_NAME);
		}
	}

	public boolean readingSuccessful() {
		return readingSuccessful ;
	}
	
	public List getNamesList() {
		return highscoreNames;

	}

	public List getHighscoreValues() {
		return highscoreValues;
	}
}
