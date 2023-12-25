function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.DISPLAY")
    if fake == nil then
        fake = "NMF26X"
    end

    param:setResult(fake)
    return true, ret, fake
end