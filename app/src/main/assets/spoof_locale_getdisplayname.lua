function after(hook, param)
	local res = param:getResult()

	local country = param:getSetting("value.country")
	local language = param:getSetting("value.language")

	if country == nil then 
		country = "Iceland"
	end

	if language == nil then 
		language = "Icelandic"
	end

	local fake = language .. " (" .. country .. ")"

	if res ~= nil then 
		log("Fake language Display Name [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end