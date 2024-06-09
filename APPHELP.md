
# How can I use this Damn F*cking App
<br><br>
## Issues with Simplicity
Due to the Complex nature of the Application (it allowing for Dynamic Definition of Hooks, ability to have 90% Control over Hooks and its Behaviour) it makes it difficult to "make the application more simple". Also due to its size of elements it contributes to the difficult in "making it simple".<br>
Applications such as "Android Spoofer", "PMP", and "Android Faker" can do these things since there goal in mind is to make a application with Static Hook Definitions. Now sure XPLEX provides out the box ALOT of Hooks most you will ever need, but you are the User can Create your Own Hooks, define the Hook behaviour so simply trying to group all of this is a challenge. When XPLEX is Compiled the Hook Scripts and Hooks are never set in stone complete. They can be modified by, updated by you, removed by you, or you can add more hooks, trying to "group" this chaos, or have a "magic" button one click all solution would lock more things in leaving the ned user with less options to customize to their hearts content.<br>
Simply put it, your comparing a Kitchen Knife to a Sword (Excalibur), if you want a Kitchen Knife, use the "Competitors/Alternatives". On top of that wonderful thing about XPLEX is it used Native Android UI, Supports Android 6.0+, Is 100% Free, Hooks can be Customized, Supports (most) APIs that can retrieve Device Information, AD and Anlytic Free, Fully works Offline (No Server it ever connects too), and supports virtual Environment such as "ThaiChi" and "VirtualXposed".<br>For a final Note for those who as me to make modifications to the Pro Companion app (Free Download can be found in the Telegram) I cannot as I do not have access or source code to that application. Future of XPLEX will merge all the Pro Features into one app still free.<br><br>
So instead of Dumbing it down, we will create a Guide that will help you navigate this maze!<br><br>

## Context (Global vs Application)
So each Hook or (most) Hooks can have an assigned setting for it value usage. Example the "IMEI" Hook(s) that Spoofs the Device "IMEI" would obvious grab the Fake "IMEI" value from the XLUA Settings Database. If the user Modified the Settings for those Hook(s) under the Context of the Application then it will direct its values from those assignments. Now if you didnt set the settings under the Context of the Application but under the "Global" (all) Context, then the Target Apps will grab its needed setting values for it assigned hooks from (Global) IF the values are not assigned to settings under the Context of the Application. So think of it as "Defaults" for when Values are not Assigned. This is the best option to replicate Default values as newest versions of XPLEX does not support Default Values (for now) when the Values needed are not assigned. On the contrary you can still use 'default' values if its available in the specific app settings as a check but this would be less affective especially when the setting / hook doesn't have a 'default' value.<br>
One Controls the Settings Context for ALL new Apps and All Apps, if the Settings Context for the App is missing the setting value then it will Grab the Global Value. The other controls the settings context for the specific app context (only for that app). So if the Specific App Context is missing a specific setting it needs for a hook, it will just use Global Settings Context if it exists in Global.<br>
- **Global Context:** Controls all apps. (If the specific app does not have assigned settings for that specific hook)
- **App Context:** Controls a specific app.<br>
You can typically access Global settings or any Global UI by selecting the Buttons on the side slide menu of the XPLEX Main UI, you can access App Specific Context Menus within the Buttons under the Applications within XPLEX Main UI.<br>
<details>
  <summary>Global Context</summary>

![Screenshot_20240610-223252_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/1e4b7a76-f343-463a-9a43-ab02f0dc522d)
</details>
<details>
  <summary>Application Context</summary>

![Screenshot_20240610-223234_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/ca9ab95d-c881-4b72-8657-22bcbed33e73)
</details>

<br><br>
## Main UI
<br>
<details>
  <summary>Application View</summary>

![Screenshot_20240610-225035_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/b667b1f0-a6b3-43e9-80d7-f3db008e2b6a)
</details>

<br>

