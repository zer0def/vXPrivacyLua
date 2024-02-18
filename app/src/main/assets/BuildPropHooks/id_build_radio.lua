function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.radio", "M8994F-2.6.36.2.20")
    param:setResult(fake)
    return true, ret, fake
end