function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.INCREMENTAL", "N960USQU2CSI1")
    param:setResult(fake)
    return true, ret, fake
end