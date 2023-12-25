function after(hook, param)
	local res = param:getResult()
	local fake = "is_IS"
	if res ~= nil then 
		log("Fake toString Locale [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end