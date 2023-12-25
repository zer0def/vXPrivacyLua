function after(hook, param)
	local fake = param:getSetting("value.country")
	local res = param:getResult()

	if fake == "skip_me" then
		return false
	end

	if fake == nil then 
		fake = "Iceland"
	end

	if res ~= nil then 
		log("Fake Language Country [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end