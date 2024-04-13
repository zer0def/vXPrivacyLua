function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

	local setting = param:getSettingInt("battery.charge.time.remaining", 0)
	if setting == nil then
	    return false
	end

	param:setResult(setting)
	return true, tostring(res), tostring(setting)
end