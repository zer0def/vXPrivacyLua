function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.SERIAL")
    if fake == nil then
        fake = "HT35W1A00476"
    end

    param:setResult(fake)
    return true, ret, fake
end