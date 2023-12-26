XLUA API
==============


Filtering Params
------------

```Java

//Filters out a Property in replacement of a spoofed one
//You can Define the Spoof Value within the App UI
String filterBuildProperty(String property)

//Filters Params from Settings$Secure
//Currently only filters for "android_id" & "bluetooth_name"
//Returns True if the Fake Value was Set, From the Lua Script just return True else False
boolean filterSettingsSecure(String setting)

//Filters ".query" Commands from "ContentResolver" only Supports "gsf_id" For now
boolean queryFilterAfter(String filter)
boolean queryFilterAfter(String filter, Uri uri)

```

```LUA

	local propertyParam = param:getArgument(0)
	local filtered = param:filterBuildProperty(propertyParam)

	if filtered == false then
		log("Property Intercepted & Filtered")
		return true
	end

	log("Property is alight to Pass")
	return false

```

```LUA

	local filtered = param:filterSettingsSecure("android_id")
	if filtered == true then
		log("Secure Setting was Intercepted & Filtered")
		return true
	end

	log("Secure Setting is clean good to go")
	return false

```

```LUA

	local filtered = param:queryFilterAfter("gsf_id")

	if filtered == true then
		log("Query Intercepted & Filtered")
		return true
	end

	log("Query is alight to Pass")
	return false

```


Memory/CPU
------------


```Java
//Returns a Fake /proc/meminfo File as 'java.io.File'
File createFakeMeminfoFile(int totalGigabytes, int availableGigabytes)

//Returns a 'java.io.FileDescriptor' Pointing to the Fake /proc/meminfo File
FileDescriptor createFakeMeminfoFileDescriptor(int totalGigabytes, int availableGigabytes)

//Sets the Fields for Java 'MemoryInfo' Structure Holding Memory Information
void populateMemoryInfo(ActivityManager.MemoryInfo memoryInfo, int totalMemoryInGB, int availableMemoryInGB)

//Instead of Populating, Return a 'MemoryInfo' Structure with preset fake values passed from params
ActivityManager.MemoryInfo getFakeMemoryInfo(int totalMemoryInGB, int availableMemoryInGB)

//Returns a Fake /proc/cpuinfo File as 'java.io.File'
//Specific CPU Map can be set in the UI
//If nothing is set, it will Randomize through the 43 Maps
File createFakeCpuinfoFile()

//Returns a Fake /proc/cpuinfo File as 'java.io.FileDescriptor'
//Specific CPU Map can be set in the UI
//If nothing is set, it will Randomize through the 43 Maps
FileDescriptor createFakeCpuinfoFileDescriptor()

```

```LUA
	-- 90GB / 50GB RAM/Memory
	local fakeMemFL = param:createFakeMeminfoFile(99, 50)
	local fakeMemFD = param:createFakeMeminfoFileDescriptor(99, 50)

	-- Populating the MemoryInfo Struct passed through
	local memoryInfoInstance = param:getArgument(0)
	param:populateMemoryInfo(memoryInfoInstance, 99, 50)

	local fakeMemoryInfo = param:getFakeMemoryInfo(99, 50)

	local fakeCpuFL = param:createFakeCpuinfoFile()
	local fakeCpuFD = param:createFakeCpuinfoFileDescriptor()
```


Network
------------

```Java

//Convert Ip String to Bytes
//Needs 4 decimals like "127.0.0.1" keep it numeric
byte[] getIpAddressBytes(String ipAddress)

//Generates a Random Fake IpAddress Returns the Bytes
//A preset value can be defined Via LUAPRO UI as a setting 'net.host_address'
byte[] getFakeIpAddressBytes()

//Generates a Random Fake IpAddress Returns the value but as a int
//A preset value can be defined Via LUAPRO UI as a setting 'net.host_address'
int getFakeIpAddressInt()

//Generates a Random Fake MAC Address Returns the bytes of it
//A preset value can be defined Via LUAPRO UI as a setting 'net.mac'
byte[] getFakeMacAddressBytes() 

```

```LUA

	local fakeIpBytes = param:getIpAddressBytes("127.0.0.1")
	local fakeIpBytes2 = param:getFakeIpAddressBytes()

	local fakeIpInt = param:getFakeIpAddressInt()

	local fakeMacBytes = param:getFakeMacAddressBytes()

```

Utils
------------

```Java

//Gets the Context / 'android.content.Context'
Context getApplicationContext()

//Get the Name of the Package Hook is in
String getPackageName()

//Get the UID Group of the Package / Hook
int getUid()

//Get the Xposed Scope Param
Object getScope()

//Gets the 'this' Instance of the Hooked Function as if you are the Function
Object getThis()

//Get the Runtime SDK of the App
int getSDKCode()

//Prints the Contents in a File onto the CatLog Line by Line
void printFileContents(String filePath)


```