- **1:**  Pro Companion App Settings (Links Access to the Original Pro Application)
- **2:** Check to Enable all the Hooks (If its your first time enabling the hook or using the app, select each Group you want first so you can read the warnings)
- **3:** Enable the Application to be Killed when you are modifying the Hook Assignments
- **4:** ***Hook Control UI***, under the Context of the Target Application
- **5:** ***Config Control UI***, under the Context of the Target Application
- **6:** ***Settings Control UI***, under the Context of the Target Application
- **7:** ***Properties Control UI***, under the Context of the Target Application
- **8:** Hook Groups, Hook Grouped in sub categories made available to select<br><br>
Selecting the Drawer Menu on the Side will open those UI's Under the Context of "Global"<br><br>

## Settings Embed
You will notice as the more you use this application, a lot of things are just advance setting wrappers. Most of the system is controlled by settings there for you will see things such as Properties UI, Settings UI, Config UI what not, it wil integrate some sort of setting control view or view(s). To understand the layout first here is a View of what it would look like:<br>
<details>
  <summary>Setting Control View</summary>

![Screenshot_20240610-231738_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/8133c305-9669-4de1-8281-660c6f06f22d)
</details>


- **1:**  Input for the Custom Value
- **2:**  ***Randomizer***, this will be auto selected no need to change it, if it has no randomizer the text will be "N/A (No Randomizer)" and if the Randomizer is a Static Element Randomizer it will just say "N/A" meaning nothing is selected but select the drop down to see the options.
- **3:** ***Delete***,  Delete the setting and or the Map or both. The map is the Default value and or Description (Gives personality to the setting), note setting names should never have actual spaces but XPLEX system converts "." (Periods) into Spaces within the UI for better user Viewing.
- **4:** ***Save***, Save the Modified Value into the XPLEX Settings Database override the last saved value
- **5:** ***Reset***, Reset the (Unsaved) Modified Value back to the last Saved Value
- **6:** ***Randomize***, Use the current Selected Randomizer to generate a Random Value into the Input. This will be Un-Saved there for can be Retested, you can save it if you are satisfied.<br>

***Easter Egg***, Knowing the Setting you are working work does not have "N/A (No Randomizer)" for its Default Randomizer and also it dosnt just say "N/A" as one means no Randomizer other Means no option selected since it has a hardcoded option selection. If those two things check out (basically put it easy the randomizer dosnt have "N/A" in it) then you can set the value of the Randomizer to "***%random%***" (No Qutoes), doing that will make it so each time you open up your target app, and needs that setting value it will Randomize that value for you. Once you close that app re open it, it will have a new Randomized value, Randomizes each time you closed and opened the app. Once again this will only work if the setting has a Randomizer by Default, and also that the Randomizer isnt a Hardcoded Option based one (again to make it easy if the randomizer name dosnt contain "N/A" then you can use this ***Easter Egg***).<br>

<details>
  <summary>Exmaple of Easter Egg</summary>
  
