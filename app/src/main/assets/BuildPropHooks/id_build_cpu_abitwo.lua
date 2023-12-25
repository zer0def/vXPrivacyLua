function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.CPU_ABI2")
    if fake == nil then
        fake = "armeabi"
    end

    param:setResult(fake)
    return true, ret, fake
end