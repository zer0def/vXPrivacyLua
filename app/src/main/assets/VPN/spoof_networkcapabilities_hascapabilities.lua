function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

    if ret == 0x4 then 
        log("TRANSPORT_VPN Check 0x4")
        param:setResult(false)
        return true
    end

    return false
end