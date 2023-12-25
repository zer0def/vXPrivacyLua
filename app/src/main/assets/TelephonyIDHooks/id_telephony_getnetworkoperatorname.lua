function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSetting("phone.isp")
	if fake == nil then
		fake = "Siminn"
	end

	param:setResult(fake)
	return true, ret, fake
end