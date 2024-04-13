function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

	local setting = param:getSettingReMap("zone.country.iso", "phone.countryiso", "IS")
	if setting == nil then
	    return false
	end

	param:setResult(setting)
	return true, ret, setting
end