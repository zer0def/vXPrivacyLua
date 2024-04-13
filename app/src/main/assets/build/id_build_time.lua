function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.date.utc")
    if fake == nil then
        return false
    end

    param:setResultToLong(fake)
    return true, tostring(ret), fake
end