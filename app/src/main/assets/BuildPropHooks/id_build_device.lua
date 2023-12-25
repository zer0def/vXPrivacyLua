function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.DEVICE")
    if fake == nil then
        fake = "zerofltexx"
    end

    param:setResult(fake)
    return true, ret, fake
end