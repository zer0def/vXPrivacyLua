function after(hook, param)
	--local fake = param:getSetting("value.tzhshcode")
	--local res = param:getResult()
	--if fake == nil then
	--	fake = 100468396
	--end

	--if res ~= nil then
	--	log("Fake [hashCode] [" + res + "] => [" + fake + "]")
	--end

	local fake = "100468396";
    --local fake = 100468396
	log("Fake [hashCode] [" .. fake .. "]")
    param:setReturnToIntFromStr(fake);
	--param:setResult(fake)
	return true
end