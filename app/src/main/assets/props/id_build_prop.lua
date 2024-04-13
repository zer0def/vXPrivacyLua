function after(hook, param)
	local prop = param:getArgument(0)
	if prop == nil then 
		log("Param is NULL skipping...");
		return false
	end

	--local ret = param:getResult()
	--if res == nil then
	--	log("Filtering Property::" .. prop)
	--else
	--	log("Filtering Property::" .. prop .. " [RETURN]::" .. ret)
	--end

	local filtered = param:filterBuildProperty(prop)
	if filtered == "NotBlacklisted" then 
		return false
	end

	--log("Filtered Property::[" .. prop .. "] => " .. filtered)
	param:setResult(filtered)
	return true
end