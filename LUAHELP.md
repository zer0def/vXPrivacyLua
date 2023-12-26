Examples of LUA / LUAJ with XPL-EX
------------

```LUA

-- To Start the Hook you either need to Specify a "before" or "after" Function with params "(hook, param)"
-- If what you are Hooking is a Field then the Function will have to be a "after" Hook do not use a "before" Hook
-- To Specify Strings you can use double quotes or Single quotes
-- Lets say for example we are Hooking a "toString" Function

-- First define the function , either as a "before" or "after" with its params 

-- We use a "before" Hook if we want to Modify the Target Function Behaviour BEFORE it Executes the Original Function Code
function before(hook, param)
	log("Hello World, the before hook for toString has been Invoked")
	-- add code here
end


-- We use an "after" Hook when we want to Modify the Target Function Behaviour AFTER it Executes
-- Use this if you want to get the Result of the Return value of the Hook Function if it Has a return Value
function after(hook, param) 
	log("Hello World, the after hook for toString has been Invoked")
	--add code here
end

```
BASICS
------------

```LUA


-- To Log we simply use the "log" function
-- Do note LUA if a Fast Scripting language , bit faster than Java ;)
-- The version of LUA Is 5.2 (Version right behind the one that supports BIG INT / LONG)
-- With that we get to use the LUA Basic Functions such as "math", "tostring" , "tonumber"
-- If we want to interact with the JAVA Environment we simply use "luajava", following with either "bindClass" or "newInstance"

function before(hook, param)
	log("Before Hook is Invoked")

	-- lets create a Java "File" object
	-- First lets create the LUA Object that points to that Class
	local fileClass = luajava.bindClass("java.io.File")
	-- We specify the Full length to the class including class name
	-- From here we can interact with the class obj using ":"
	-- Lets create a TEMP File

	log("Creating Temp File!")

	local tempFile = fileClass:createTempFile("temp", null)
	-- 'null' can be used but in the context of Java
	-- 'nil' is used in the Context of LUA

	local path = tempFile:getPath();
	log("Temp File was Created, Path is: " .. path)

	-- To Concatinate strings in lua you use ".."
	-- As you see to Interact with the Return value of 'java.io.File' object 'tempFile' we use ":" , "tempFile:getPath()"
	-- If we want to access a JAVA Field we dont need to use ":" instead we can go back to using "."
	-- If Java "java.io.File" Class has a Public Field called 'filePath' we can then do "local path = tempFile.filePath"
	-- In LUA you dont end code with a semi colon ";"
end

```

PARAM Usage
------------

```LUA

-- Now we got the Basics out the way how LUA works / LUAJ / Java with LUA on a Basic Level
-- Lets get into the parameters/ arguments that each "before" and "after" Function is defined with (hook, param)
-- These two Params will be our Bridge between LUA and our JAVA API to give eas of access to functions at your need
-- "hook" Param will store just information on the actual hook definition it self
-- "param" Param will store all the cool utils to use, and as well as the Return Value of the Hooked Function as well as Argument Values inputed into the Hook Function
-- Lets set the return Value of a "toString" Function Hook, if its defined properly within the UI the LUA Code can look something like the following

function after(hook, param) 
	log("The toString Before Hook has been Invoked!")
	-- lets get the result of the 'toString' Function return

	local res = param:getResult()
	-- using the 'param' arg we have a function in the API that we can use called 'getResult'

	if res == nil then 
		return false
	end

	log("Original Return result=" .. res)

	-- We did a few things, since the object has been lifted to LUA layer to check if its NULL we use NIL
	-- Its best to always check what you are using if its NIL, good code practicies avoid crashing when you try to access a 'NULL'/'NIL' Object

	-- Logging is not needed but is a good practice to understand what is going on if things do not work
	-- Lets now actually Modify the return value
	param:setResult("Fake to string return, Hahaha Hello World, Spoofed!")

	-- using 'param' API we get to set the Result of the Hook function using 'setResult'
	-- Make sure the Value your setting it to is the same return value of the Hooked Function
	-- Once we do that we now have to Notify the Actual Hook Instance itself that the return value and or function was modified
	-- To do so we return 'true' else if no modifications were made to the function either 'result' or changing the hooked function 'argument' values then return false
	-- Since we modifed the Return Value/Result we return from this LUA Function with 'true'
	-- With '== nil' check above we return 'false' since result is NULL we will not modify the function there for return 'false'
	return true
end

```

