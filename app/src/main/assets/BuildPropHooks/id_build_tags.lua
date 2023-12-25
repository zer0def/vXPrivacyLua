function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.TAGS")
    if fake == nil then
        fake = "release-keys"
    end

    param:setResult(fake)
    return true, ret, fake
end