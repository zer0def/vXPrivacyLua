function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

    if string.match(ret, "tun") or string.match(ret, "pptp") or string.match(ret, "ppp") then 
        log("VPN Interface Spoofing...")
        param:setResult("unknown")
        return true
    end

    return false
end