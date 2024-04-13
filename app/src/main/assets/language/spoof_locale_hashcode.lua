function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

	local fake = "100468396";
	log("Fake [hashCode] [" .. fake .. "]")
    param:setReturnToIntFromStr(fake);
	return true, tostring(res), fake
end