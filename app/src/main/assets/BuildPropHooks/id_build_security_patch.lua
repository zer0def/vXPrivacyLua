function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.SECURITY_PATCH")
    if fake == nil then
        fake = "2024-05-03"
    end

    param:setResult(fake)
    return true, ret, fake
end