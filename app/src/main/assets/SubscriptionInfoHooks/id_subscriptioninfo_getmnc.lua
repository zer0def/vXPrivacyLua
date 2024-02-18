function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local mnc = 299
	local fake = param:getSetting("gsm.operator.mnc")
	if fake ~= nil then
		local n = tonumber(fake)
		if n ~= nil then 
			mnc = n
		end
	end

	param:setResult(mnc)
	return true, tostring(ret), tostring(mnc)
end