package de.alf.helper;

import de.alf.logging.Logger;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Helper to perform tasks with urls and paths
 *
 * @author Alf
 * @version 1.0
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
	 * @param gSyncPath path to the rosenrudel path in Roaming (standard: %AppData%/sals-rosenrudel)
	 * @param pathKeys  url suffix
	 * @return Key: Url, Value: Path
	 */
	public static Map<String, String> buildUrlPathMap(String baseUrl, String gSyncPath, ArrayList<String> pathKeys) {
		if (!gSyncPath.endsWith(SLASH)) {
			gSyncPath += SLASH;
		}

		Map<String, String> urlPathMapping = new HashMap<>();
		for (String path : pathKeys) {
			String localPath = moveToSubDirectory(path);
			if (path != null) {
				urlPathMapping.put(baseUrl + path, replaceWrongSlashes(gSyncPath + localPath));
			}
		}
		return urlPathMapping;
	}

	/**
	 * Tries to match the given path to an sub directory
	 *
	 * @param savePath path
	 * @return the path with subdirectory or null if {@link PathHelper#isSkipNonMatchable()} returns true
	 */
	private static String moveToSubDirectory(String savePath) {
		Integer subDirectory = evaluateSubDirectory(savePath);
		if (matchableSubDirectories.stream().anyMatch(subDirectory::equals)) {
			return subDirectory + SLASH + savePath;
		} else if (isSkipNonMatchable()) {
			Logger.logWarning("Skipping " + savePath);
			return null;
		} else {
			return "/manualDownload" + SLASH + savePath;
		}
	}

	/**
	 * Replaces all //, \ and \\ with /
	 *
	 * @param path which should be reworked
	 * @return path with replaced shlashes
	 */
	private static String replaceWrongSlashes(String path) {
		return path.replaceAll("(//|\\\\|\\\\\\\\)", SLASH);
	}

	private static Integer evaluateSubDirectory(String savePath) {
		if (zero.stream().anyMatch(savePath::startsWith)) {
			return 0;
		} else if (two.stream().anyMatch(savePath::startsWith)) {
			return 2;
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

	private static final List<Integer> matchableSubDirectories = Arrays.asList(0, 2, 6);

	private static final List<String> zero = Arrays.asList("@3denEnhanced", "@ace", "@ACEX", "@ace_nouniformrestrictions",
			"@ace_particles", "@ace_tracers", "@Achilles", "@ACRE2", "@acre_sys_gm", "@AdvancedSlingLoading",
			"@AdvancedTowing", "@Align", "@BoxLoader", "@BoxloaderACE", "@BrushClearing", "@CBA_A3", "@CUPTerrainsACEComp",
			"@CUPTerrains_Core", "@DismountWhereYouLook", "@dznExtendedJamming", "@EasyTrack", "@ExtendedFortifications",
			"@GRAD_Trenches", "@Immerse", "@InteriorsForCUP", "@LAMBS_Danger.fsm", "@LAMBS_suppression", "@L_climb",
			"@MrSanchezHeadlamps", "@PaddleMod", "@PLPMarkers", "@RR_audio", "@RR_babe_WOS", "@RR_backwardsComp",
			"@RR_CBASettings", "@RR_commons_resources", "@RR_Persistence_Client", "@RR_wallAvoidance", "@rspncaves",
			"@Suppress", "@VET_Unflipping", "@vurtualsCarSeatAndStretcher", "@ZeusEnhanced");

	private static final List<String> two = Arrays.asList("@3CB_Factions", "@@Horror_Mod", "@ACE3__BWMod_Compatibility",
			"@ace_compat_rhs_afrf3", "@ace_compat_rhs_gref3", "@ace_compat_rhs_usf3", "@AfricanRebels", "@Anizay",
			"@Beketov", "@Bozcaada", "@Bundeswehr_Kleiderkammer_PBW_", "@BWMod", "@CUPMaps2v0", "@Desert Battlegrounds",
			"@Em_Buildings", "@Esseker_Fixed", "@FapovoIsland", "@Hellanmaa", "@hl2Zombies", "@hlEcho", "@Ihantala",
			"@Ihantala_Winter", "@Jbad", "@Kidal", "@Kujari", "@Kunduz_Afghanistan", "@LYTHIUM", "@MBGBuildingsRosche",
			"@MCAGCC29Palms", "@PLP_Containers", "@Pulau", "@RHSAFRF", "@RHSGREF", "@RHSPKL", "@RHSSAF", "@RHSUSAF",
			"@RKSLMk5LAndingCraft", "@Rosche", "@RR_RHS_sounds", "@Ruegen", "@Ruha", "@Summa", "@TRIA", "@Vinjesvingen",
			"@Virolahti");

	private static final List<String> six = Arrays.asList("@IFA3_AIO_LITE", "@NorthernFronts", "@NorthernFrontsTerrains");

	public static void printKnownSubDirectories() {
		System.out.println("Directories :" + matchableSubDirectories.stream().map(Object::toString).collect(Collectors.joining(", ")));
		System.out.println("0: " + String.join(", ", zero));
		System.out.println("2: " + String.join(", ", two));
		System.out.println("6: " + String.join(", ", six));
	}
}
