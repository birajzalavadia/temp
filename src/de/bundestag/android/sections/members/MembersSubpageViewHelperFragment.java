package de.bundestag.android.sections.members;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.database.Cursor;
import de.bundestag.android.storage.MembersDatabaseAdapter;

public class MembersSubpageViewHelperFragment {
	public static  String[] cityNames = { "Baden-Württemberg", "Bayern", "Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen", "Mecklenburg-Vorpommern", "Niedersachsen",
			"Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland", "Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thüringen" };

	public static  String[] electionNames = { "001: Flensburg – Schleswig", "002: Nordfriesland – Dithmarschen Nord", "003: Steinburg – Dithmarschen Süd",
			"004: Rendsburg-Eckernförde", "005: Kiel", "006: Plön – Neumünster", "007: Pinneberg", "008: Segeberg – Stormarn-Nord", "009: Ostholstein",
			"010: Herzogtum Lauenburg – Stormarn-Süd", "011: Lübeck", "012: Wismar – Nordwestmecklenburg – Parchim", "013: Schwerin – Ludwigslust", "014: Rostock",
			"015: Stralsund – Nordvorpommern – Rügen", "016: Greifswald – Demmin – Ostvorpommern", "017: Bad Doberan – Güstrow – Müritz",
			"018: Neubrandenburg – Mecklenburg-Strelitz – Uecker-Randow", "019: Hamburg-Mitte", "020: Hamburg-Altona", "021: Hamburg-Eimsbüttel", "022: Hamburg-Nord",
			"023: Hamburg-Wandsbek", "024: Hamburg-Bergedorf – Harburg", "025: Aurich – Emden", "026: Unterems", "027: Friesland – Wilhelmshaven", "028: Oldenburg – Ammerland",
			"029: Delmenhorst – Wesermarsch – Oldenburg-Land", "030: Cuxhaven – Stade II", "031: Stade I – Rotenburg II", "032: Mittelems", "033: Cloppenburg – Vechta",
			"034: Diepholz – Nienburg I", "035: Osterholz – Verden", "036: Rotenburg I – Soltau-Fallingbostel", "037: Harburg", "038: Lüchow-Dannenberg – Lüneburg",
			"039: Osnabrück Land", "040: Stadt Osnabrück", "041: Nienburg II – Schaumburg", "042: Stadt Hannover I", "043: Stadt Hannover II", "044: Hannover-Land I",
			"045: Celle – Uelzen", "046: Gifhorn – Peine", "047: Hameln-Pyrmont – Holzminden", "048: Hannover-Land II", "049: Hildesheim", "050: Salzgitter – Wolfenbüttel",
			"051: Braunschweig", "052: Helmstedt – Wolfsburg", "053: Goslar – Northeim – Osterode", "054: Göttingen", "055: Bremen I", "056: Bremen II – Bremerhaven",
			"057: Prignitz – Ostprignitz-Ruppin – Havelland I", "058: Uckermark – Barnim I", "059: Oberhavel – Havelland II", "060: Märkisch-Oderland – Barnim II",
			"061: Brandenburg an der Havel – Potsdam Mittelmark I – Havelland III – Teltow", "062: Potsdam – Potsdam-Mittelmark II – Teltow-Fläming II",
			"063: Dahme-Spreewald – Teltow-Fläming III – Oberspreewald-Lausitz I", "064: Frankfurt (Oder) – Oder-Spree", "065: Cottbus – Spree-Neiße",
			"066: Elbe-Elster – Oberspreewald-Lausitz II", "067: Altmark", "068: Börde", "069: Harz", "070: Magdeburg", "071: Dessau – Wittenberg", "072: Anhalt", "073: Halle",
			"074: Burgenland – Saalekreis", "075: Mansfeld", "076: Berlin-Mitte", "077: Berlin-Pankow", "078: Berlin-Reinickendorf", "079: Berlin-Spandau – Charlottenburg Nord",
			"080: Berlin-Steglitz – Zehlendorf", "081: Berlin-Charlottenburg – Wilmersdorf", "082: Berlin-Tempelhof – Schöneberg", "083: Berlin-Neukölln",
			"084: Berlin-Friedrichshain – Kreuzberg – Prenzlauer Berg Ost", "085: Berlin-Treptow – Köpenick", "086: Berlin-Marzahn – Hellersdorf",
			"087: Berlin-Lichtenberg – Hohenschönhausen", "088: Aachen", "089: Kreis Aachen", "090: Heinsberg", "091: Düren", "092: Erftkreis I", "093: Euskirchen – Erftkreis II",
			"094: Köln I", "095: Köln II", "096: Köln III", "097: Bonn", "098: Rhein-Sieg-Kreis I", "099: Rhein-Sieg-Kreis II", "100: Oberbergischer Kreis",
			"101: Rheinisch-Bergischer Kreis", "102: Leverkusen – Köln IV", "103: Wuppertal I", "104: Solingen – Remscheid – Wuppertal II", "105: Mettmann I", "106: Mettmann II",
			"107: Düsseldorf I", "108: Düsseldorf II", "109: Neuss I", "110: Mönchengladbach", "111: Krefeld I – Neuss II", "112: Viersen", "113: Kleve", "114: Wesel I",
			"115: Krefeld II – Wesel II", "116: Duisburg I", "117: Duisburg II", "118: Oberhausen – Wesel III", "119: Mülheim – Essen I", "120: Essen II", "121: Essen III",
			"122: Recklinghausen I", "123: Recklinghausen II", "124: Gelsenkirchen", "125: Steinfurt I – Borken I", "126: Bottrop – Recklinghausen III", "127: Borken II",
			"128: Coesfeld – Steinfurt II", "129: Steinfurt III", "130: Münster", "131: Warendorf", "132: Gütersloh", "133: Bielefeld", "134: Herford – Minden-Lübbecke II",
			"135: Minden-Lübbecke I", "136: Lippe I", "137: Höxter – Lippe II", "138: Paderborn", "139: Hagen – Ennepe-Ruhr-Kreis I", "140: Ennepe-Ruhr-Kreis II", "141: Bochum I",
			"142: Herne – Bochum II", "143: Dortmund I", "144: Dortmund II", "145: Unna I", "146: Hamm – Unna II", "147: Soest", "148: Hochsauerlandkreis",
			"149: Siegen-Wittgenstein", "150: Olpe – Märkischer Kreis I", "151: Märkischer Kreis II", "152: Nordsachsen", "153: Leipzig I", "154: Leipzig II", "155: Leipzig Land",
			"156: Meißen", "157: Bautzen I", "158: Görlitz", "159: Sächsische Schweiz – Osterzgerbirge", "160: Dresden I", "161: Dresden II – Bautzen II", "162: Mittelsachsen",
			"163: Chemnitz", "164: Chemnitzer Umland – Erzgebirgskreis II", "165: Erzgebirgskreis I", "166: Zwickau", "167: Vogtlandkreis", "168: Waldeck", "169: Kassel",
			"170: Werra-Meißner – Hersfeld-Rotenburg", "171: Schwalm-Eder", "172: Marburg", "173: Lahn-Dill", "174: Gießen", "175: Fulda", "176: Hochtaunus", "177: Wetterau",
			"178: Rheingau-Taunus – Limburg", "179: Wiesbaden", "180: Hanau", "181: Main-Taunus", "182: Frankfurt am Main I", "183: Frankfurt am Main II", "184: Groß-Gerau",
			"185: Offenbach", "186: Darmstadt", "187: Odenwald", "188: Bergstraße", "189: Eichsfeld – Nordhausen – Unstrut-Hainich-Kreis I",
			"190: Eisenach – Wartburgkreis – Unstrut-Hainich-Kreis II", "191: Kyffhäuserkreis – Sömmerda – Weimarer Land I", "192: Gotha – Ilm-Kreis",
			"193: Erfurt – Weimar – Weimarer Land II", "195: Gera – Jena – Saale-Holzland-Kreis", "195: Greiz – Altenburger Land",
			"196: Sonneberg – Saalfeld-Rudolstadt – Saale-Orla-Kreis", "197: Suhl – Schmalkalden-Meiningen – Hildburghausen", "198: Neuwied", "199: Ahrweiler", "200: Koblenz",
			"201: Mosel/Rhein-Hunsrück", "202: Kreuznach", "203: Bitburg", "204: Trier", "205: Montabaur", "206: Mainz", "207: Worms", "208: Ludwigshafen/Frankenthal",
			"209: Neustadt – Speyer", "210: Kaiserslautern", "211: Pirmasens", "212: Südpfalz", "213: Altötting", "214: Erding – Ebersberg", "215: Freising",
			"216: Fürstenfeldbruck", "217: Ingolstadt", "218: München-Nord", "219: München-Ost", "220: München-Süd", "221: München-West/Mitte", "222: München-Land",
			"223: Rosenheim", "224: Starnberg", "225: Traunstein", "226: Weilheim", "227: Deggendorf", "228: Landshut", "229: Passau", "230: Rottal-Inn", "231: Straubing",
			"232: Amberg", "233: Regensburg", "234: Schwandorf", "235: Weiden", "236: Bamberg", "237: Bayreuth", "238: Coburg", "239: Hof", "240: Kulmbach", "241: Ansbach",
			"242: Erlangen", "243: Fürth", "244: Nürnberg-Nord", "245: Nürnberg-Süd", "246: Roth", "247: Aschaffenburg", "248: Bad Kissingen", "249: Main-Spessart",
			"250: Schweinfurt", "251: Würzburg", "252: Augsburg-Stadt", "253: Augsburg-Land", "254: Donau-Ries", "255: Neu-Ulm", "256: Oberallgäu", "257: Ostallgäu",
			"258: Stuttgart I", "259: Stuttgart II", "260: Böblingen", "261: Esslingen", "262: Nürtingen", "263: Göppingen", "264: Waiblingen", "265: Ludwigsburg",
			"266: Neckar-Zaberv", "267: Heilbronn", "268: Schwäbisch Hall – Hohenlohe", "269: Backnang – Schwäbisch Gmünd", "270: Aalen – Heidenheim", "271: Karlsruhe-Stadt",
			"272: Karlsruhe-Land", "273: Rastatt", "274: Heidelberg", "275: Mannheim", "276: Odenwald – Tauber", "277: Rhein-Neckar", "278: Bruchsal – Schwetzingen",
			"279: Pforzheim", "280: Calw", "281: Freiburg", "282: Lörrach – Müllheim", "283: Emmendingen – Lahr", "284: Offenburg", "285: Rottweil – Tuttlingen",
			"286: Schwarzwald-Baar", "287: Konstanz", "288: Waldshut", "289: Reutlingen", "290: Tübingen", "291: Ulm", "292: Biberach", "293: Bodensee", "294: Ravensburg",
			"295: Zollernalb – Sigmaringen", "296: Saarbrücken", "297: Saarlouis", "298: Sankt Wendel", "299: Homburg" };

