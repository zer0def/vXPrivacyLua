function after(hook, param)
	local fake = param:getSetting("value.isocountry")
	local res = param:getResult()

	if fake == nil then 
		fake = "ISL"
	end

	if fake == "skip_me" then
		return false
	end

	if res ~= nil then 
		log("Fake ISO3 Country [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end