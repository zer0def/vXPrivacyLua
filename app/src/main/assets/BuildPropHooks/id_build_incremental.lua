function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.INCREMENTAL")
    if fake == nil then
        fake = "N960USQU2CSI1"
    end

    param:setResult(fake)
    return true, ret, fake
end