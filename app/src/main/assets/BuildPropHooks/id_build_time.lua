function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.TIME")
    if fake == nil then
        fake = "1609459200000"
    end

    param:setResult(fake)
    return true, ret, fake
end