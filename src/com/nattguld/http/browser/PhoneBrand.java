package com.nattguld.http.browser;

import java.util.ArrayList;
import java.util.List;

import com.nattguld.util.maths.Maths;

/**
 * 
 * @author randqm
 *
 */

public enum PhoneBrand {
	
	SAMSUNG("Samsung", new Object[][] {
		{"9", "SAMSUNG SM-G973F Build/PQ2A"},
		{"9", "SAMSUNG SM-A505FN Build/PQ2A"},
		{"9", "SAMSUNG SM-A606F Build/PQ2A"},
		{"9", "SAMSUNG SM-A705F Build/PQ2A"},
		{"9", "SAMSUNG SM-A805F Build/PQ2A"},
		
		{"8.1.0", "SAMSUNG SM-J710MN Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-N960F Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-N9600 Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-G390F Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-J701M Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-J701MT Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-G610M Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-J530G Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-M205F Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-M305F Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-J260F Build/M1AJQ"},
		{"8.1.0", "SAMSUNG SM-J610FN Build/M1AJQ"},
		
		{"8.0.0", "SAMSUNG SM-G955U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G930A Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-J600GT Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G9650 Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A520F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G611MT Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A520W Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A600FN Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G955F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A530F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G950U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-J600G Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G935F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-N950F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-N950U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G960U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A520F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G930F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A320FL Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G965U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-N950U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G9650 Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A530F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-J330F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G950U Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G965F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-N950F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G611MT Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A720F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-G965U1 Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A260F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-J600F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-J810F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A750F Build/R16NW"},
		{"8.0.0", "SAMSUNG SM-A920F Build/R16NW"},
		
		{"7.1.2", "SAMSUNG SM-T555 Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-T550 Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J250M Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-T350 Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J510FN Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J510FN Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J330F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J530F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J730F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-N950F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-A530F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-A310F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J710F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G925F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J730F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-T585 Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G925I Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-A510M Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G610M Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-A510F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J530F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J327T1 Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G920F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G570M Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J530G Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J710MN Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G920F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G950F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J730G Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J730G Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-G935F Build/N2G48H"},
		{"7.1.2", "SAMSUNG SM-J701MT Build/N2G48H"},
		
		{"7.1.1", "SAMSUNG SM-T555 Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-T550 Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J250M Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-T350 Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J510FN Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J510FN Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J330F Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J530F Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-J730F Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-N950F Build/NMF26X"},
		{"7.1.1", "SAMSUNG SM-A530F Build/NMF26X"},
		
		{"7.0", "SAMSUNG SM-A310F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J710F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G925F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J730F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-T585 Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G925I Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A510M Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G610M Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A510F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J530F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J327T1 Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G920F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G570M Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J530G Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J710MN Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G920F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G950F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J730G Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J730G Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G935F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J701MT Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G920F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J701MT Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A520F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G925I Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G570M Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G920F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J710MN Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J710MN Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J730G Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A510F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A510F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A320FL Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J530G Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G955F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-J530F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-A520F Build/NRD90M"},
		{"7.0", "SAMSUNG SM-G930F Build/NRD90M"},
		{"6.0", "SAMSUNG SM-G935F Build/MMB29K"},
		{"6.0", "SAMSUNG SM-G900T Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G935F Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-G900T Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-J500M Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-J500FN Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G532M Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-G570M Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-G532MT Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-J120A Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-G532M Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-G532MT Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-G935F Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-J500M Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-A500FU Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-A310F Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-J510FN Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G532M Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-G900F Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-N910F Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-J500FN Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-J500M Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G532MT Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-G920I Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-J700M Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-J500M Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G900F Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G532MT Build/MMB29T"},
		{"6.0.1", "SAMSUNG SM-J710MN Build/MMB29K"},
		{"6.0.1", "SAMSUNG SM-G900F Build/MMB29M"},
		{"6.0.1", "SAMSUNG SM-G900F Build/MMB29M"},
		{"5.1.1", "SAMSUNG SM-T280 Build/LMY47V"},
		{"5.1.1", "SAMSUNG SM-G531H Build/LMY48B"},
		{"5.1.1", "SAMSUNG SM-G531H Build/LMY48B"},
		{"5.1.1", "SAMSUNG SM-G531F Build/LMY48B"},
		{"5.1.1", "SAMSUNG SM-G531BT Build/LMY48B"},
		{"5.1.1", "SAMSUNG SM-J120M Build/LMY47X"},
		{"5.1.1", "SAMSUNG SM-J200BT Build/LMY47X"},
		{"5.1.1", "SAMSUNG SM-G360T1 Build/LMY47X"},
		{"5.1.1", "SAMSUNG SM-J320M Build/LMY47V"},
		{"5.1.1", "SAMSUNG SM-G903F Build/LMY47X"},
		{"5.1.1", "SAMSUNG SM-J111M Build/LMY47V"},
		{"5.1.1", "SAMSUNG SM-J320F Build/LMY47V"},
		{"5.1.1", "SAMSUNG SM-G925F Build/LMY47X"},
		{"5.1.1", "SAMSUNG SM-G361F Build/LMY48B"},
		{"5.1.1", "SAMSUNG SM-J110M Build/LMY48B"},
		{"5.0.2", "SAMSUNG SM-G530M Build/LRX22G"},
		{"5.0.2", "SAMSUNG SM-G360BT Build/LRX22G"},
		{"5.0.2", "SAMSUNG SM-A500FU Build/LRX22G"},
		{"5.0.1", "SAMSUNG GT-I9515 Build/LRX22C"},
		{"5.0", "SAMSUNG SM-N9005 Build/LRX21V"},
		{"5.0", "SAMSUNG SM-G900F Build/LRX21T"},
		
		{"4.4.4", "SAMSUNG SM-G900F Build/LRX21T"}, //TODO
	}),
	APPLE("Apple", new Object[][] {
		{"iPhone", "iPhone"},
	}),
	HUAWEI("Huawei", new Object[][] {
		{"9", "HMA-AL00 Build/HUAWEIHMA-AL00"},
		{"9", "VOG-L04 Build/HUAWEIVOG-L04"},
		{"9", "ELE-L09 Build/HUAWEIELE-L09"},
		{"9", "MAR-LX1A Build/HUAWEIMAR-LX1A"},
		
		{"8.1", "CLT-L29 Build/HUAWEICLT-L29"},
		{"8.1", "DRA-LX2 Build/HUAWEIDRA-LX2"},
		{"8.1", "DUA-LX2 Build/HUAWEIDUA-LX2"},
		{"8.0", "ALP-L09 Build/HUAWEIALP-L09"},
		
		{"7.0", "PRA-LX3 Build/HUAWEIPRA-LX3"},
		{"7.0", "WAS-TL10 Build/HUAWEIWAS-TL10"},
		{"7.0", "VTR-L09 Build/HUAWEIVTR-L09"},
		{"", "VTR-L29 Build/HUAWEIVTR-L29"},
		
		{"6.0", "MYA-L22 Build/HUAWEIMYA-L22"},
		{"6.0", "EVA-L09 Build/HUAWEIEVA-L09"},
		{"5.1.1", "HUAWEI MT7-TL10 Build/HuaweiMT7-TL10"},
		{"5.1.1", "LUA-L21 Build/HUAWEILUA-L21"},
		{"5.0.1", "ALE-L21 Build/HuaweiALE-L21"},
		{"5.0.2", "PLK-AL10 Build/HONORPLK-AL10"},
		{"4.4.4", "HUAWEI H891L Build/HuaweiH891L"},
	}),
	XIAOMI("Xiaomi", new Object[][] {
		{"5.0", "HM 1SW Build/LTD768"},
		{"5.0.2", "Mi 4i Build/LRX22G"},
		{"4.4.4", "MI 3W Build/KTU84P"},
		{"4.4.4", "MI PAD Build/KTU84P"},
		{"4.4.4", "HM 1S Build/KTU84Q"},
		{"4.4.4", "HM NOTE 1LTE Build/KTU84P"},
	}),
	PIXEL("Pixel", new Object[][] {
		{"9", "Pixel Build/PQ1A.181205.002.A1"},
		{"8.1.0", "Pixel Build/OPP6.171019.012"},
		{"8.0.0", "Pixel Build/OPR3.170623.013"},
		{"7.1.2", "Pixel Build/NHG47O"},
		{"7.1.2", "Pixel Build/NHG47Q"},
		{"7.1.1", "Pixel Build/NOF26V"},
	}),
	LG("LG", new Object[][] {
		{"8.0", "LG-H850 Build/OPR1.170623.032"},
		{"8.0", "LG-H870 Build/OPR1.170623.032"},
		{"8.0", "LG-H87 Build/OPR1.170623.032"},
		{"7.1.1", "LG-M700 Build/NMF26X"},
		{"7.0", "LG-H850 Build/NRD90U"},
		{"7.0", "LG-H870 Build/NRD90U"},
		{"7.0", "LG-M150 Build/NRD90U"},
		{"7.0", "LGMS210 Build/NRD90U"},
		{"7.0", "LG-M250 Build/NRD90U"},
		{"7.0", "LG-H990DS Build/NRD90U"},
		{"6.0.1", "LG-H850 Build/MMB29M"},
		{"6.0.1", "LG-M150 Build/MXB48T"},
		{"6.0", "LG-H818 Build/MRA58K"},
		{"6.0", "LG-H815 Build/MRA58K"},
		{"6.0", "LG-D855 Build/MRA58K"},
		{"6.0", "LG-K350 Build/MRA58"},
		{"6.0", "LG-K430 Build/MRA58K"},
		{"5.1", "LG-H815 Build/LMY47D"},
		{"5.1", "LG-H818 Build/LMY47D"},
		{"5.1.1", "LG-D722 Build/LMY48Y"},
		{"5.0.2", "LG-D415 Build/LRX22G"},
		{"5.0.2", "LG-D620 Build/LRX22G"},
		{"5.0.2", "LG-D802 Build/LRX22G"},
		{"5.0.2", "LG-V410/V41020c Build/LRX22G"},
		{"5.0", "LG-D855 Build/LRX21R"},
	}),
	MOTOROLA("Motorola", new Object[][] {
		{"6.0.1", "XT1254"},
		{"6.0.1", "XT1254 Build/MCG2"},
		{"6.0", "XT1068 Build/MPB24.65-34-3"},
		{"5.1", "XT1025 Build/LPCS23.13-34.8-3"},
		{"5.1", "XT1022 Build/LPCS23.13-34.8-3"},
		{"5.1", "XT1528 Build/LPIS23.29-17.5-7"},
		{"5.1", "XT1528 Build/LPIS23.29-17.5-2"},
		{"5.1", "XT1033 Build/LPBS23.13-56-2"},
		{"5.1", "XT1254 Build/SU4T"},
		{"4.4.4", "XT1022 Build/KXC21.5-40"},
		{"4.4.4", "XT1080 Build/SU6-7"},
	}),
	HTC("HTC", new Object[][] {
		{"8.0.0", "HTC 10 Build/OPR1.170623.027"},
		{"8.0.0", "HTC 10)"},
		{"8.0.0", "HTC U11 plus"},
		{"7.1.1", "HTC U11 Build/NMF26X"},
		{"7.0", "HTC 10 Build/NRD90M"},
		{"7.0", "HTC6535LVW Build/NRD90M"},
		{"6.0.1", "HTC_0P6B Build/MMB29M"},
		{"6.0.1", "HTC 10 Build/MMB29M"},
		{"6.0", "HTC_0P6B Build/MRA58K"},
		{"6.0", "HTC6525LVW Build/MRA58K"},
		{"6.0", "HTC6535LVW Build/MRA58K"},
		{"6.0", "HTC_M8x"},
		{"5.0.2", "HTC6500LVW 4G Build/LRX22G"},
		{"5.0.2", "HTC_M8x Build/LRX22G; wv"},
		{"5.0.2", "HTC_PN071 Build/LRX22G"},
		{"5.1.1", "HTC6535LVW Build/LMY47O"},
		{"5.0.1", "HTC_0P6B Build/LRX22C"},
		{"5.0.1", "HTC6525LVW Build/LRX22C"},
		{"4.4.4", "HTC_0P6B Build/KTU84P"},
		{"4.4.4", "HTC6525LVW Build/KTU84P"},
	});
	
	
	/**
	 * The name.
	 */
	private final String name;
	
