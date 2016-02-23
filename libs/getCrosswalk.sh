ver="16.45.421.19"
wget https://download.01.org/crosswalk/releases/crosswalk/android/maven2/org/xwalk/xwalk_core_library/${ver}/xwalk_core_library-${ver}.aar
unzip -j xwalk_core_library-${ver}.aar classes.jar
zip -d classes.jar javax\*
zip -r xwalk_core_library-${ver}.aar classes.jar
rm -f classes.jar
