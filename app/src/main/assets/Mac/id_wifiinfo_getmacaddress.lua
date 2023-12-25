function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local def = "00:1A:2B:3C:4D:5E"
    local fake = param:getSetting("net.mac", def)
    --if fake:length() > 17 or fake:length() < 12 then
    --	fake = def
    --end

    param:setResult(fake)
    return true, ret, fake
end