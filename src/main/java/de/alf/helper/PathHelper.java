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
		} else if (three.stream().anyMatch(savePath::startsWith)) {
			return 3;
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

	private static final List<Integer> matchableSubDirectories = Arrays.asList(0, 2, 3, 5, 6);

	private static final List<String> zero = Arrays.asList("@3denEnhanced", "@Immerse", "@ace", "@InteriorsForCUP",
			"@ace_compat_rhs_afrf3", "@ITC_Land_Systems", "@ace_compat_rhs_gref3",
			"@ITC_Land_Systems_RHS_AFRF_Compatibility", "@ace_compat_rhs_usf3",
			"@ITC_Land_Systems_RHS_USAF_Compatibility", "@ace_nouniformrestrictions", "@L_climb", "@ace_particles",
			"@LAMBS_Danger.fsm", "@ace_tracers", "@LAMBS_suppression", "@ACEX", "@MrSanchezHeadlamps", "@Achilles",
			"@PaddleMod", "@acre_sys_gm", "@PLPMarkers", "@ACRE2", "@RHSAFRF", "@AdvancedSlingLoading", "@RHSGREF",
			"@AdvancedTowing", "@RHSSAF", "@Align", "@RHSUSAF", "@BoxLoader", "@RR_audio", "@BoxloaderACE",
			"@RR_babe_WOS", "@BrushClearing", "@RR_CBASettings", "@CBA_A3", "@RR_commons_resources",
			"@CUPTerrains_Core", "@RR_wallAvoidance", "@CUPTerrainsACEComp", "@rspncaves", "@DismountWhereYouLook",
			"@Suppress", "@dznExtendedJamming", "@VET_Unflipping", "@EasyTrack", "@vurtualsCarSeatAndStretcher",
			"@ExtendedFortifications", "@ZeusEnhanced", "@GRAD_Trenches");

	private static final List<String> two = Arrays.asList("@@Horror_Mod", "@CUPTerrains_Core", "@Jbad", "@RHSUSAF",
			"@3CB_Factions", "@CUPTerrainsACEComp", "@Kidal", "@RKSLMk5LAndingCraft", "@ace_compat_rhs_afrf3",
			"@Desert Battlegrounds", "@Kujari", "@Rosche", "@ace_compat_rhs_gref3", "@Em_Buildings",
			"@Kunduz_Afghanistan", "@RR_RHS_sounds", "@ace_compat_rhs_usf3", "@Esseker_Fixed", "@LYTHIUM", "@Ruegen",
			"@ACE3__BWMod_Compatibility", "@FapovoIsland", "@MBGBuildingsRosche", "@Ruha", "@AfricanRebels",
			"@Hellanmaa", "@MCAGCC29Palms", "@Summa", "@Anizay", "@hlEcho", "@PLP_Containers", "@TRIA", "@Beketov",
			"@Ihantala", "@Pulau", "@Vinjesvingen", "@Bozcaada", "@Ihantala_Winter", "@RHSAFRF", "@Virolahti",
			"@Bundeswehr_Kleiderkammer_PBW_", "@InteriorsForCUP", "@RHSGREF", "@BWMod",
			"@ITC_Land_Systems_RHS_AFRF_Compatibility", "@RHSPKL", "@CUPMaps2v0",
			"@ITC_Land_Systems_RHS_USAF_Compatibility", "@RHSSAF");

	private static final List<String> three = Arrays.asList("@[Discontinued]Fox F.D.N.Y Pack", "@KA Suitcase Nuke",
			"@[Discontinued]Fox NYPD Pack", "@KA Weapons Pack NEW", "@[MELB] Mission Enhanced Little Bird",
			"@KokaKolaA3's Gesture Pack (Animations)", "@A3_Funiture_Pack", "@Little Yacht", "@A3F_Factory", "@Luke",
			"@Accessoire", "@Military Gear Pack", "@ace", "@Multi-play Uniforms", "@ACE 3 Extension (Gestures)",
			"@Office_Building", "@acex", "@OneUnited", "@Antonov An-2", "@PLP_Addons", "@ANZINS Sun",
			"@Project Infinite - All in one", "@ANZINS Terrain",
			"@Radio Animations for Advanced Combat Radio Enviroment 2", "@AR Hand Weapon Detector", "@RH_Pistol_pack",
			"@AR Megaphone", "@RHSUSAF", "@Arganiny's Civilian Project", "@RKSL Studios- Attachments v3.01",
			"@Arma3_Coustum_Buildings", "@Sabs Ultralight", "@BUGATTI DIVO - COP", "@Sac", "@BUGATTI VEYRON SS",
			"@Santa Claus", "@CBA_A3", "@Schert´s Firestation", "@Charlieco", "@Schert´s Hospital",
			"@CYBER PUNK POLICE", "@skn_nbc_units", "@Eden_Extended_Objekts", "@Specter Armaments", "@ENFORCER",
			"@TAC VESTS", "@Enhanced Movement", "@Tactical Position Ready", "@Enhanced Soundscape", "@Tanoa_Bridges",
			"@Fl0's ACE³ Interactions", "@task_force_radio", "@Gilet-Veste", "@Uriki's Ballistic Mask", "@HAZMAT Corps",
			"@Uriki's Mask Mod", "@JSRS SOUNDMOD", "@Urikis_Mission_Items",
			"@JSRS SOUNDMOD - RHS USAF Mod Pack Sound Support", "@V12 BIKE PACK", "@KA Specially Equipped Pack",
			"@V12_BikerPack", "@KA Specially Equipped Pack error fix"
	);

	private static final List<String> five = Collections.singletonList("@ACEX");

	private static final List<String> six = Arrays.asList("@IFA3_AIO_LITE", "@NorthernFronts", "@NorthernFrontsTerrains");

	public static void printKnownSubDirectories() {
		System.out.println("Directories :" + matchableSubDirectories.stream().map(Object::toString).collect(Collectors.joining(", ")));
		System.out.println("0: " + String.join(", ", zero));
		System.out.println("2: " + String.join(", ", two));
		System.out.println("3: " + String.join(", ", three));
		System.out.println("5: " + String.join(", ", five));
		System.out.println("6: " + String.join(", ", six));
	}
}
