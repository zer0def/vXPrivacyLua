function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.BOOTLOADER")
    if fake == nil then
        fake = "G1HZK00500"
    end

    param:setResult(fake)
    return true, ret, fake
end