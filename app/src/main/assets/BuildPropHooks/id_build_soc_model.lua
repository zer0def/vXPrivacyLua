function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("cpu.soc.model", "SM8150")
    param:setResult(fake)
    return true, ret, fake
end
