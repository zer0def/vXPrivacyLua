function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSetting("zone.country.iso", "IS")
	param:setResult(fake)
	return true, ret, fake
end