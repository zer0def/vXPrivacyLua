function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

	local fake = 8

	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end