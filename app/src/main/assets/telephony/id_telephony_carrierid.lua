--getSubscriberId
function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local setting = param:getSetting("gsm.operator.id")
    if param:isNumericString(setting) then
        param:setResultToLongInt(setting)
        return true, tostring(ret), setting
    else
        local mcc = param:getSettingReMap("gsm.operator.mcc", "phone.mcc", "274")
        local mnc = param:getSettingReMap("gsm.operator.mnc", "phone.mnc", "299")
        if param:isNumericString(mcc) and param:isNumericString(mnc) then
            local fake = mcc .. mnc
            param:setResultToLongInt(fake)
            return true, tostring(ret), fake
        end
    end
	return false
end