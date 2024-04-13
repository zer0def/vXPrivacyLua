function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local setting = param:getSetting("gsm.setting.network.opportunistic.bool", "false")
    if setting == nil then
        return false
    end

    local fake = false
    if setting == "true" then
        fake = true
    end

	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end