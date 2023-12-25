function after(hook, param)
	local ths = param:getThis()
	local ret = param:getResult()
	if ths == nil or ret == nil then
		return false
	end

	local typ = ths:getPhoneType()
	local fake

	if typ == 1 then 
		fake = param:getSetting("value.imei")
		log("GSM [IMEI]")
	end

	if fake == nil then
		fake = "000"
	end

	--log("Setting IMEI [" .. res .. "] => " .. fake)
	param:setResult(fake)
	return true, ret, fake
end