function after(hook, param)
	local res = param:getResult()
	if res == nil then
	    return false
	end

    local fake = param:getSettingReMap("zone.timezone", "zone.tzshort", "GMT+0")
    local tzcl = luajava.bindClass("java.util.TimeZone")
    local tz = tzcl:getTimeZone(fake)
	param:setResult(tz)
	return true, "I dunno", fake
end