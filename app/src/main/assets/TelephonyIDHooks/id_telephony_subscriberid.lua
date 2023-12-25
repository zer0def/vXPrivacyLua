--getSubscriberId
function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = param:getSetting("phone.subscriberid")
	if fake == nil then
		fake = "000000000"
	end

	param:setResult(fake)
	return true, ret, fake
end