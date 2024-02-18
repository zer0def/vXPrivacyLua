function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local mcc = "274"
	local fake = param:getSetting("gsm.operator.mcc")
	if fake ~= nil and tonumber(fake) ~= nil then
		mcc = fake
	end

	param:setResult(mcc)
	return true, ret, mcc
end