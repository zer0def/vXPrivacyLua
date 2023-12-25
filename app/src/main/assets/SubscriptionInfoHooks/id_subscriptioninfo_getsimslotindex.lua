function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local fake = -1
	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end