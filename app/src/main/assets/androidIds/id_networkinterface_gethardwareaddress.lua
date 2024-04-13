function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	log("Spoofing MAC")

    local mac_bytes = param:getFakeMacAddressBytes()
    if mac_bytes == nil then
        return false
    end

    param:setResultByteArray(mac_bytes)
    return true
end