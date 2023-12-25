function after(hook, param)
	local res = param:getResult()
	local fake = "Greenwich Mean Time"

	if res ~= nil then
		log("Fake [getDisplayName] [" .. res .. "] => [" .. fake .. "]")
	end

	param:setResult(fake)
	return true
end