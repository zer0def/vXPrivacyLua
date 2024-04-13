function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local settingValue = param:getSetting("gsm.sim.card.id", "8457894")--was 8457894
	if settingValue == nil then
	    return false
	end
	--check length
	--local fake = tonumber(settingValue)
	--if fake == nil then
	--    fake = 321
	--end

	param:setResultToLongInt(settingValue)
	return true, tostring(ret), settingValue
end