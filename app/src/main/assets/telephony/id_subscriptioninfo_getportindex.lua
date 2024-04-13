function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

	local setting = param:getSettingInt("gsm.index.sim.port", 8)
	if setting == nil then
	    return false
	end

	param:setResult(setting)
	return true, tostring(ret), tostring(setting)
end