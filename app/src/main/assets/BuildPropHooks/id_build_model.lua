function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.MODEL")
    if fake == nil then
        fake = "Pixel XL"
    end

    param:setResult(fake)
    return true, ret, fake
end