	/**
	 * Creates hashmap list of fractions with number of members in each.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList<HashMap<String, Object>> createMembersFractions(Activity activity) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		Cursor fractionsCDUCSUCursor = membersDatabaseAdapter.fetchAllFractions("CDU/CSU");
		Cursor fractionsSPDCursor = membersDatabaseAdapter.fetchAllFractions("SPD");
		Cursor fractionsFDPCursor = membersDatabaseAdapter.fetchAllFractions("FDP");
		Cursor fractionsDIELINKECursor = membersDatabaseAdapter.fetchAllFractions("DIE LINKE.");
		Cursor fractionsBUNDNISGRUNENCursor = membersDatabaseAdapter.fetchAllFractions("BÜNDNIS 90/DIE GRÜNEN");

		int CDUCSUFractions = fractionsCDUCSUCursor.getCount();
		int SPDFractions = fractionsSPDCursor.getCount();
		int FDPFractions = fractionsFDPCursor.getCount();
		int DIELINKEFractions = fractionsDIELINKECursor.getCount();
		int DIEBUNDNISGRUNENFractions = fractionsBUNDNISGRUNENCursor.getCount();

		fractionsCDUCSUCursor.close();
		fractionsSPDCursor.close();
		fractionsFDPCursor.close();
		fractionsDIELINKECursor.close();
		fractionsBUNDNISGRUNENCursor.close();

		ArrayList<HashMap<String, Object>> fractions = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "CDU/CSU");
		hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, String.valueOf(CDUCSUFractions));
		hashMap.put(MembersFractionListAdapter.KEY_IMAGEURL, "cdu_csu");
		fractions.add(hashMap);

		hashMap = new HashMap<String, Object>();
		hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "SPD");
		hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, String.valueOf(SPDFractions));
		hashMap.put(MembersFractionListAdapter.KEY_IMAGEURL, "spd");
		fractions.add(hashMap);

		hashMap = new HashMap<String, Object>();
		hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "FDP");
		hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, String.valueOf(FDPFractions));
		hashMap.put(MembersFractionListAdapter.KEY_IMAGEURL, "fdp");
		fractions.add(hashMap);

		hashMap = new HashMap<String, Object>();
		hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "DIE LINKE.");
		hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, String.valueOf(DIELINKEFractions));
		hashMap.put(MembersFractionListAdapter.KEY_IMAGEURL, "die_linke");
		fractions.add(hashMap);

		hashMap = new HashMap<String, Object>();
		hashMap.put(MembersFractionListAdapter.KEY_FRACTION_NAME, "BÜNDNIS 90/DIE GRÜNEN");
		hashMap.put(MembersFractionListAdapter.KEY_NUMBERS, String.valueOf(DIEBUNDNISGRUNENFractions));
		hashMap.put(MembersFractionListAdapter.KEY_IMAGEURL, "bundnis");
		fractions.add(hashMap);

		membersDatabaseAdapter.close();

		return fractions;
	}

	/**
	 * Creates hashmap list of cities with number of members in each.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList<HashMap<String, Object>> createMembersCities(Activity activity) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		ArrayList<HashMap<String, Object>> cities = new ArrayList<HashMap<String, Object>>();

		Cursor citiesCursor = null;
		HashMap<String, Object> hashMap;
		for (int i = 0; i < cityNames.length; i++) {
			citiesCursor = membersDatabaseAdapter.fetchAllCities(cityNames[i]);

			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersCityListAdapter.KEY_FRACTION_NAME, cityNames[i]);
			hashMap.put(MembersCityListAdapter.KEY_NUMBERS, String.valueOf(citiesCursor.getCount()));
			cities.add(hashMap);
		}
		if (citiesCursor != null) {
			citiesCursor.close();
		}

		membersDatabaseAdapter.close();

		return cities;
	}

	/**
	 * Creates hashmap list of elections.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList<HashMap<String, Object>> createMembersElections(Activity activity) {
		ArrayList<HashMap<String, Object>> elections = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> hashMap;
		for (int i = 0; i < electionNames.length; i++) {
			hashMap = new HashMap<String, Object>();
			hashMap.put(MembersCityListAdapter.KEY_FRACTION_NAME, electionNames[i]);
			elections.add(hashMap);
		}

		return elections;
	}

	/**
	 * Creates cursor with members for the members sub pages.
	 * 
	 * Used by general list fragment.
	 */
	// public static ArrayList createMembersSubpage(Activity activity, int
	// subPageId, int index)
	// {
	// MembersDatabaseAdapter membersDatabaseAdapter = new
	// MembersDatabaseAdapter(activity);
	// membersDatabaseAdapter.open();
	//
	// Cursor membersCursor = membersDatabaseAdapter.fetchAllMembers();
	// String selected = "";
	//
	// if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_FRACTION)
	// {
	// if (index == 0)
	// {
	// // membersCursor =
	// // membersDatabaseAdapter.fetchFractionMembersTwo("CDU", "CSU");
	// membersCursor = membersDatabaseAdapter.fetchFractionMembers("CDU/CSU");
	// selected = "CDU/CSU";
	// }
	// else if (index == 1)
	// {
	// membersCursor = membersDatabaseAdapter.fetchFractionMembers("SPD");
	// selected = "SPD";
	// }
	// else if (index == 2)
	// {
	// membersCursor = membersDatabaseAdapter.fetchFractionMembers("FDP");
	// selected = "FDP";
	// }
	// else if (index == 3)
	// {
	// membersCursor =
	// membersDatabaseAdapter.fetchFractionMembers("DIE LINKE.");
	// selected = "DIE LINKE.";
	// }
	// else if (index == 4)
	// {
	// // membersCursor =
	// // membersDatabaseAdapter.fetchFractionMembersTwo("BÜNDNIS 90",
	// // "DIE GRÜNEN");
	// membersCursor =
	// membersDatabaseAdapter.fetchFractionMembers("BÜNDNIS 90/DIE GRÜNEN");
	// selected = "BÜNDNIS 90/DIE GRÜNEN";
	// }
	// }
	// else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_CITY)
	// {
	// String city = MembersSubpageViewHelperFragment.cityNames[index];
	// membersCursor = membersDatabaseAdapter.fetchCityMembers(city);
	// selected = city;
	// }
	// else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_ELECTION)
	// {
	// String election =
	// MembersSubpageViewHelperFragment.electionNames[index].substring(0, 3);
	// membersCursor = membersDatabaseAdapter.fetchElectionMembers(election);
	// selected = MembersSubpageViewHelperFragment.electionNames[index];
	// }
	//
	// ArrayList<Object> values = new ArrayList<Object>();
	// values.add(membersCursor);
	// values.add(selected);
	//
	// return values;
	// }
	//
	//
	/**
	 * Creates cursor with members for the members sub pages.
	 * 
	 * Used by general list fragment.
	 */
	public static ArrayList createMembersSubpage(Activity activity, int subPageId, int index) {
		MembersDatabaseAdapter membersDatabaseAdapter = new MembersDatabaseAdapter(activity);
		membersDatabaseAdapter.open();

		Cursor membersCursor = membersDatabaseAdapter.fetchAllMembers();
		String selected = "";

		if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_FRACTION) {
			if (index == 0) {
				// membersCursor =
				// membersDatabaseAdapter.fetchFractionMembersTwo("CDU", "CSU");
				membersCursor = membersDatabaseAdapter.fetchFractionMembers("CDU/CSU");
				selected = "CDU/CSU";

			} else if (index == 1) {
				membersCursor = membersDatabaseAdapter.fetchFractionMembers("SPD");
				selected = "SPD";
			} else if (index == 2) {
				membersCursor = membersDatabaseAdapter.fetchFractionMembers("FDP");
				selected = "FDP";
			} else if (index == 3) {
				membersCursor = membersDatabaseAdapter.fetchFractionMembers("DIE LINKE.");
				selected = "DIE LINKE.";
			} else if (index == 4) {
				// membersCursor =
				// membersDatabaseAdapter.fetchFractionMembersTwo("BÜNDNIS 90",
				// "DIE GRÜNEN");
				membersCursor = membersDatabaseAdapter.fetchFractionMembers("BÜNDNIS 90/DIE GRÜNEN");
				selected = "BÜNDNIS 90/DIE GRÜNEN";
			}
		} else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_CITY) {
			String city = MembersSubpageViewHelperFragment.cityNames[index];
			membersCursor = membersDatabaseAdapter.fetchCityMembers(city);
			selected = city;
		} else if (subPageId == MembersSubpageMembersActivity.SUB_PAGE_ELECTION) {
			String election = MembersSubpageViewHelperFragment.electionNames[index].substring(0, 3);
			membersCursor = membersDatabaseAdapter.fetchElectionMembers(election);
			selected = MembersSubpageViewHelperFragment.electionNames[index];
		}

		ArrayList<Object> values = new ArrayList<Object>();
		values.add(membersCursor);
		membersCursor.moveToFirst();
		String memberRow = membersCursor.getString(membersCursor.getColumnIndex(MembersDatabaseAdapter.KEY_ROWID));
		values.add(selected);

		return values;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		cityNames = null;

		electionNames = null;

	}
}
