function after(hook, param)
    local m = param:getArgument(0)
    if m == nil then
        return false
    end

    local total = param:getSetting("memory.total")
    local avail = param:getSetting("memory.available")

    if total == nil or avail == nil then
        total = "80"
        avail = "70"
    end

    local t = tonumber(total)
    local a = tonumber(avail)

    if t == nil or a == nil then
        return false
    end

    log("Spoofing Memory => [TOTAL]:[" .. total .. "] [AVAIL]:[" .. avail .. "]")
    param:populateMemoryInfo(m, t, a)

	--local a = "9000000000"
	--local m = "90000000000"
	--local t = "1000000"
	--param:setFakeMemory(m, a, m, t, false)
	--log("[MemoryInfo].avilMem=" .. a .. " => [max] => " .. m .. " => [thr] => " .. t)
	--local mem = param:getFakeMemoryInfo(a, m, t, false)
	--param:setResult(mem)
	return true
end