function after(hook, param)
	local fake = param:getSetting("value.language")
	local res = param:getResult()

	if fake == nil then 
		fake = "Icelandic"
	end

	if fake == "skip_me" then
		return false
	end

	if res ~= nil then 
		log("Fake language Display [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end