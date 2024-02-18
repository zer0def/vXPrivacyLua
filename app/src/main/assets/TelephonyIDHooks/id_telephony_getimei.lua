function after(hook, param)
	--local ths = param:getThis()
	local ret = param:getResult()
	if ret == nil then
	    return false
	end
	--if ths == nil or ret == nil then
	--	return false
	--end

	--local typ = ths:getPhoneType()
	--local fake

	--if typ == 1 then
	--	fake = param:getSettingReMap("unique.gsm.imei", "value.imei", "000")
	--	log("GSM [IMEI]")
	--end

	--log("Setting IMEI [" .. res .. "] => " .. fake)
	local fake = param:getSettingReMap("unique.gsm.imei", "value.imei", "000")
	param:setResult(fake)
	return true, ret, fake
end