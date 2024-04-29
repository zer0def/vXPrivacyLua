function before(hook, param)
	local cidString = param:getSetting("gsm.cell.location.cid")
	if cidString ~= nil then
	    param:setResult(cidString)
	    return true
	else
		local setting = param:getSetting("LAC,CID")
    	if setting ~= nil then
    		local index = string.find(setting, ",", 1, true)
    		if index ~= nil then
    		    local fakeCid = string.sub(setting, index + 1, -1)
    		    param:setResult(fakeCid)
    		    return true
    		end
    	end
	end
	return false
end