REFLECTION
------------

```Java

//Returns Type Class 'java.lang.Byte'
Class<Byte> getByteType()

//Returns Type Class 'java.lang.Integer'
Class<Integer> getIntType()

//Returns Type Class 'java.lang.Character'
Class<Character> getCharType()

//Returns a Java Byte Array
byte[] createByteArray(int size)

//Returns a Java Integer Array
int[] createIntArray(int size)

//Returns a Java Character Array
Character[] createCharArray(int size)

//Pass in a Class Path with Name , Pass a Method to check for if it exists in that Class
boolean javaMethodExists(String className, String methodName)

//Get the Type Class From a Class Path and Name String 'classForname'
Class<?> getClassType(String className)

//Create a Java Array Passing in Class Path and Name of the Element Type
Object createReflectArray(String className, int size)

//Create a Java Array Passing in Type Class of the Element Type
Object createReflectArray(Class<?> classType, int size)

```

```LUA

	local bytePath = "java.lang.Byte"
	local byteClass = param:getClassType(bytePath)
	local byteArray = param:createReflectArray(byteClass, 6)
	-- Create a Byte[] (array) size of 6
	-- Or

	local byteClass = param:getByteType()
	local byteArray = param:createReflectArray(byteClass, 6)
	-- Or

	local byteArray = param:createByteArray(6)

	local strClass = "java.lang.String"
	local strArray = param:createReflectArray(strClass, 6)
	-- Create a String[] (String) size of 6

```



LONG HELPERS
------------

These functions are to handle the issue of the LUA Version not supporting BIG INT / LONG


```Java

//Uses Reflection to get a Field of Type Long and Convert it into a String to the LUA Layer
String getFieldLong(Object instance, String fieldName)

//Uses Reflection to Set a Field Value Type of Long from a String of a Long Value
void setFieldLong(Object instance, String fieldName, String longValue) 

//Takes a String of a Long Value to the Java Layer Parsing String to Long
//Takes Parcel Instance to Set the long Value in
void parcelWriteLong(Parcel parcel, String longValue)

//Reads Long Value from a Parcel then Converts it to a String Before lifting it to the LUA Layer
String parcelReadLong(Parcel parcel)

//Takes a String of a Long Value to the Java Layer Parsing String to Long
//Takes Bundle Instance & key to Set the long Value in
void bundlePutLong(Bundle bundle, String key, String longValue)

//Reads Long Value from a Bundle then Converts it to a String Before lifting it to the LUA Layer
String bundleGetLong(Bundle bundle, String key)

//Set the Result of the Hooked Function as a Long , Parsing the Given String to a Long in the Java Layer
void setResultToLong(String long_value)

//Gets the Result of the Hooked Function as a Long then Converting it to a String before lifting it to the LUA Layer
String getResultLong()

```

```LUA

	local memInfo = luajava.newInstance("android.app.ActivityManager$MemoryInfo")
	-- set the Field 'availMem' to 999999999
	param:setFieldLong(memInfo, "availMem", "999999999")

	-- get the Field 'availMem' as a String long "999999999"
	local availMemory = param:getFieldLong(memInfo, "availMem")


	local parcel = luajava.newInstance("android.os.Parcel")
	-- write to parcel a Long Value represented as a String in LUA
	param:parcelWriteLong(parcel, "7777777777777")

	-- read from parcel a Long Value represented as a String in LUA
	local longBack = param:parcelReadLong(parcel)


	local bundle = luajava.newInstance("anroid.os.Bundle")
	-- put to bundle a Long Value represented as a String in LUA
	param:bundlePutLong(bundle, "SomeKey", "555555555555")

	-- get from bundle a Long Value represented as a String in LUA
	local longBack = param:bundleGetLong(bundle, "SomeKey")

```


Arguments / Return / Result
------------

```Java

//Getting / Setting Arguments of Hooked Function

Object getArgument(int index)

void setArgument(int index, Object value)

void setArgumentString(int index, Object value)

void setArgumentString(int index, String value)

//Getting / Setting Result of Hooked Function

Object getResult()

void setResult(Object result)

void setResultString(Object result)

void setResultString(String result)

void setResultByteArray(Byte[] result)

Throwable getException()

```


Settings / Globals
------------

```Java

//Get a XPL-EX Setting
String getSetting(String name)

//Get a XPL-EX Setting , if setting does not exist return Default Value
String getSetting(String name, String defaultValue)

//Get a XPL-EX Setting as int, if setting does not exist return Default Value
int getSettingInt(String name, int defaultValue)

//Get a Global Value for that Instance of Target Hook App
Object getValue(String name, Object scope)

//Put a Global Value for that Instance of Target Hook App
void putValue(String name, Object value, Object scope)
```



Sorry English not good will get better over time <3 much love