package com.hackhalo2.util.sync;

public enum OS {
	UNIX("Unix"),
	LINUX("Linux"),
	SOLARIS("Solaris"),
	WINDOWS_LEGACY("Windows"),
	XP("Windows XP"),
	VISTA("Windows Vista"),
	SEVEN("Windows 7"),
	EIGHT("Windows 8"),
	WINRT("Windows RT"),
	OSX("Mac OSX"),
	MAC("Mac"),
	ANDROID("Android"),
	IOS("iOS"),
	UNKNOWN("Unknown OS");
	
	private final String id;
	
	private OS(String id) {
		this.id = id.toLowerCase();
	}
	
	public boolean isMobileOS() {
		return (this == ANDROID || this == IOS || this == WINRT);
	}
	
	public boolean isUnixOS() {
		return (this == UNIX || this == LINUX || this == SOLARIS);
	}
	
	public boolean isWindowsOS() {
		return (this == XP || this == VISTA || this == SEVEN || this == EIGHT);
	}
	
	public boolean isLegacyWindowsOS() {
		return this == WINDOWS_LEGACY;
	}
	
	public boolean isMacOS() {
		return (this == MAC || this == OSX);
	}
	
	public boolean isOSKnown() {
		return !(this == UNKNOWN);
	}
	
	public static OS getOperatingSystem() {
		OS bestMatch = UNKNOWN;
		final String osNameFromOS = System.getProperty("os.name").toLowerCase();
		for(OS system : values()) {
			if(osNameFromOS.contains(system.id)) {
				if(system.id.length() > bestMatch.id.length()) {
					bestMatch = system;
				}
			}
		}
		return bestMatch;
	}

}