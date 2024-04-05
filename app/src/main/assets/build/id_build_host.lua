function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.host", "ido-X3999A")
    param:setResult(fake)
    return true, ret, fake
end