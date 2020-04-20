package de.alf;

import de.alf.helper.DownloadHelper;
import de.alf.helper.PathHelper;
import de.alf.helper.ValueExtractor;
import de.alf.logging.Logger;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;

/**
 * @author Alf
 * @version 1.0
 * @since 14.03.2020
 */
public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);

		if (args != null && args.length == 1) {
			if (args[0].equalsIgnoreCase("true")) {
				System.out.println("Dev options");
				System.out.println("0: Set debug level, 1: print know subdirectories and mods in them");
				try {
					boolean config = true;
					while (scanner.hasNextLine() && config) {
						switch (scanner.nextInt()) {
							case 0:
								System.out.println("Level?");
								if (scanner.hasNextLine()) {
									scanner.nextLine();
									Logger.setLogLevel(Level.parse(scanner.nextLine().toUpperCase()));
								}
								break;
							case 1:
								PathHelper.printKnownSubDirectories();
								break;
							default:
								config = false;
						}
					}
				} catch (InputMismatchException ignored) {
				}
			}
		}

		System.out.println("Einstellungen:");

		printSeparator();

		//User input save Path
		String gSyncPath = null;
		while (!PathHelper.validatePath(gSyncPath)) {
			System.out.println("Speicherpfad (z.B. C:\\Users\\{USERNAME}}\\AppData\\Roaming\\sals-rosenrudel\\gSync): ");
			gSyncPath = scanner.nextLine();
		}

		printSeparator();

		//User input overwrite existing files
		boolean overwriteExisting;
		System.out.println("Sollen bereits vorhandene Dateien ersetzt werden? (Default: 'nein', z.B. ja)");
		overwriteExisting = polarQuestion(scanner.nextLine());

		printSeparator();

		//User input skip non matchable files
		System.out.println("Sollen Dateien die keinem Unterordner zugeordnet werden können übersprungen werden? (Default: 'nein', z.B. ja)");
		PathHelper.setSkipNonMatchable(polarQuestion(scanner.nextLine()));

		printSeparator();

		//User input baseUrls
		System.out.println("Repository oder Bucket Urls");
		System.out.println("'Standard' eingeben für 'https://s3-1.sals.app/rosenrudel-base/' und 'https://s3-1.sals.app/rosenrudel-standard/'");
		System.out.println("'WW2' eingeben für 'https://s3-1.sals.app/rosenrudel-ww2/'");
		System.out.println("Urls eingeben, danach 'done' eintippen");
		ArrayList<String> baseUrls = new ArrayList<>();
		String input;
		while (scanner.hasNextLine()) {
			input = scanner.nextLine();
			if (input.equalsIgnoreCase("Standard")) {
				baseUrls.add("https://s3-1.sals.app/rosenrudel-base/");
				baseUrls.add("https://s3-1.sals.app/rosenrudel-standard/");
				break;
			} else if (input.equalsIgnoreCase("WW2")) {
				baseUrls.add("https://s3-1.sals.app/rosenrudel-ww2/");
				break;
			} else if (!input.equalsIgnoreCase("done")) {
				baseUrls.add(input);
			} else {
				break;
			}
		}

		printSeparator();
		scanner.close();

		//Show settings to user
		System.out.println("Folgende Einstellungen wurden gespeichert:");
		System.out.println("Speicherpfad: " + gSyncPath);
		if (overwriteExisting) {
			System.out.println("Vorhandene Dateien werden überschrieben.");
		} else {
			System.out.println("Vorhandene Dateien werden nicht überschrieben.");
		}
		if (PathHelper.isSkipNonMatchable()) {
			System.out.println("Unterordner ohne Zuordnung werden übersprungen.");
		} else {
			System.out.println("Unterordner ohne Zuordnung werden unter " + gSyncPath + "\\manualDownload abgelegt. Diese müssen manuell eingeordnet werden.");
		}
		System.out.println("Bucket Urls: " + baseUrls.toString());

		printSeparator();

		for (String baseUrl : baseUrls) {
			final ArrayList<String> pathKeys = ValueExtractor.getValues(baseUrl, "Key");
			final Map<String, String> urlPathMapping = PathHelper.buildUrlPathMap(baseUrl, gSyncPath, pathKeys);
			DownloadHelper.download(urlPathMapping, overwriteExisting);
		}

	}

	private static void printSeparator() {
		System.out.println("-----");
	}

	private static boolean polarQuestion(String userInput) {
		switch (userInput.toLowerCase()) {
			case "true":
			case "yes":
			case "1":
			case "y":
			case "ja":
				return true;
			default:
				return false;
		}
	}
}
