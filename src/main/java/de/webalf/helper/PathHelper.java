package de.webalf.helper;

import de.webalf.logging.Logger;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Helper to perform tasks with urls and paths
 *
 * @author Alf
 * @version 2.0
 * @since 14.03.2020
 */
public class PathHelper {

	private static final String SLASH = "/";

	/**
	 * Checks if given path is a directory
	 *
	 * @param path toi check
	 * @return true if directory
	 */
	public static boolean validatePath(String path) {
		if (path == null || path.isEmpty()) {
			return false;
		}
		return new File(path).isDirectory();
	}

	/**
	 * Maps download Urls to the file system paths
	 *
	 * @param baseUrl   url start
	 * @param gSyncPath path to the rosenrudel path in Roaming (standard: %APPDATA%/sals)
	 * @param pathKeys  url suffix
	 * @return Key: Url, Value: Path
	 */
	public static Map<String, String> buildUrlPathMap(String baseUrl, String gSyncPath, List<String> pathKeys) {
		if (!gSyncPath.endsWith(SLASH)) {
			gSyncPath += SLASH;
		}

		Map<String, String> urlPathMapping = new HashMap<>();
		for (String path : pathKeys) {
			String localPath = moveToSubDirectory(path);
			if (path != null) {
				urlPathMapping.put(baseUrl + encode(path), replaceWrongSlashes(gSyncPath + localPath));
			}
		}
		return urlPathMapping;
	}

	/**
	 * Tries to match the given path to a subdirectory
	 *
	 * @param savePath path
	 * @return the path with subdirectory or null if {@link PathHelper#isSkipNonMatchable()} returns true
	 */
	private static String moveToSubDirectory(String savePath) {
		Integer subDirectory = evaluateSubDirectory(savePath);
		if (matchableSubDirectories.stream().anyMatch(subDirectory::equals)) {
			return "1" + SLASH + subDirectory + SLASH + savePath;
		} else if (isSkipNonMatchable()) {
			Logger.logWarning("Skipping " + savePath);
			return null;
		} else {
			return SLASH + "manualDownload" + SLASH + savePath;
		}
	}

	private static String encode(String path) {
		return Arrays.stream(path.split(SLASH)).map(splitted -> splitted.replace(" ", "%20")).collect(Collectors.joining(SLASH));
	}

	/**
	 * Replaces all //, \ and \\ with /
	 *
	 * @param path which should be reworked
	 * @return path with replaced slashes
	 */
	private static String replaceWrongSlashes(String path) {
		return path.replaceAll("(//|\\\\|\\\\\\\\)", SLASH);
	}

	private static Integer evaluateSubDirectory(String savePath) {
		if (one.stream().anyMatch(savePath::startsWith)) {
			return 1;
		} else if (five.stream().anyMatch(savePath::startsWith)) {
			return 5;
		} else if (six.stream().anyMatch(savePath::startsWith)) {
			return 6;
		} else {
			return -1;
		}
	}

	private static boolean skipNonMatchable;

	public static boolean isSkipNonMatchable() {
		return skipNonMatchable;
	}

	public static void setSkipNonMatchable(boolean skipNonMatchable) {
		PathHelper.skipNonMatchable = skipNonMatchable;
	}

	private static final List<Integer> matchableSubDirectories = Arrays.asList(1, 5, 6);

	private static final List<String> one = Arrays.asList("@3denEnhanced", "@ace/", "@ace_nouniformrestrictions",
			"@ace_particles", "@ace_realisticdispersion", "@ace_tracers", "@ACRE Animations", "@ACRE2", "@acre_sys_gm",
			"@AdvancedSlingLoading", "@Align", "@AWESome", "@BoxLoader", "@BoxloaderACE", "@BrighterFlares", "@BrushClearing",
			"@CBA_A3", "@DismountWhereYouLook", "@dznExtendedJamming", "@EasyTrack", "@ExtendedFortifications",
			"@GRAD_Trenches", "@Gruppe Adler Pace Count Beads", "@Helicopter Turbulence", "@Immerse", "@LAMBS_Danger.fsm",
			"@LAMBS_suppression", "@L_climb", "@MrSanchezHeadlamps", "@PaddleMod", "@PLPMarkers", "@RideWhereYouLook",
			"@RR_audio", "@RR_babe_WOS", "@RR_backwardsComp", "@RR_CBASettings", "@RR_Commons_Resources", "@RR_mapStuff",
			"@RR_Persistence_Client", "@RR_wallAvoidance", "@rspncaves", "@Suppress", "@VET_Unflipping",
			"@vurtualsCarSeatAndStretcher", "@Zeus Enhanced - ACE3 Compatibility", "@ZeusEnhanced/");

	private static final List<String> five = List.of("@Blastcore Murr Edition");

	private static final List<String> six = Arrays.asList("@CUP ACE3 Compatibility Addon - Terrains", "@CUP Terrains - Core", "@CUP Terrains - Maps 2.0", "@InteriorsForCUP");

	public static void printKnownSubDirectories() {
		System.out.println("Directories :" + matchableSubDirectories.stream().map(Object::toString).collect(Collectors.joining(", ")));
		System.out.println("1: " + String.join(", ", one));
		System.out.println("5: " + String.join(", ", five));
		System.out.println("6: " + String.join(", ", six));
	}
}
