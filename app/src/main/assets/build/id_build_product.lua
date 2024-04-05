function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.codename", "yaap_guacamoles")
    param:setResult(fake)
    return true, ret, fake
end