function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	log("isPowerSaveMode")

	param:setResult(false)
	return true
end