function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.ID", "OPM1.171019.011")
    param:setResult(fake)
    return true, ret, fake
end