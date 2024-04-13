function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local mcc = 1337
	local fake = param:getSetting("gsm.operator.mcc")
	if fake == nil then
	    return false
	end

    local n = tonumber(fake)
    if n ~= nil then
        mcc = n
    end
	param:setResult(mcc)
	return true, tostring(ret), tostring(mcc)
end