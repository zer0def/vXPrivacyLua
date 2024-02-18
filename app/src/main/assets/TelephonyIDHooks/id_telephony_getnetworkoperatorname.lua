function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSettingReMap("gsm.network.carrier", "phone.isp", "Siminn")
	param:setResult(fake)
	return true, ret, fake
end