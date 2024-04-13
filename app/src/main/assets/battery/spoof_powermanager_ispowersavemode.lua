function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local setting = param:getSetting("battery.is.power.save.mode.bool", "false")
    if setting == nil then
        return false
    end

    local fake = true
    if setting == 'true' then
        fake = true
    end

	param:setResult(fake)
	return true, tostring(res), tostring(fake)
end