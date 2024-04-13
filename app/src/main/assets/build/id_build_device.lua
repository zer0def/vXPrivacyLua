function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("device.nick.name", "OnePlus7ProNR")
    if fake == nil then
        return false
    end
    param:setResult(fake)
    return true, ret, fake
end