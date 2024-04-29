function before(hook, param)
	local lacString = param:getSetting("gsm.cell.location.lac")
	if lacString ~= nil then
	    param:setResult(lacString)
	    return true
	else
		local setting = param:getSetting("LAC,CID")
    	if setting ~= nil then
    		local index = string.find(setting, ",", 1, true)
    		if index ~= nil then
    		    local fakeLac = string.sub(setting, 1, index -1)
    		    param:setResult(fakeLac)
    		    return true
    		end
    	end
	end
	return false
end