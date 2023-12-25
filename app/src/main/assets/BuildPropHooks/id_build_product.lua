function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.PRODUCT")
    if fake == nil then
        fake = "marlinex"
    end

    param:setResult(fake)
    return true, ret, fake
end