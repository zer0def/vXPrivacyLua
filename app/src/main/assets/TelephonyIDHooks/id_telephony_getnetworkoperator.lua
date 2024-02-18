function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		param:setResult(null)
		return true
	end

	local fake = param:getSetting("gsm.operator.id")
	--local setting = param:getSetting("MMC,MNC")
	local mcc = param:getSettingReMap("gsm.operator.mcc", "phone.mmc")
	local mnc = param:getSettingReMap("gsm.operator.mnc", "phone.mnc")
	if mmc ~= nil and mnc ~= nil then
		if tonumber(mcc) ~= nil and tonumber(mnc) ~= nil then
			fake = mcc .. mnc
		end
		--local index = string.find(setting, ",", 1, true)
		--if index ~= nil then
		--	local fakemmc = string.sub(setting, 1, index - 1)
		--	local fakemnc = string.sub(setting, index + 1, -1)
		--	if fakemmc ~= nil and fakemnc ~= nil then
		--		if tonumber(fakemmc) ~= nil and tonumber(fakemnc) ~= nil then
		--			fake = fakemmc .. fakemnc
		--			--log("Setting Fake MMC & MNC::" .. fake)
		--			--param:setResult(fake)
		--			--return true, ret, fake
		--		end
		--	end
		--end
	end

	param:setResult(fake)
	return true, ret, fake
end