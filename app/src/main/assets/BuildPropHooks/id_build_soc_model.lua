function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("build.SOC_MODEL", "MSM8926")
    param:setResult(fake)
    return true, ret, fake
end
