# XPL-EX
Hooking + Privacy Framework For Android 6.0 +

XPL-EX
------------
All though this is said to be a fork, this point of the Project it is taken a complete different direction and most code replaced over 50k lines while around 5k lines of the original code remain. I don't feel need at the moment to convert it to my own REPO so for now it will be linked to the base as a fork!<br>
To understand it easily, XPL-EX is an application that conceals other applications from accessing any information about your device, whether the data is unique or generic. This project was originally forked from M66B's work, although, at this point, very few parts of the original code remain. Most of what exists now has been modified or added by me (over 40,000 lines modified/added). I further developed this app not just to fulfill a childhood dream but also to challenge the companies that trade our data, aiming to inspire hope for our future privacy. I seek no financial gain, only privacy for future generations.<br><br>

XPL-EX is a hooking framework designed for privacy, arguably the best privacy-focused Xposed application for Android. It offers numerous hooks, significantly more than its competitors, and it is entirely open-source and free. No internet connection is required, no background analytics, no logging to servers – nothing! Just pure Java Android native UI, no authentication, all free and open-source.

This framework targets any selected application, masking your real data by feeding the targeted apps fabricated information. For example, if an app detected your device had 8 GB of RAM, with the memory options applied in XPL-EX, it could now detect 900 GB of RAM (or whatever you select). If your device is a Samsung, you can choose device name/manufacturer options to masquerade as an iPhone or Pixel – the possibilities are vast. While the applications won't fully believe they're interacting with an iPhone, given the JVM environment, it illustrates the app's potential.

We live in a world where applications consistently collect and sell user data. Ironically, some apps that offer similar functionalities to XPL-EX come with built-in data analytics and provide significantly fewer options. Some even require payment for features that XPL-EX offers for free, making alternative options less appealing.

Beyond privacy, this app serves various other purposes, allowing you to manipulate target applications if you know how to write LUA (the scripts executed within the hooks use LUA). XPL-EX is more than just a privacy tool; it's a comprehensive hooking framework that allows users to define hooks dynamically within the app.

As of now (March 26, 2024), XPL-EX only supports Java hooks, not native hooks. However, since most data requests are served through Java APIs, native APIs are less crucial (even with JNI, the execution still transitions through Java, where XPL-EX can intercept). In the future, XPL-EX plans to support native hooking, aiming to be an all-in-one hooking solution and the best application for protecting privacy from other apps.

In App
------------

<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/d32115bd-2c6b-4a57-93bb-b7f8d523448c" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/a160a7c1-b4ce-4c89-8989-ce96fca7a474" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/d51e9c78-6efb-48f2-876a-ef5c69e48fd6" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/25b82519-309f-4b0e-9d12-87091ec699d6" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/e68d1ed2-3878-4a2e-bfb0-9b23de1bae76" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/002e12b3-ab4d-4a4b-944b-9dff9fc84c76" width="311" height="640">
<img src="https://github.com/0bbedCode/XPL-EX/assets/114315756/b830af03-f143-44eb-a30d-7aa7df24004b" width="311" height="640">


To Come
------------
XPL-EX is not just an application designed for privacy; it also harbors potential for future enhancements in areas such as reverse engineering and concealing elements like Xposed, root access, and more. However, the primary focus remains steadfastly on privacy, with other features set to follow in due course.

This repository will offer a modified version of the Pro Companion app, which will facilitate free communication with XPL-EX. In time, the plan is to integrate all these functionalities into a single, unified application. This strategic approach ensures that while we expand the app's capabilities, its core mission of ensuring user privacy remains at the forefront.

Help
------------

