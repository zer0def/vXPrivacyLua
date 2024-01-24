function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.SECURITY_PATCH", "2024-05-03")
    param:setResult(fake)
    return true, ret, fake
end