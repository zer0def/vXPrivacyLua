function after(hook, param)
	local fake = param:getSetting("value.tzid")
	local res = param:getResult()

	if fake == nil then
		fake = "Atlantic/Reykjavik"
	end

	if res ~= nil then
		log("Fake [getID] [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end