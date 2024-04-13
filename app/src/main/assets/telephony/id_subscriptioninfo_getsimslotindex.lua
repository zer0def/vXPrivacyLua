function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local setting = param:getSettingInt("gsm.index.sim.slot", 1)
	if setting == nil then
	    return false
	end

	param:setResult(setting)
	return true, tostring(ret), tostring(setting)
end