	/**
	 * The models.
	 */
	private final Object[][] models;
	
	
	/**
	 * Creates a new phone brand.
	 * 
	 * @param name The name.
	 * 
	 * @param models The models.
	 */
	private PhoneBrand(String name, Object[][] models) {
		this.name = name;
		this.models = models;
	}
	
	/**
	 * Retrieves a random model.
	 * 
	 * @param osVersion The OS version.
	 * 
	 * @return The model.
	 */
	public String getRandomModel(String osVersion) {
		List<String> filters = new ArrayList<>();
		
		for (int i = 0; i < models.length; i++) {
			String modelOsVersion = (String)models[i][0];
			String modelName = (String)models[i][1];
			
			if (!modelOsVersion.equals(osVersion)) {
				continue;
			}
			filters.add(modelName);
		}
		if (filters.isEmpty()) {
			System.err.println("[" + getName() + "]: No model found for os version: " + osVersion);
			return PhoneBrand.SAMSUNG.getRandomModel(osVersion);
		}
		return filters.get(Maths.random(filters.size()));
	}
	
	/**
	 * Retrieves the android API level.
	 * 
	 * @param osVersion The OS version.
	 * 
	 * @return The API level.
	 */
	public int getApiLevel(String osVersion) {
		if (osVersion.startsWith("10")) {
			return 29;
		} else if (osVersion.startsWith("9")) {
			return 28;
		} else if (osVersion.startsWith("8.1.0")) {
			return 27;
		} else if (osVersion.startsWith("8.0.0")) {
			return 26;
		} else if (osVersion.startsWith("7.1")) {
			return 25;
		} else if (osVersion.startsWith("7.0")) {
			return 24;
		} else if (osVersion.startsWith("6.0")) {
			return 23;
		} else if (osVersion.startsWith("5.1")) {
			return 22;
		} else if (osVersion.startsWith("5.0")) {
			return 21;
		} else if (osVersion.startsWith("4.4")) {
			return 19;
		} else if (osVersion.startsWith("4.3")) {
			return 18;
		} else if (osVersion.startsWith("4.2")) {
			return 17;
		} else if (osVersion.startsWith("4.1")) {
			return 16;
		} else if (osVersion.startsWith("4.0.3") || osVersion.startsWith("4.0.4")) {
			return 15;
		} else if (osVersion.startsWith("4.0.1") || osVersion.startsWith("4.0.2")) {
			return 14;
		} else if (osVersion.startsWith("3.2")) {
			return 13;
		} else if (osVersion.startsWith("3.1")) {
			return 12;
		} else if (osVersion.startsWith("3.0")) {
			return 11;
		} else if (osVersion.startsWith("2.3.3") || osVersion.startsWith("2.3.4")
				|| osVersion.startsWith("2.3.5") || osVersion.startsWith("2.3.6")
				|| osVersion.startsWith("2.3.7")) {
			return 10;
		} else if (osVersion.startsWith("2.3")) {
			return 9;
		} else if (osVersion.startsWith("2.2")) {
			return 8;
		} else if (osVersion.startsWith("2.1")) {
			return 7;
		} else if (osVersion.startsWith("2.0.1")) {
			return 6;
		} else if (osVersion.startsWith("2.0")) {
			return 5;
		} else if (osVersion.startsWith("1.6")) {
			return 4;
		} else if (osVersion.startsWith("1.5")) {
			return 3;
		} else if (osVersion.startsWith("1.1")) {
			return 2;
		} else if (osVersion.startsWith("1.0")) {
			return 1;
		}
		return 30;
	}
	
	/**
	 * Retrieves the name.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Retrieves the models.
	 * 
	 * @return The models.
	 */
	public Object[][] getModels() {
		return models;
	}
	
	@Override
	public String toString() {
		return getName();
	}

}
