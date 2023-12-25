function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.RADIO")
    if fake == nil then
        fake = "M8994F-2.6.36.2.20"
    end

    param:setResult(fake)
    return true, ret, fake
end