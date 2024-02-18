function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

	local settingValue = param:getSetting("gsm.index.sim.port", "8")
	local fake = tonumber(settingValue)
	param:setResult(fake)
	return true, tostring(ret), settingValue
end