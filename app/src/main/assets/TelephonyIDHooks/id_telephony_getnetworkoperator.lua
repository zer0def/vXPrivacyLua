function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		param:setResult(null)
		return true
	end

	local fake = "274299"
	--local setting = param:getSetting("MMC,MNC")
	local mmc = param:getSetting("phone.mmc")
	local mnc = param:getSetting("phone.mnc")
	if mmc ~= nil and mnc ~= nil then
		if tonumber(mmc) ~= nil and tonumber(mnc) ~= nil then
			fake = mmc .. mnc
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