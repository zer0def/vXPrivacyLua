function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSetting("value.phone_number")
	if fake == nil then 
		fake = "1111111111"
	end

	--log("Fake [line1Number] ... ");
	param:setResult(fake)
	return true, ret, fake
end