function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.display.id", "TQ3A.230805.001 release-keys")
    param:setResult(fake)
    return true, ret, fake
end