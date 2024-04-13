function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("device.model", "GM1920")
    if fake == nil then
        return false
    end
    param:setResult(fake)
    return true, ret, fake
end