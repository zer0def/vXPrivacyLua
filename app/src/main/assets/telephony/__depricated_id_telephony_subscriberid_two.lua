function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local setting = param:getSettingReMap("unique.gsm.subscription.id", "phone.subscriberid")
	if setting ~= nil then
        param:setResult(setting)
        return true, ret, setting
	end

	local mcc = param:getSettingReMap("gsm.operator.mcc", "phone.mcc", "274")
	local mnc = param:getSetting("gsm.operator.mnc", "phone.mnc", "299")
	--next line is 9 chars MSIN
	local msin = param:getSettingReMap("unique.gsm.operator.msin", "phone.msin", "842762952")
	--Returns the unique subscriber ID, for example, the IMSI for a GSM phone.
	if tonumber(mcc) ~= nil and tonumber(mnc) ~= nil and tonumber(msin) ~= nil then
        --local fake = param:getSetting("phone.subscriberid", "000000000")
        local fake = mcc .. mnc .. msin
        param:setResult(fake)
        return true, ret, fake
	end

	return false
end