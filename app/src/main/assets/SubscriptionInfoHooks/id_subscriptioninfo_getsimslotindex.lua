function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local settingValue = param:getSetting("gsm.index.sim.slot", "1")
	local fake = tonumber(settingValue)
	param:setResult(fake)
	return true, tostring(ret), settingValue
end