![Screenshot_20240612-072718_SystemUI](https://github.com/0bbedCode/XPL-EX/assets/114315756/9303e689-9f6d-48a8-b8eb-36254b115738)
</details>
<br>

<details>
  <summary>Exmaple of Good Randomizer (Donst contain N/A)</summary>
  
![Screenshot_20240612-072729_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/769ba56e-1195-46f6-9298-78b2574ca3ae)
</details>

<details>
  <summary>Exmaple a Option Based Randomizer</summary>
  
![Screenshot_20240612-072747_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/23bf2a58-c7ab-43e0-af65-859901a76b20)
</details>

<details>
  <summary>Exmaple of NO Randomizer/ Dosnt have one</summary>
  
![Screenshot_20240612-072803_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/a8fb50b6-b512-4da1-8847-8cc62b611a70)
</details>

<br><br>

## Settings UI
Skipping over the common Embed, we will focus on the Floating Action Button Menu options and the Floating App Island with the Drop Down options. For starters here is a ***Easter Egg***, by checking ONE setting in the group, THEN holding down on that ONE check box that you checked, it will then check all the other check boxes in that group. If you uncheck ONE check box in that group then hold down on that ONE unchecked box in that group, it will un check all the check boxes in that group. This will be useful for mass checking / selection.<br>
<details>
  <summary>Settings Options View</summary>

![Screenshot_20240611-181759_SystemUI3](https://github.com/0bbedCode/XPL-EX/assets/114315756/632f6daa-83c5-486b-80d4-d27916036c16)
</details>
<br>

- **1:** ***Delete***, Will Delete the Selected Settings
- **2:** ***Save***, Will Save the Selected Modified (but not saved) settings
- **3:** ***Add***, Will prompt a Menu to Add your own Setting
- **4:** ***Randomize***, Will Randomize all Selected settings if they have a Randomizer / and or one Selected<br>
<details>
  <summary>Application Floating Island View</summary>

![Screenshot_20240611-182840_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/3c57ee0a-805f-4111-aedb-eb365a1a441d)
</details>
<br>

- **1:** ***Properties UI***, Jump to Properties UI Under the Context of the selected Context in Settings UI
- **2:** ***Terminate***, Terminate the Android process (Kill it until you its re-opened)
- **3:** ***Configs UI***, Jump to Configs UI Under the Context of the selected Context in Settings UI
- **4:** ***Reset Settings***, Delete all Setting Values from the XLUA Database under the selected Context in Settings UI
- **5:** ***Save Checked***, Save the currently selected settings globally so next time you open the Settings UI the settings you saved to be selected will be auto selected so you do not have to keep re selecting.
- **6:** ***Clear Data***, Clear selected Context Target App, App Data
- **7:** ***Export to Config***, Export the settings to a Config so you can apply it later and or share to other users (this will be the setting values)
- **8:** ***Use Default Values for Settings***, If the Hook in question for it setting that it requires for a value has a default option available if user has not set that value, then use it. Currently no method except for looking through source / scripts if the Hooks have a Default value fallback as not all will have.<br><br>

## Properties UI
Before starting this to enable the power of Properties Interception select "***Intercept Properties***" and "***Intercept Shell***" Hook Groups, then in the ***Settings UI*** find the setting under the name "***Intercept Shell Getprop***" and set its value to "true" using the Drop down Randomizer option. After doing this you are ready to go to start intercepting properties, but for applications that use native methods to get  their properties XPLEX currently as if of (6/12/24) will not be of use for these situations. If you know you configured your properties correctly and enabled the hooks and LSPosed XPLEX is selected for that target app but it still gets the original Value of that property, then maybe the target App is using native methods.<br><br>

### Properties UI Overview
<details>
  <summary>Floating Action Buttons View</summary>

![Screenshot_20240611-191329_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/828c17cb-b9b8-4c02-af00-2b0899d06934)
</details>
<br>

- **1:** ***Add***, Add a new Property, typically adding it from this button means it has no group but if you do add it to a group that already exists then it will just add it to that group instead of creating a new group.

<details>
  <summary>Property Control View</summary>

![Screenshot_20240611-191352_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/c317f57c-20bf-4b04-ad04-36ed826e19ce)
</details>
<br>

- **1:** ***Add***, Add a new Property, this would bind it to the group you have clicked under so you do not need to fill in the group name just fill in the property name
- **2:** ***Hide***, Hide the Property if it exists
- **3:** ***Skip***, Ignore the property if it exists, not intercept and replace its value just skip / ignore it.
- **4:** ***Force***, Mainly used if the property does not exist, if it does not exist force it into view as if it does exist
- **5:** ***Delete***, Delete the Property from that Group<br>

### Properties System Overview

Now understanding this UI is not too complex as some may put it, for the reason we represent properties with XPLEX settings has to do with the fact that there can be many properties that represent the same value. We are of course talking about "build.prop" properties and or the properties you get from "getprop", "SystemProperties.get", "System.getProperty" functions. If you still have no idea what those are or what this is, its best for you to do research and back away from this thank you!<br>
So if we look here for Real world Examples lets list off ***Properties*** that represent the value "human":<br><br>

- **Jake**
- **Jill**
- **Michael**
- **Amanda**
- **Emily**<br><br>

What do they all have in Common ? They are all Humans (well we assume so I do guess it can be pets or aliens) but in this context they are humans. Now lets look at more Direct Examples of Properties that represent the same value, we will be looking at properties that represent that phones Manufacturer value, mine will be "Samsung".<br>

- **ro.vendor_dlkm.build.manufacturer**
- **ro.product.vendor.manufacturer**
- **ro.product.system_ext.manufacturer**
- **ro.product.system.manufacturer**
- **ro.product.odm.manufacturer**
- **ro.product.manufacturer**
- **ro.vendor.product.manufacturer**<br><br>

Those listed Properties what they have in common is they all represent the devices manufacturer, there for when you execute command like "getprop" on any of those above listed properties it will return the value (in my context) "Samsung". So would your solution to be map each one to a value but individually ? Well sure but that solution is piss poor, my solution is to map each one to a Single setting essentially grouping them together to a setting that best represents there value. In the case of XPLEX the setting that represents phone manufacturer is ***Device Manufacturer*** or actual name being ***device.manufacturer***, there for once that setting is changed it will affect all (7) of those above listed properties. This avoid the need to manually set a value that is common for each (7) one by one and also links the over all XPLEX system together making Hooked Functions and things such as Commands and Properties all synced. So all you have to do in theory is to modify the setting that represents that or those properties and boom you are good to go. Now if you need to create a group of properties (even if its just one property it will still be in a group just one on its own) then following the next instructions.<br><br>

## Configs UI
Now this UI is very easy to understand, but how the Configs work is very simple so listen up to the rules. First configs are just JSON Files made up of a bunch of XPLEX settings, it essentially is kind of like a XPLEX Settings backup but not as it is used for configuration to share to others or use again.<br>
Now the one rule I say for those creating configs and sharing is to separate them, as in have one Config focused on Carrier details (changing device carrier details) and another for just Phone details not related to carrier. So have Configs for ***Samsung Note 9***, and ***Pixel 8*** but then have seperate configs for ***TMobile***, ***Verizon*** and ***Sprint*** Carriers so they are not all in one config. Now combining  the two is not the end of the world but it starts to get dumb when you combine Unique Values with regular device values. Instance you made a Config for the ***OnePlus 7 Pro*** BUT you also in that config included Unique setting values such as "IMEI", "MAC Address" etc. No purpose in that as your combining something that is UNIQUE and will have to keep being changes at times, with something that isn't UNIQUE but just detailing the device and will not be changed or randomized. NO Purpose in creating configs that set UNIQUE values, Zero unless its generic UNIQUE values such as "Zero", "Private", "Unknown" whatever. I provide 3 examples of configs, note the Unique Device IDs is dumb but included for reference.<br><br>

<details>
  <summary>Config Control View</summary>

![Screenshot_20240611-193009_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/188cc5f8-c353-469e-ac25-263cbb0fb0cd)
</details>
<br>

- **1:** ***Config Selection***, Drop down to select the Config you want
- **2:** ***Delete Config***, Delete the selected Config<br>

<details>
  <summary>Floating Action Buttons View</summary>

![Screenshot_20240611-193038_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/201d0fe3-2d8b-4d99-bc6c-546c14462134)
</details>
<br>

- **1:** ***Export***, Export the Selected Config to a JSON File
- **2:** ***Import***, Import a Config from Storage a JSON File. When importing it will not be saved so make sure you select the save option from the Floating Action Button Menu
- **3:** ***Delete***, Delete the SELECTED settings FROM the Selected Config (Removes the settings from that config)
- **4:** ***Save***, Save the Current Config Modified Settings to the Config and or just when it was Imported Save it. This does NOT SAVE or APPLY the actual Setting Values to the XLUA Database Settings but updates the Config, does not Apply the Config to Settings
- **5:** ***Apply***, Apply the selected Config to Settings, this will actually apply the Values to XPLEX Settings Database<br><br>


## Hooks Control UI
This is the new Hook Control UI that can only be accessed from the button that looks like a Hook under the Application in the Main UI. This can give you more in depth control over the Hooks instead of just selecting the group in whole.

<details>
  <summary>Hook Control View</summary>
  
![Screenshot_20240611-194907_XPL-EX2](https://github.com/0bbedCode/XPL-EX/assets/114315756/06dfa66b-9b07-429e-89df-40b7290a658c)
</details>
<br>

- **1:** ***Group Select***, Select all the Hooks in the Group, Enable them or Disable them
- **2:** ***Group Drop Down***, Drop Down the Group to see the Individual hooks within that group
- **3:** ***Hook Drop Down***, Drop Down the Hook to see the Settings it requires if any (not all Hooks have settings)
- **4:** ***Setting Drop Down***, Drop Down the Setting to see the Embed to control the setting
- **5:** ***Hook Select***, Select the Individual Hook Enable it or Disable it<br><br>

## Debugging Issues
<br>

### Limits Native vs Java


Right away lets explain the limits, currently as if of (6/12/24) XPLEX Does not support Native Hooking, there for applications that use Methods to get the specific detail you are trying to block with Native functions XPLEX cannot reach that. Example Chromium based browsers use Native Functions to get / build their User Agent, there for using User Agent Hooks on browsers like ***Brave***, ***Chrome*** it will not work. Sometimes Apps use either Native "exec" on the command "getprop" or open files using "libc" "fopen" to open files or even use "__system_property_get" function to get Properties. All have in common is Native, they happen on the Native layer there for XPLEX Cannot touch them (yet). Easy cop out to tell if its Native is either reputation of the application as in its known to incorporate native function to complete its actions, and or you configured everything correctly such as settings and Hooks yet the thing in mind is still not being spoofed. Other than that, you as the user would have to make the guess as I as the dev would not have enough time in the world to analyze each app sent.<br><br>

### Wrong Value being Spoofed / Testing Spoof

When opening Target app you see that the target detail in question is being spoofed but to not what you are setting it too ? Seems like a XPLEX issue or poor judgment on your end for assuming that detail is what you think it is when its not. Easy way to test this is this, lets say we assume the detail we are tyring to spoof is Device Brand so the Application Displays Device Manufacturer and even Looks like its being spoofed but not to what we set it as. This is how you should approach this "issue", go into XPLEX and find the settings you may think it is tied too or select all you are using so in this example we will modify the following Values:<br>

- **device.manufacturer** set it to "dog"
- **device.brand** set it to "cat"
- **device.model** set it to "snake"<br><br>

We run the target application and notice the Detail that we are trying to spoof now appears to say "snake", that means that detail you judged as the "manufacturer" setting was wrong and it is actually the phones "model".<br>

### Testing Hooks
The app isn't starting or the Application is crashing but no message and the cat logs don't make much sense ? Here's what you do , you de-select all Hook Groups, then select ONE hook Group, make sure the settings that is requires are configured, then open the target app, if it works then repeat, select ANOTHER group while having the previous ones selected still, open, if it works great keep going. Once you select a Group that then causes the issue in mind, you then go into the new Hooks UI for that group, then de-select it the group. While still in the new UI Select each Individual Hook in that Group until you find the one causing the target Application issues, send that as part of the report and do not select that hook for now, and ensure you are properly setting the setting in correct format.<br>

### Errors Exceptions
When XPLEX has an Error relating to a Hook you will Get a Notification you can click on it will take you to the error, alternatively you can see in the XPLEX application on the Hook group that did produce the error that an exclamation mark appears "!". Clicking on it will produce the Error message in front of you, this message then you can present as an issue on Github and or to me directly via group chat or private message. You can copy the message text and or take screenshots in parts until you have the whole thing screenshotted.<br>

<details>
  <summary>Error Notification</summary>
  
![Screenshot_20240611-205451_AIDA64](https://github.com/0bbedCode/XPL-EX/assets/114315756/4a93236d-33c2-4bce-8be2-34696bb69536)
</details>

<details>
  <summary>Error in XPLEX</summary>
  
![Screenshot_20240611-205533_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/12414574-3ada-4a67-bd5b-d3d7e11ad72a)
</details>

<details>
  <summary>Error Message</summary>
  
![Screenshot_20240611-210208_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/8e944170-da30-47f1-a908-6db8a4e83645)

![Screenshot_20240611-210450_XPL-EX](https://github.com/0bbedCode/XPL-EX/assets/114315756/57b0e7a5-de64-48e7-8251-f226f51939c0)

</details>
<br>

### Sending and Getting Reports and Errors
For me to truly accept an error I need to see Cat Logs / Logs, combining the logs given above you need or may need cat logs as well for more context. On top of that I need to know what App you got this error from what Specific Hook Group and What Hooks you do have selected. To get Cat Logs you can download and install the following app ***Logcat Reader*** or ***com.dp.logcatapp*** for the package name. Once installed go into the Top Right 3 dot menu select clear, then in the search ICON in the app search for "xlua" then re produce the error. Once the error is reproduced go back into the logcat app select the 3 dots once more , select "Save" button, then those save logs export them to me with the rest of the details.<br><br>

### Old Collections M66B and Warning Messages (!!!READ THIS READ THIS!!!)
If the error origins from the "Privacy" Collection and or an Older Update of the App I will not be in support or help. The "Privacy" Collection is Deprecated and is no longer used there for I will not be helping, use the new Collection "PrivacyEx" as it has all old Hooks Merged, Updated. If you have made a modification for the Hook that is causing issues I will not provide support as it MAY be to do with a mistake on your end versus the end of XPLEX. Last before selecting all Hook Groups select each one , one by one or the ones you want, doing this will invoke the Warning Messages for the Hooks that require a warning or can cause issues. If you fail to read these messages and bring me an issue relating to these messages I will ignore the "issue" as you fail to do your bare minimum in reading the warnings.<br>

### Clearing App Data, Updating Folder and App (Possible Fix for Issues)
***ENSURE*** each time you Install a XPLEX Update you MUST select in LSPosed under XPLEX ***Settings Storage*** and MUST reboot the device.<br>
Clearing XPLEX app data will not clear the actual settings of the App but this can also help resolve issues (I Promise it will not delete your assigned hooks or settings) so try this if you have issues within the app. If you actually want to delete your data the Databases for XPLEX are located in "/data/system/xplex-xxx", "xxx" being a random generated string for each installation. In that folder you can delete both Databases then Reboot the phone, this will completely reset everything.<br>
If its your first time Installing XPLEX over XPL you may need to uninstall XPL (again your data will remain) and then install XPLEX. Sometimes transferring the Databases over does not work well so after selecting ***Settings Storage*** in LSPosed for XPLEX and you restarted, its best to go into "/data/system/" folder find the "xplex-" one, make sure the Databases are in there. If you do not see them in there find the "xlua" folder in the same directory in "/data/system/" and Copy the Database that's in there into the "xplex" folder. Then you can delete the older "xlua" folder, if the new one has the one "xlua" database in it and it looks like its the same file size. You can also copy these files somewhere else as a backup copy!<br><br>

### LSPosed (Unchecking Settings Storage)
In LSPosed you may notice when going into it under XPLEX application, you will see that ***Settings Storage*** is unchecked (***Settings Storage*** is required for XPLEX to work). If you remember that you have selected ***Settings Storage*** least once and restart, then you should be fine. If you are not sure it doesn't hurt to select ***Settings Storage*** once more then restart the device, keep that in memory so you don't have to keep repeating this. This appears to be a LSPosed and or XPLEX issue that on my end I cannot seem to solve but even though it is unchecked, if you checked it and restarted it once you are fine!


