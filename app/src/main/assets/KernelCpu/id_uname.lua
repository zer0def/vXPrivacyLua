function after(hook, param)
	local uname = "android.system.StructUtsname"
	local s = param:getSetting("cpu.sysname", "Unix")
	local n = param:getSetting("cpu.nodename", "localhost")
	local r = param:getSetting("cpu.release", "Kool-Kernel-RS3899238")
	local v = param:getSetting("cpu.version", "#1 SMP PREEMP Wen Jan 21 11:28:12 AEST 1970")
	local m = param:getSetting("cpu.machine", "x86_64")

	local fake = luajava.newInstance(uname, s, n, r, v, m)
	log("[sysname]: " .. s .. "\n[nodename]: " .. n .. "\n[release]: " .. r .. "\n[version]: " .. v .. "\n[machine]: " .. m)
	param:setResult(fake)

	return true
end