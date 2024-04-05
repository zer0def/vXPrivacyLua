function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local mnc = "299"
	local fake = param:getSetting("gsm.operator.mnc")
	if fake ~= nil and tonumber(fake) ~= nil then
		mnc = fake
	end

	param:setResult(mnc)
	return true, ret, mnc
end