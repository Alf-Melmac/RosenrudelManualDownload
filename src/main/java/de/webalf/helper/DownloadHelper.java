package de.webalf.helper;

import de.webalf.logging.Logger;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Helper to downloads files from Urls and save them to file system
 *
 * @author Alf
 * @version 1.0
 * @since 14.03.2020
 */
public class DownloadHelper {

	/**
	 * Downloads each element of the map from the url given in the key to the corresponding path given as value
	 *
	 * @param urlPathMapping         Urls with corresponding paths to save to
	 * @param overwriteExistingFiles true if files should be overwritten
	 */
	public static void download(Map<String, String> urlPathMapping, boolean overwriteExistingFiles) {
		urlPathMapping.forEach((url, path) -> DownloadHelper.download(url, path, overwriteExistingFiles));
	}

	/**
	 * Fetches a file from the given url and save it to the given path
	 *
	 * @param downloadUrl            to get the file from
	 * @param savePath               local path to save to
	 * @param overwriteExistingFiles true if excising file in path should be overwritten
	 */
	private static void download(String downloadUrl, String savePath, boolean overwriteExistingFiles) {
		URL url;
		try {
			url = new URL(downloadUrl);
		} catch (MalformedURLException e) {
			Logger.logSevere("Error while parsing url: " + downloadUrl, e);
			return;
		}

		File file = new File(savePath);
		if (file.isFile() && !overwriteExistingFiles) {
			Logger.logWarning("File already exists in: " + savePath);
			return;
		}

		try {
			Logger.logInfo("Starting download from: " + downloadUrl);
			FileUtils.copyURLToFile(url, file);
			Logger.logInfo("Saved file to: " + savePath);
		} catch (IOException e) {
			Logger.logSevere("Error while copying file from: " + downloadUrl + " to: " + savePath, e);
		}
	}
}