Lifting objects from the JAVA Layer to LUA can sometimes get 'broken' or mis translated, either from JAVA to LUA or LUA back to JAVA
To Handle this issue Ive added Functions in the API that can set the result of the Hooked function to a few specific types (ones Ive had issues with translation)

 ```Java
	void setResult(java.lang.Object)
	void setResultString(java.lang.String)
	void setResultByteArray([B or BYTE[])
```
	

```LUA

-- Only needed when your hook gives a expected result/return value error , aka Type Mis Match
-- It will say something like 'got UserData instead of X Type'
-- Lets now Modify the Arguments going into the Hooked Function
-- Example we will use is 'java.io.File(java.lang.String)' Function "File f = new File("/sdcard/filePath/fileName");"
-- We will specifically filter for the "/proc/cupinfo" File

-- Define the LUA Function
function before(hook, param) 
	log("File is being Opened")
	-- to get the paramter passed into the Hook Function we use the API Function 'getArgument(int index)'
	local filePath = param:getArgument(0) -- 0 Since Index of the String File Path Param is (1) aka (0) starting from (0) 
	-- If you dont know why (0) then Google Array Indexes or something :P
	-- Do Note LUA Arrays do start on index (0), but since its out API Function that lays in the JAVA layer it will start at index (0)
	-- Check if its NULL
	if filePath == nil then
		log("Hook Constructor File Path Param is NULL! Skipping....")
		return false
	end

	log("File that is being opened=" .. filePath) -- filePath is a 'java.lang.String' , first Arg in the Constructor of 'java.io.File'

	-- to do a string check you can use '==' or I like to use (works most times) is LUA 'string.match'

	if string.match(filePath, "cpuinfo$") then
		-- In the string check we add at the end a '$' to indicate that the string 'cpuinfo' is at the end of the string your comparing with
		-- Lets now create a Empty file
		local fileClass = luajava.bindClass("java.io.File")
		local tempFile = fileClass:createTempFile("temp", null) -- Since we are passing it down to the JAVA layer we will use 'null' instead of 'nil'

		if tempFile ~= nil then
			log("Replacing File open from: " .. filePath .. " to => " .. tempFile:getPath())
			-- "~=" is not equals or "!="
			-- Instead of replacing the return value this time we will replace the argument of the "File" Function to point to our fake created file
			-- We use the API Function 'setArgument(int index, Object value)'
			-- Index being the Index of the Argument you want to replace

			param:setArgument(0, tempFile:getPath()) -- Out Empty Created File in the temp dir of the App being Hooked
			-- Since we modified the the Function we will return True
			return true
		end
	end

	return false -- Make sure to always return, false if no modifications, true if function was modfied via args or result/return value
end

```

Just like 'setResult' we have different helper functions for 'setArgument' incase the type is mismatched / didnt translate between the two languages

 ```Java
	void setArgument(java.lang.Object)
	void setArgumentString(java.lang.String)
```

```LUA

-- When returning from the Script we can add more return values after the 'true' or 'false'
-- The Extra values getting returned will be printed to the log cat, make sure your returning string values so it can be printed to logcat
-- Example we modify the Arguments again for the "File" Constructor to point to our fake file

-- Remeber returning 'true' will prevent the Target Hook Function from finishing execution assuming its a 'before' Hook
		-- returning 'false' will allow the Target Hook Function to Finish execution assuming its a 'before' Hook

function before(hook, param) 
	log("File Constructor Hook Invoked")
	local filePath = param:getArgument(0)
	if filePath == nil then
		log("File path arg is NULL skipping...")
		return false
	end

	if string.match(filePath, "/sdcard/WhateverFile") then 
		log("Is Target File: " .. filePath .. " Replacing")
		
		local fileClass = luajava.bindClass("java.io.File")
		local fake = fileClass:createTempFile("temp", null)

		log("Fake File path: " .. fake)
		--param:setResult(fake) we can do this replacing the orignal File Object this the Fake File Object
		param:setArgument(0, fake:getPath())

		return true, filePath, fake:getPath()
		-- Doing this first will tell the Hook, yes the Target Hook Function was Modified
		-- Then the following Return Values will be printed out to the log so you can see the changes
		-- This also means for alot of scripts no need to log assuming its simple logic, nothing too complex
	end

	return false
end

```