[XPL DEFINE](https://github.com/M66B/XPrivacyLua/blob/master/DEFINE.md) <br>
[XPL-EX UI Help Docs or Usage Help Docs (How to use this app)](https://github.com/0bbedCode/XPL-EX/blob/new/APPHELP.md) <br>
[XPL-EX API](https://github.com/0bbedCode/XPL-EX/blob/new/APIHELP.md) <br>
[XPL-EX Examples](https://github.com/0bbedCode/XPL-EX/blob/new/LUAHELP.md) <br>
[Hooked Apis in XPL-EX](https://github.com/0bbedCode/XPL-EX/blob/main/HOOKED.md) <br>

Do note for every Hook that is not re-created or added within the 'PrivacyEx' or XPL-EX Collection (it will be added) but you can use both Collections at once.<br>
You cen switch between the two collections in the "XPrivacy-LUA Pro Companion App". Please Avoid Selecting Hook Groups with similar Hooks

If the Hooks do not appear , nor the Collection appears in the Pro Companion App even after a Reboot. <br>
Use a root file explorer navigate to the Directory "/data/system/xlua/" Backup any File in there (xlua) database, then delete it. Restart the Device <br>
Later versions this will be 'fixed' / alot easier to work with within the UI etc <br>

### Telegram Group
https://t.me/xplexex

### Telegram
https://t.me/obbedcode

### Telegram Discussion Chat
https://t.me/XPLEXCHAT

### XDA Thread
https://xdaforums.com/t/xpl-ex-xprivacylua-ex-android-privacy-manager-hooking-manager-extended.4652573/

### Discord , Nulled (ObbedCode)<br><br>

## XPL-EX Development Roadmap

### Completed Features
- [X] Hook Java APIs
- [X] Compatibility with Xposed, LSPosed, EdXposed, ThaiChi, VXP
- [X] Dynamic Privacy Features (Free and Open Source Software)
- [X] Modification of Hook Behavior using LUA Scripts
- [X] Built-in Pre-defined Hooks for Enhanced Privacy
- [X] UI Component for Intercepting System Properties (`System.getProperty` / `SystemProperties.get`)
- [X] UI Component to Select CPU Maps for the System Information File `/proc/cpuinfo`
- [X] Expanded API Functions for Use in LUA Scripts
- [X] Updated LUA Core (Note: Current version does not support BIG INT still...)
- [X] Enhanced and Optimized Hooks for Android 14 and or Just Enhanced
- [X] Dark Mode Feature in Main Application
- [X] Added Settings UI
- [X] Added ALOT more Properties
- [X] Added Config UI 

### Planned Enhancements
- [ ] Add new Text to different languages (Tanslation)
- [ ] Class Finder to Identify Classes Inheriting Specific Interfaces (e.g., `javax.microedition.khronos.opengles.GL10`)
- [ ] Protection Against Inner Process Communication
- [ ] Adding Icons and Refining Text & Names for CPUs
- [X] Updating and Fixing LUA Code for More Hooks
- [ ] Secure Communication between Hook and XPL-EX
- [X] Code Cleanup (Focus on UI Development)
- [ ] Hook Native APIs
- [ ] Target Native APIs (e.g., `__system_property_get`, `popen`, `fopen`, `open`, `exec`) for Enhanced Privacy
- [X] Additional API Functions for LUA Scripts
- [ ] Integration of 'M66B's "XPrivacyLua-Pro Companion App" Features into Main App "XPL-EX"
- [ ] UI for Modifying/Mocking Specific Device Sensors
- [ ] Built-in Pre-defined Hooks for Reverse Engineering, Logging, Bypassing/Hiding, and Disabling Ad Libraries in Apps
- [ ] Mode/View Switcher (e.g., "Reverse Engineering Mode," "Privacy Mode")
- [ ] System for Selecting Phone Configurations for Spoofing
- [ ] Support for Frida
- [ ] Floating Menu in Hooked Apps for UI Inspection (Similar to "Reflect Master")
- [ ] Incorporation of Engaging Visual Elements and Progress Indicators
- [ ] Documentation Refinement and Language Corrections

**Note:** This roadmap is subject to change based on project development and user feedback.

* From https://github.com/M66B/XPrivacyLua

Notes
-----

* Some apps will start the camera app to take pictures. This cannot be restricted and there is no need for this, because only you can take pictures in this scenario, not the app.
* Some apps will use [OpenSL ES for Android](https://developer.android.com/ndk/guides/audio/opensl-for-android.html) to record audio, an example is WhatsApp. Xposed cannot hook into native code, so this cannot be prevented.
* The get applications restriction will not restrict getting information about individual apps for stability and performance reasons.
* The telephony data restriction will result in apps seeing a fake IMEI. However, this doesn't change the IMEI address of your device.
* Restricting activity recognition (location) results for recent Google Maps versions and possibly other apps in the error *... java.lang.ClassNotFoundException ...* for unknown reasons.

Compatibility
-------------

XPL-EX is supported on Android 6.0 Marshmallow and later.
For Android 4.0.3 KitKat to Android 5.1.1 Lollipop you can use [XPrivacy](https://github.com/M66B/XPrivacy/blob/master/README.md) (unsupported though).

XPL-EX is supported for smartphones and tablets only. XPL-EX is not supported on emulators.

Hooking *com.google.android.gms.location.ActivityRecognitionResult.extractResult* (restriction *Determine activity*)
is known to fail with *script:25 vm error: java.lang.ClassNotFoundException: com.google.android.gms.location.DetectedActivity*
and *script:28 attempt to call nil* for some apps, like Google Maps and NetFlix, for yet unknown reasons. (maybe I can fix this OBC)

Installation
------------

* Install [Magisk](https://www.xda-developers.com/how-to-install-magisk/)
* Android 6-7: download, install, and activate [Xposed](http://forum.xda-developers.com/xposed)
* Android 8.1-12: download, install, and activate [LSPosed](https://forum.xda-developers.com/t/lsposed-xposed-framework-8-0-11-0-edxposed-alternetive.4228973/) ~~or [EdXposed](https://forum.xda-developers.com/t/official-edxposed-the-successor-of-xposed-oreo-pie-q-r-2020-07-19.4070199)~~
* Download, install, and activate [XPL-EX]()

LSPosed: please make sure that any app you want to restrict is [in the 'scope'](https://github.com/LSPosed/LSPosed/wiki/How-to-use-it#install--activate-modules) of LSposed.

Frequently Asked Questions
--------------------------

See [here](https://github.com/0bbedCode/XPL-EX/blob/master/FAQ.md) for a list of often asked questions. (XPrivacy-Lua)


Donate
------

### ObbedCode

- BTC: `bc1q9daenk4sdfcxnqxducrxy69zfyruf6un5wytge`
- XMR: `489AB9gBxvKDbF1cJXXpFnWZ7ZPKsp6JWdZrxaytS4ceZV53fQ1Wj9nWBAotYEWGwPZByJyySmwPD1q8F1g7Pags6gHcKHa`
- ETH: `0x4282Bc864B29Dbd62B9A9960A71e8343eDA44707`
- LTC: `ltc1qlhlnfspn6j8v5s9xxp297zq2m3vhlk9dlrrvvk`
- BCH: `bitcoincash:qz5scaha4gyh92pjw5z5uhuy33sm2rdh5v0tpw2ggm`

### M66B

- https://lua.xprivacy.eu/
- https://github.com/M66B/XPrivacyLua
