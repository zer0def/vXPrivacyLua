function after(hook, param)
	local fake = param:getSetting("value.langtag")
	local res = param:getResult()

	if fake == nil then 
		fake = "is-IS"
	end

	if fake == "skip_me" then
		return false
	end

	if res ~= nil then 
		log("Fake Language Tag [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end