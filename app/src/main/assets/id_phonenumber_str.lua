function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("unique.gsm.phone.number")
    if fake == nil then
        fake = param:getSetting("value.phone_number", "6666666666")
    end

	--log("Fake [line1Number] ... ");
	param:setResult(fake)
	return true, ret, fake
end