function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSettingReMap("gsm.network.carrier", "phone.isp", "Siminn")
	if fake == nil then
	    return false
	end

	param:setResult(fake)
	return true, ret, fake
end