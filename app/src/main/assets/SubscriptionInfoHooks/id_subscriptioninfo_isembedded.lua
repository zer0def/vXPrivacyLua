function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = false
	if ret == false then
		fake = true
	end

	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end