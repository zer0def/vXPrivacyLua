function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("unique.gsm.icc.id", "891004234814455936F")
	param:setResult(fake)
	return true, ret, fake
end