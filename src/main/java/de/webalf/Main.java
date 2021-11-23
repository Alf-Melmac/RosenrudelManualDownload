package de.webalf;

import de.webalf.helper.DownloadHelper;
import de.webalf.helper.PathHelper;
import de.webalf.helper.ValueExtractor;
import de.webalf.logging.Logger;

import java.util.*;
import java.util.logging.Level;

/**
 * @author Alf
 * @version 2.0
 * @since 14.03.2020
 */
public class Main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Logger.setLogLevel(Level.OFF);

		if (args != null && args[0].equalsIgnoreCase("dev")) {
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
					}
					config = false;
				}
			} catch (InputMismatchException ignored) {
			}
		}

		System.out.println("Einstellungen:");

		printSeparator();

		//User input save Path
		String gSyncPath = null;
		while (!PathHelper.validatePath(gSyncPath)) {
			System.out.println("Speicherpfad (z.B. C:\\Users\\{{USERNAME}}\\AppData\\Roaming\\sals\\gSync): ");
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
		System.out.println("'Lite' eingeben für 'https://s3-1.sals.app/sals-1-base/', 'https://s3-1.sals.app/sals-1-blastcore/' und 'https://s3-1.sals.app/sals-1-cup/'");
		System.out.println("Urls eingeben, danach 'done' eintippen");
		ArrayList<String> baseUrls = new ArrayList<>();
		String input;
		while (scanner.hasNextLine()) {
			input = scanner.nextLine();
			if (input.equalsIgnoreCase("Lite")) {
				baseUrls.add("https://s3-1.sals.app/sals-1-base/");
				baseUrls.add("https://s3-1.sals.app/sals-1-blastcore/");
				baseUrls.add("https://s3-1.sals.app/sals-1-cup/");
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
		System.out.println("Bucket Urls: " + baseUrls);

		printSeparator();

		for (String baseUrl : baseUrls) {
			final List<String> pathKeys = ValueExtractor.getValues(baseUrl, "Key");
			final Map<String, String> urlPathMapping = PathHelper.buildUrlPathMap(baseUrl, gSyncPath, pathKeys);
			System.out.println("Download von " + baseUrl + " gestartet.");
			DownloadHelper.download(urlPathMapping, overwriteExisting);
			System.out.println("Download von " + baseUrl + " beendet.");
		}

		System.out.println("Download abgeschlossen. Nun den Sals Launcher öffnen, dass gewünschte Repository auswählen und den Download der übriggeblieben Restdateien starten. Dieser Download sollte ziemlich klein sein.");
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
