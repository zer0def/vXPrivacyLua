function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local mnc = 299
	local fake = param:getSetting("gsm.operator.mnc")
	if fake == nil then
	    return false
	end

    local n = tonumber(fake)
    if n ~= nil then
        mnc = n
    end

	param:setResult(mnc)
	return true, tostring(ret), tostring(mnc)
end