LUAJ newInstance
------------


```LUA

-- The LUAJ Function 'newInstance' is simple, first arg will be the full path to the class including name
-- Then the Following Arguments will be passed down to the object as args to use to create the new Instance
-- Lets create a new instance of a File object , Java defination something like "File(java.lang.String filePath)"
 
 function before(hook, param) 
 	log("I am in some Hooked Function ahahahaha")
 	local fileClass = luajava.bindClass("java.io.File")
 	-- can do 
 	local fakeFile1 = luajava.newInstance(fileClass, "/sdcard/SomeCoolFile")
 	-- or
 	local fakeFile2 = luajava.newInstance("java.io.File", "/sdcard/SomeCoolFile")
 	-- Dont mistake the 'newInstance' function from Java Reflect Array
 	return false
 end

 -- One thing this LUA version does not support is BIG INTs or LONG Values
 -- Once a LONG Value is lifted from the JAVA layer to the LUA layer it will be translated into a DECIMAL
 -- It does not matter how you Handle it, once it touches the LUA Layer it will be converted into a different type
 -- If you try to convert it back to a LONG it will Fail, simply put it LUA does not support LONG
 -- To work with this, we Convert the LONG value into a String BUT before it touches the LUA layer becuase once again it will be converted
 -- Ive made a few Helper Functions to "work" with this issue
 -- Lets work with one structure that has LONG Fields "ActivityManager.MemoryInfo" , Specific Function "ActivityManager.getMemoryInfo(MemoryInfo)"

 -- [MemoryInfo]
 -- long advertisedMem;
 -- long availMem;
 -- long lowMemory;
 -- long threshold;
 -- long totalMem;

 function after(hook, param)
 	local memInfo = param:getArgument(0)
 	if memInfo == nil then
 		return false
 	end

 	local longClass = luajava.bindClass("java.lang.Long")
 	local strgClass = luajava.bindClass("java.lang.String")

 	-- this wont work
 	local bad1 = memInfo.availMem
 	local bad2 = longClass:parseLong(bad1) -- This wont help, once the LONG gets lifted from the JAVA layer to LUA its back to Decimal
 	local bad3 = luajava.newInstance(longClass, 9999999999)
 	local bad4 = strgClass:valueOf(memInfo.availMem) 
 	-- Once again it will have to lift the Field result from the Struct to the LUA layer
 	-- Sure your passing it into a JAVA Function but again has to touch the LUA layer , then messes up the Type

 	-- What you CAN do, using our API in this case we use: getFieldLong(Object objectInstance, String fieldName) returns a String representing a LONG
 	-- public String getFieldLong(Object instance, String fieldName)

 	local good = param:getFieldLong(memInfo, "availMem")
 	-- Now to set the LONG Field
 	-- public void setFieldLong(Object instance, String fieldName, String longValue) 

 	param:setFieldLong(memInfo, "availMem", "99999999999")

 	-- We substitue by using a String, then in the JAVA layer convert the String to a LONG
 	-- We use a Mix of things with Reflection
 	-- Within the Future we should Have more APIs to work around the LONG issue

 end

```

HOOK USAGE API
------------


```LUA

-- Just like the 'param' arg that gets passed within the hook you also get 'hook' arg that is passed
-- This just Provides information about the Hook

function before(hook, param)
	-- Name of Hook
	local name = hook:getName()
	log("Hook Name: " .. name)
	
	-- Group Name of Hook
	local groupName = hook:getGroup()
	log("Group Name: " .. groupName)

	-- Author Name for the Hook
	local authorName = hook:getAuthor()
	log("Author Name: " .. authorName)

	return false
end


```