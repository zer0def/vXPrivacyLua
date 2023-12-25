function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.ID")
    if fake == nil then
        fake = "OPM1.171019.011"
    end

    param:setResult(fake)
    return true, ret, fake
end