function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("gsm.setting.display.name", "Rob")
	param:setResult(fake)
	return true, ret, fake
end