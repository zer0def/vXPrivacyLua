function after(hook, param)
	local fake = param:getSetting("value.tzshort")
	local res = param:getResult()
	local tzcl = luajava.bindClass("java.util.TimeZone")
	if fake == nil then 
		fake = "GMT+0"
	end

	local tz = tzcl:getTimeZone(fake)

	if res ~= nil then
		log("Fake [getTimeZone] [" .. fake .. "]")
	end

	param:setResult(tz)
	return true
end